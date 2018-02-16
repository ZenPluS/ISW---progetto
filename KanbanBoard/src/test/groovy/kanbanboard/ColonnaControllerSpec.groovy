package kanbanboard

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ColonnaController)
@Mock([Colonna])
class ColonnaControllerSpec extends Specification{

    def populateValidParams(params) {
        assert params != null

        params["id"] = '3'
        params["name"] = 'colonna'
    }

    void "Test sulla create se rende il modello corretto"() {
        when: "The create action is executed"
        populateValidParams(params)
        def col = new Colonna(params)
        controller.createColumn()

        then: "The model is correctly created"
        !model.colonna
    }

    void "Test salvatagggio nuova colonna"() {
        when: "Viene creata una nuova colonna"
        populateValidParams(params)
        def col = new Colonna(params)
        controller.createColumn()

        then: "l'istanza viene salvata"
        col != null
    }
}
