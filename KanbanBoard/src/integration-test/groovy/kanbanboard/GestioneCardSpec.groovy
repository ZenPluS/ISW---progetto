package kanbanboard

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import groovy.sql.Sql
import org.hibernate.SessionFactory
import spock.lang.Specification

import javax.sql.DataSource

@Integration
@Rollback
class GestioneCardSpec extends GebSpec {

    static transactional = false
    SessionFactory sessionFactory
    DataSource dataSource
    File schemaDump
    Sql sql

    def nomeBoard = "KanbanProject"
    def mainUser = "user"
    def username1 = "Davide"
    def username2 = "Giacomo"

    def setup() {
        if(dataSource != null) {
            sql = new Sql(dataSource)
            schemaDump = File.createTempFile("test-database-dump", ".sql")
            sql.execute("script drop to ${schemaDump.absolutePath}")
        }

        createUser(username1 , "1234")
        createUser(username2 , "1234")
        createUser(mainUser , "1234")
        login()
        createBoard(nomeBoard)
        createColumn("Backlog")
        createColumn("Ready")
        createColumn("Work in Progress")
        createColumn("Done")
        $("a.adduser").click()
        $("form")."utenteSelezionato" = [username1 , username2]
        $("input" , name: "_action_aggiorna").click()
    }

    def cleanup() {
        if(dataSource != null) {
            sql.execute("runscript from ${schemaDump.absolutePath}")
            sessionFactory.currentSession.clear()
            schemaDump.delete()
        }
    }

    def createBoard(String nomeBoard){
        $("a.create").click()
        $("input#nomeBoard").value(nomeBoard)
        $("input#createBoard").click()
    }

    def createColumn(String nomeColonna){
        $("a.create").click()
        $("input#nomeColonna").value(nomeColonna)
        $("input" , name: "_action_createColumn").click()
    }

    def createUser(String username, String password){
        go '/'
        $("a#registra").click()
        $("input" , id:"username").value(username)
        $("input" , id:"password").value(password)
        $("input" , id:"utenteCreate").click()
    }

    def createCard(String titolo , String descrizione){
        $("input" , id: "nomeCard").value(titolo)
        $("textarea" , id: "descrizione").value(descrizione)
        $("form")."scadenza_day" = 1
        $("form")."scadenza_month" = 1
        $("form")."scadenza_year" = "2017"
        $("input" , name: "_action_createCard").click()
    }

    void login(){
        go '/'
        $("a#login").click()
        $("input" , id:"username").value(mainUser)
        $("input" , id:"password").value("1234")
        $("input" , id:"submit").click()
    }

    void "Stato di inizio"(){
        expect: "Board con 3 collaboratori e con quattro colonne"
            Board.findByName(nomeBoard) != null
            Utenti.findAll().size() == 3
            Board.findByName(nomeBoard).users.size() == 3
            Colonna.findAll().size() == 4
            $("p.column-title" , 0).text() == "Backlog"
            $("p.column-title" , 1).text() == "Ready"
            $("p.column-title" , 2).text() == "Work in Progress"
            $("p.column-title" , 3).text() == "Done"
            title == "Board: " + nomeBoard
    }

    void "Crea Card"(){
        given: "Premo sul bottone 'Aggiungi Card' di 'Backlog'"
            $("a p.column-addcard" , 0).click()

        when:"Compilo la card con un titolo non valido"
            $("input" , id: "nomeCard").value(" ")
            $("textarea" , id: "descrizione").value("descrizione")
            $("input" , name: "_action_createCard").click()
        then: "Viene mostrato un messaggio di errore"
            $("p.inputError").text() != null
            Colonna.findByName("Backlog").cards.size() == 0
            Card.findAll().size() == 0
            title == "Crea Card"

        when:"Compilo la card con una descrizione non valida"
            $("input" , id: "nomeCard").value("titolo")
            $("textarea" , id: "descrizione").value(" ")
            $("input" , name: "_action_createCard").click()
        then: "Viene mostrato un messaggio di errore"
            $("p.inputError").text() != null
            Colonna.findByName("Backlog").cards.size() == 0
            Card.findAll().size() == 0
            title == "Crea Card"

        when:"Compilo la card dati validi"
            $("input" , id: "nomeCard").value("Editing Card")
            $("textarea" , id: "descrizione").value("Form per la modifica delle card")
            $("form")."scadenza_day" = 1
            $("form")."scadenza_month" = 1
            $("form")."scadenza_year" = "2017"
            $("input" , name: "_action_createCard").click()
        then: "Viene mostrata la board con la card nella colonna selezionata"
            Board.findAll().size() == 1
            Colonna.findAll().size() == 4
            Card.findAll().size() == 1
            Card.findByTitolo("Editing Card") != null

            $("p.card-titolo" , 0 ).text() == "Editing Card"
            title == "Board: " + nomeBoard
    }

    void "Sposta Card"(){
        given: "Esistono due Card nelle colonne 'Ready' e 'Work in Progress'"
            $("a p.column-addcard" , 1).click()
            createCard("Editing Colonna" , "Pagina per la modifica delle colonne")
            $("a p.column-addcard" , 2).click()
            createCard("Creazione Card" , "Form di creazione delle card")

        when: "Cambio colonna alle card dalla loro pagina di editing"
            $("p.card-titolo" , 1).click()
            $("form")."colonnaCard" = "Done"
            $("input" , name: "_action_update").click()
            $("p.card-titolo" , 0).click()
            $("form")."colonnaCard" = "Work in Progress"
            $("input" , name: "_action_update").click()
        then: "Le card sono spostate nella casella successiva"
            Card.findAll().size() == 2
            Colonna.findByName("Done").cards.size() == 1
            Colonna.findByName("Work in Progress").cards.size() == 1
            title == "Board: " + nomeBoard
    }

    void "Assegna Card"(){
        given: "Esiste una card su 'Backlog' da assegnare a un utente"
            def titoloCard = "Editing Card"
            $("a p.column-addcard" , 0).click()
            createCard(titoloCard , "Pagina per la modifica delle Card")

        when: "Cambio colonna alle card dalla loro pagina di editing"
            $("p.card-titolo" , 0).click()
            $("form")."utentiBoard" = ["Davide"]
            $("input" , name: "_action_update").click()
        then: "Le card sono spostate nella casella successiva"
            Card.findAll().size() == 1
            Colonna.findByName("Backlog").cards.size() == 1
            Card.findByTitolo(titoloCard).users.size() == 1
            Card.findByTitolo(titoloCard).users.getAt(0).username == "Davide"
            title == "Board: " + nomeBoard
    }

}
