package kanbanboard

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CardSpec extends Specification implements DomainUnitTest<Card> {

    void "Creazione Card"() {
        given:"Creo la card"
        new Card(titolo: "card", descrizione: "descrizione", scadenza: new Date('10/10/2020')).save(flush: true)

        expect:"La card esiste"
        Card.findAll().size()==1
    }

    void "Rinomina titolo card"() {
        given:"Creo la card e la rinomino"
        def card = new Card(titolo: "card", descrizione: "descrizione", scadenza: new Date('10/10/2020')).save(flush: true)
        Card.findByTitolo("card").titolo = "card2"

        expect:"La colonna esiste con un altro nome "
        Card.findById(card.id).titolo == "card2"
    }

    void "Cambio descrizione card"() {
        given:"Creo la card e cambio la descrizione"
        def card = new Card(titolo: "card", descrizione: "descrizione", scadenza: new Date('10/10/2020')).save(flush: true)
        Card.findByDescrizione("descrizione").descrizione = "descrizione2"

        expect:"La colonna esiste con un altro nome "
        Card.findById(card.id).descrizione == "descrizione2"
    }

    void "Cambio la scadenza card"() {
        given:"Creo la card e cambio la scadenza"
        def card = new Card(titolo: "card", descrizione: "descrizione", scadenza: new Date('10/10/2020')).save(flush: true)
        Card.findById(card.id).scadenza = new Date('8/8/2020')

        expect:"La colonna esiste con un altro nome "
        Card.findById(card.id).scadenza == new Date('8/8/2020')
    }

    void "Ricerca per ID crad"() {
        given:"Creo la card e salvo l'id"
        def card = new Card(titolo: "card", descrizione: "descrizione", scadenza: new Date('10/10/2020')).save(flush: true)
        def id = card.id

        expect:"La colonna viene trovata"
        Card.findById(id).id == card.id
    }

    void "Creo board, colonna e card e li lego tramite le dipendenze"() {
        given:"Creo una colonna e la aggiungo alla board"
        def board = new Board(name: "Board" , admin: 1).save(flush:true)
        def colonna = new Colonna(name: "colonna").save(flush: true)
        def card = new Card(titolo: "card", descrizione: "descrzione", scadenza: new Date('10/10/2020')).save(flush: true)
        board.addToColonne(colonna).save(flush: true)
        colonna.addToCards(card).save(flush: true)
        def id = colonna.id
        def id2 = card.id

        expect:"Tutto viene legato per dipendenze"
        Board.findByAdmin(1).colonne.count(colonna) == 1
        Colonna.findById(id).cards.count(card) == 1
        Card.findById(id2).id == card.id
    }

    void "Ricerca tra colonne multiple"() {
        given:"Creo 4 colonne"
        new Colonna(name: "c1").save(flush: true)
        new Colonna(name: "c2").save(flush: true)
        new Colonna(name: "c3").save(flush: true)
        new Colonna(name: "c4").save(flush: true)

        expect:"La colonna viene trovata"
        Colonna.findByName("c3") != null
    }
}
