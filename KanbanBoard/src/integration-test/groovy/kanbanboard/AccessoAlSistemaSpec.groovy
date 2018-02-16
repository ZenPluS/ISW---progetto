package kanbanboard

import grails.testing.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*
import geb.spock.*

@Integration
@Rollback
class AccessoAlSistemaSpec extends GebSpec {


    def setup() {
    }


    def cleanup() {
        Utenti.deleteAll()
    }

    void "Visualizzazione della schermata Home"() {
        when:"Visiti la pagina principale"
            go '/'

        then:"Il titolo è corretto"
        	title == "Benvenuti nella KanbanBoard"
    }

    void "Registrazione"(){
        given: "Raggiungo la pagina di registrazione"
            go '/'
            $("a#registra").click()

        when: "L'utente immette credenziali non valide"
            $("input" , id:"username").value(" ")
            $("input" , id:"password").value("1234")
            $("input" , id:"utenteCreate").click()
        then: "Si mostra un messaggio di errore sulla stessa pagina"
            $("p.inputError").text() == "Credenziali non valide"
            Utenti.findAll().size() == 0
            title == "Registrazione"


        when: "L'utente si registra con credenziali corrette"
            $("input" , id:"username").value("user")
            $("input" , id:"password").value("1234")
            $("input" , id:"utenteCreate").click()
        then: "L'utente viene aggiunto al database e reindirizzato alla pagina di Login"
            Utenti.findAll().size() == 1
            title == "Login"
    }

    void "Login"(){
        given: "Creo un utente tramite registrazione e vengo rendirizzato a Login"
            go '/'
            $("a#registra").click()
            $("input" , id:"username").value("user")
            $("input" , id:"password").value("1234")
            $("input" , id:"utenteCreate").click()

            $("a#login").click()

        when: "Inserisco delle credenziali sbagliate"
            $("input" , id:"username").value("us")
            $("input" , id:"password").value("1234")
            $("input" , id:"submit").click()
        then: "Visualizza un messaggio di errore e ricarica la pagina"
            $("div.login_message").text() != null
            title == "Login"

        when: "Inserisco le giuste credenziali"
            $("input" , id:"username").value("user")
            $("input" , id:"password").value("1234")
            $("input" , id:"submit").click()
        then: "Reindirizza alla propria dashboard"
            Utenti.findAll().size() == 1
            Utenti.findByUsername("user").username == "user"
            $("div.login_message").text() == null
            title == "Dashboard"
    }
}
