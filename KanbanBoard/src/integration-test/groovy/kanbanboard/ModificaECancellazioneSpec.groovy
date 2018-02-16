package kanbanboard

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import groovy.sql.Sql
import org.hibernate.SessionFactory
import javax.sql.DataSource

@Integration
@Rollback
class ModificaECancellazioneSpec extends GebSpec {


    static transactional = false
    SessionFactory sessionFactory
    DataSource dataSource
    File schemaDump
    Sql sql

    def nomeBoard = "OldProject"
    def mainUser = "user"

    def setup() {
        if (dataSource != null) {
            sql = new Sql(dataSource)
            schemaDump = File.createTempFile("test-database-dump", ".sql")
            sql.execute("script drop to ${schemaDump.absolutePath}")
        }

        createUser(mainUser, "1234")
        login()
        createBoard(nomeBoard)
        createColumn("Ideas")
        createColumn("Baclog")
        createColumn("Ready")
        createColumn("Work in Progress")
        createColumn("Testing")
        createColumn("Done")
        $("a p.column-addcard", 0).click()
        createCard("Card1", "descrizione")
        $("a p.column-addcard", 0).click()
        createCard("Card2", "descrizione")
        $("a p.column-addcard", 4).click()
        createCard("Card3", "descrizione")
    }

    def cleanup() {
        if (dataSource != null) {
            sql.execute("runscript from ${schemaDump.absolutePath}")
            sessionFactory.currentSession.clear()
            schemaDump.delete()
        }
    }

    def createBoard(String nomeBoard) {
        $("a.create").click()
        $("input#nomeBoard").value(nomeBoard)
        $("input#createBoard").click()
    }

    def createColumn(String nomeColonna) {
        $("a.create").click()
        $("input#nomeColonna").value(nomeColonna)
        $("input", name: "_action_createColumn").click()
    }

    def createUser(String username, String password) {
        go '/'
        $("a#registra").click()
        $("input", id: "username").value(username)
        $("input", id: "password").value(password)
        $("input", id: "utenteCreate").click()
    }

    def createCard(String titolo, String descrizione) {
        $("input", id: "nomeCard").value(titolo)
        $("textarea", id: "descrizione").value(descrizione)
        $("form")."scadenza_day" = 1
        $("form")."scadenza_month" = 1
        $("form")."scadenza_year" = "2017"
        $("input", name: "_action_createCard").click()
    }

    void editBoardElements(){
        $("p.column-title", 1).click()
        $("input#nomeColonna").value("Backlog")
        $("input", name: "_action_update").click()
        $("input.deleteColumn", 0).click()
        $("input.deleteColumn", 3).click()
    }

    void login() {
        go '/'
        $("a#login").click()
        $("input", id: "username").value(mainUser)
        $("input", id: "password").value("1234")
        $("input", id: "submit").click()
    }

    void "Stato di inizio"() {
        expect: "Board con 6 colonne e 3 card"
        Board.findByName(nomeBoard) != null
        Colonna.findAll().size() == 6
        Colonna.findByName("Ideas").cards.size() == 2
        Colonna.findByName("Testing").cards.size() == 1
        Card.findAll().size() == 3
        $("p.column-title", 0).text() == "Ideas"
        $("p.column-title", 1).text() == "Baclog"
        $("p.column-title", 2).text() == "Ready"
        $("p.column-title", 3).text() == "Work in Progress"
        $("p.column-title", 4).text() == "Testing"
        $("p.column-title", 5).text() == "Done"
        title == "Board: " + nomeBoard
    }

    void "Rinomina Board"(){
        when: "Cambi il nome alla board"
        $("a.adduser").click()
        $("input#nome").value("KanbanProject")
        $("input.form-submit").click()

        then: "La board ha il nome aggiornato"
        title == "Board: KanbanProject"
    }

    void "Rinomina ed elimina colonne"() {

        when: "Rinomini la colonna Baclog in Backlog ed elimini Ideas (dalla edit) e Testing (dalla dashboard)"
            $("p.column-title", 1).click()
            $("input#nomeColonna").value("Backlog")
            $("input", name: "_action_update").click()

            $("p.column-title", 0).click()
            $("a.deleteButtonBar").click()

            $("input.deleteColumn", 3).click()

        then: "La Board contiene 5 colonne e una si chiama 'Backlog'"
            title == "Board: " + nomeBoard
            Colonna.findByName("Ideas") == null
            Colonna.findByName("Testing") == null
            Colonna.findByName("Backlog") != null
            Colonna.findAll().size() == 5
    }

    void "Riassegna e cancella Card"(){
        given: "Il progetto non ha le colonne Ideas e Testing, ha Backlog"
            editBoardElements()

        when: "Elimini Card1 (da editing) e Card2 (da DashBoard) e sposti Card3 da 'Card non Assegnate' a 'Backlog'"
            $("div.card-container" , 0).click()
            $("a.deleteButtonBar").click()

            $("input.deleteCard" , 0).click()

            $("div.card-container" , 0).click()
            $("form")."colonnaCard" = "Backlog"
            $("input", name: "_action_update").click()

        then: "La colonna Card non Assegnate non esiste più e Backlog ha una card"
            title == "Board: " + nomeBoard
            Colonna.findByName("Backlog").cards.size()==1
            Colonna.findByName("Card non assegnate") == null
            Colonna.findAll().size() == 4
            Card.findAll().size() == 1
            Card.findByTitolo("Card3") != null
    }
}