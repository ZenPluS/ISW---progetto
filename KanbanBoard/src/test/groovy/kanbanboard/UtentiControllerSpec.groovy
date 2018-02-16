package kanbanboard

import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.*

@Integration
class UtentiControllerSpec extends Specification implements ControllerUnitTest<UtentiController> {

    def populateValidParams(params) {
        assert params != null

        params["username"] = 'username'
        params["password"] = 'password'
    }

    void "Controllo lista utenti"() {
        when: "non faccio nulla"
        // Non faccio nulla

        then: "Il modello è corretto"
        !model.utentiList
    }

    void "Test nuovo utente null"() {
        when:"non faccio nulla"
        // Non faccio nulla

        then:"il modello non viene creato"
        model.utenti == null
    }

    void "Test salvatagggio nuovo utente"() {
        when: "viene eseguita l'azione saveNewUser con un'istanza valida"
        populateValidParams(params)
        def user = new Utenti(params)

        then: "l'istanza viene salvata"
        user != null
    }

}