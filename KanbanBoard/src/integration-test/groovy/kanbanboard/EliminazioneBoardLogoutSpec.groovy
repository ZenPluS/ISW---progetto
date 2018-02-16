package kanbanboard

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import groovy.sql.Sql
import org.hibernate.SessionFactory

import javax.sql.DataSource

@Integration
@Rollback
class EliminazioneBoardLogoutSpec extends GebSpec {

    static transactional = false
    SessionFactory sessionFactory
    DataSource dataSource
    File schemaDump
    Sql sql

    def nomeBoard = "KanbanProject"
    def mainUser = "user"

    def setup() {
        if(dataSource != null) {
            sql = new Sql(dataSource)
            schemaDump = File.createTempFile("test-database-dump", ".sql")
            sql.execute("script drop to ${schemaDump.absolutePath}")
        }
        createUser(mainUser , "1234")
        login()
        createBoard(nomeBoard)
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

    def createUser(String username, String password){
        go '/'
        $("a#registra").click()
        $("input" , id:"username").value(username)
        $("input" , id:"password").value(password)
        $("input" , id:"utenteCreate").click()
    }
    void login(){
        go '/'
        $("a#login").click()
        $("input" , id:"username").value(mainUser)
        $("input" , id:"password").value("1234")
        $("input" , id:"submit").click()
    }

    void "Eliminazione Board"(){
        when: "Elimino la Board"
            $("a.adduser").click()
            $("a.deleteButtonBar").click()

        then: "Viene visualizzata la dashboard senza la Board eliminata"
            find("p.nomeBoard").isEmpty()
            title == "Dashboard"

    }

    void "Logout"(){
        when: "Premo sul bottone Logout"
            $("a#logout").click()

        then: "Vengo reindirizzato alla pagina di login"
            title == "Login"

    }
}
