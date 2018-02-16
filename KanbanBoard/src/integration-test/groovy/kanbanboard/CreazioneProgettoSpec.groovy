package kanbanboard

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import groovy.sql.Sql
import org.hibernate.SessionFactory

import javax.sql.DataSource

@Integration
@Rollback
class CreazioneProgettoSpec extends GebSpec {

    static transactional = false
    SessionFactory sessionFactory
    DataSource dataSource
    File schemaDump
    Sql sql

    def nomeBoard = "DemoProject"
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
        login(mainUser)
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

    void login(String nomeUtente){
        go '/'
        $("a#login").click()
        $("input" , id:"username").value(nomeUtente)
        $("input" , id:"password").value("1234")
        $("input" , id:"submit").click()
    }

    void "Visualizzazione DashBoard"() {
        expect:"Dopo il login si viene reindirizzati alla dashboard"
        title == "Dashboard"
    }

    void "Creazione Board"(){
        when: "Premo su 'Nuova Board' e inserisco un nome non valido"
            $("a.create").click()
            $("input#nomeBoard").value(" ")
            $("input#createBoard").click()
        then: "Viene mostrato un messaggio di errore"
            $("p.inputError").text() != null
            Board.findByName(" ") == null
            title == "Crea nuova Board"

        when: "Premo su 'Nuova Board' e inserisco un nome valido"
            $("a.create").click()
            $("input#nomeBoard").value(nomeBoard)
            $("input#createBoard").click()
        then: "Viene mostrata la Board"
            Board.findAll().size() == 1
            title == "Board: " + Board.findByName(nomeBoard).name
    }

    void "Creazione colonne"(){
        given: "La board è stata creata"
            createBoard(nomeBoard)

        when: "L'utente crea una colonna con un nome non valido"
            createColumn(" ")
        then: "Viene mostrato un messaggio di errore"
            $("p.inputError").text() == "Il campo 'Nome della colonna' non può essere vuoto"
            Colonna.findAll().size() == 0
            title == "Crea colonna"

        when: "L'utente crea quattro colonne"
            createColumn("Backlog")
            createColumn("Ready")
            createColumn("Work in Progress")
            createColumn("Done")
        then: "Le colonne vengono mostrate nella home della Board"
            Colonna.findAll().size() == 4
            $("p.column-title" , 0).text() == "Backlog"
            $("p.column-title" , 1).text() == "Ready"
            $("p.column-title" , 2).text() == "Work in Progress"
            $("p.column-title" , 3).text() == "Done"
            title == "Board: " + nomeBoard
    }

    void "Aggiungi Collaboratori"(){
        given: "Esiste la Board e due collaboratori"
            createBoard(nomeBoard)

        when: "Aggiungo collaboratori da 'Gestisci'"
            $("a.adduser").click()
            $("form")."utenteSelezionato" = [username1 , username2]
            $("input" , name: "_action_aggiorna").click()
        then: "La board sarà condivisa tra il creatore e gli utenti aggiunti"
            title == "Board: " + nomeBoard
            Utenti.findAll().size() == 3
            Board.findByName(nomeBoard).users.size() == 3
    }

}
