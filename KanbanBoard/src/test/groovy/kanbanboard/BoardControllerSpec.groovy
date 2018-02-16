package kanbanboard

import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.*

@Integration
class BoardControllerSpec extends Specification implements ControllerUnitTest<BoardController> {

    def card(params){
        assert params != null

        params["titolo"] = 'card'
        params["descrizione"] = 'descrzione'
        params["data"] = '10/10/2020'
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

    def board(params){
        assert params != null

        params["name"] = 'board'
        params["admin"] = ''
    }

    void "Show: creazione di una Board completa"(){
        when: "La creazione è stata eseguita"
        board(params)
        def b = new Board(params)
        user(params)
        def u = new Utenti(params)
        b.admin = u.id
        controller.show(b)

        then:"Il modello è correttamente creato"
        model.board != null
    }

    void "Edit: la modifica di una Board"(){
        when: "La Edit è stata eseguita"
        board(params)
        def b = new Board(params)
        user(params)
        def u = new Utenti(params)
        b.admin = u.id
        b.addToUsers(u)
        controller.edit()

        then:"Il modello è correttamente visualizzato"
        !model.board
    }

    void "Aggiorna: test creazione modello corretto"(){
        when:"Invocazione del metodo aggiorna"
        board(params)
        def b = new Board(params)
        user(params)
        def u = new Utenti(params)
        b.admin = u.id
        controller.show(b)
        controller.aggiorna()

        then:"Il modello è correttamente visualizzato"
        model.board != null
    }
}
