package kanbanboard

import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

@Integration
class CardControllerSpec extends Specification implements ControllerUnitTest<CardController> {

    def lu = new ArrayList<Utenti>()
    def lc = new ArrayList<Card>()


    def populateValidParams(params) {
        assert params != null

        params["titolo"] = 'card'
        params["descrizione"] = 'descrzione'
        params["data"] = new Date( '10/10/2020')
    }

    def colonn(params){
        assert params != null

        params["id"] = '3'
        params["name"] = 'colonna'
    }

    def user(params){
        assert params != null

        params["username"] = 'username'
        params["password"] = 'password'
    }

    void "Show: creazione di una Card completa"(){
        when: "La creazione è stata eseguita"
        populateValidParams(params)
        def card = new Card(params)
        user(params)
        def user = new Utenti(params)
        lu.add(user)
        card.users = lu
        colonn(params)
        def col = new Colonna(params)
        lc.add(card)
        col.cards = lc

        then: "Il modello è correttamente creato"
        !model.card
    }

    void "Controllo creazione card su tutti i parametri"(){
        when: "La creazione è stata eseguita"
        populateValidParams(params)
        def card = new Card(params)
        user(params)
        def user = new Utenti(params)
        lu.add(user)
        card.users = lu
        colonn(params)
        def col = new Colonna(params)
        lc.add(card)
        col.cards = lc


        then: "La card è completa"
        card.titolo != null
        card.descrizione != null
        card.users.size() == 1
    }

    void "Test creazione nuova card vuota"() {
        when:"non faccio nulla"
        // Non faccio nulla

        then:"il modello non viene creato"
        model.card == null
    }

}