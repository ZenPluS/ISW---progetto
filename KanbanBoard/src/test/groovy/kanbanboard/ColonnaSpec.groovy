package kanbanboard

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ColonnaSpec extends Specification implements DomainUnitTest<Colonna> {

    void "Creazione Colonna"() {
        given:"Creo colonna"
        new Colonna(name: "colonna").save(flush:true)

        expect:"La board esiste"
        Colonna.findAll().size()==1
    }

    void "Rinomina colonna"() {
        given:"Creo la colonna e rinomino"
        def col = new Colonna(name: "colonna").save(flush:true)
        Colonna.findByName("colonna").name = "colonna2"

        expect:"La colonna esiste con un altro nome "
        Colonna.findById(col.id).name == "colonna2"
    }

    void "Ricerca per ID colonna"() {
        given:"Creo la colonna e salvo l'id"
        def col = new Colonna(name: "colonna").save(flush:true)
        def id = col.id

        expect:"La colonna viene trovata"
        Colonna.findById(id).id == col.id
    }

    void "Inserimento più colonne in una board"() {
        given: "Creo la colonna e salvo l'id"
        def board = new Board(name: "Board", admin: 1).save(flush: true)
        def c1 = new Colonna(name: "c1").save(flush: true)
        def c2 = new Colonna(name: "c2").save(flush: true)
        def c3 = new Colonna(name: "c3").save(flush: true)
        board.addToColonne(c1).save(flush: true)
        board.addToColonne(c2).save(flush: true)
        board.addToColonne(c3).save(flush: true)

        expect: "Le colonne sono state inserite tutte"
        board.colonne.size() == 3
    }

    void "Creo board, colonna e card e li lego tramite le dipendenze"() {
        given:"Creo una colonna e la aggiungo alla board"
        def board = new Board(name: "Board" , admin: 1).save(flush:true)
        def colonna = new Colonna(name: "colonna").save(flush: true)
        def card = new Card(titolo: "card", descrizione: "descrzione", scadenza: new Date('10/10/2020')).save(flush: true)
        board.addToColonne(colonna).save(flush: true)
        colonna.addToCards(card).save(flush: true)
        def id = colonna.id

        expect:"Tutto viene legato per dipendenze"
        Board.findByAdmin(1).colonne.count(colonna) == 1
        Colonna.findById(id).cards.count(card) == 1
    }

    void "Trovare colonne all'interno di una board"() {
        given:"Creo una colonna e la aggiungo alla board"
        def b1 = new Board(name: "b1" , admin: 1).save(flush:true)
        def b2 = new Board(name: "b2" , admin: 1).save(flush:true)
        def c1 = new Colonna(name: "c1").save(flush: true)
        def c2 = new Colonna(name: "c2").save(flush: true)
        b1.addToColonne(c1).save(flush: true)
        b2.addToColonne(c2).save(flush: true)

        expect:"le colonne vengono trovate dentro la board a cui sono state assegnate"
        Board.findByName(b1.name).getColonne().name.toString() == "[" + Colonna.findById(c1.id).getName().toString() + "]"
        Board.findByName(b2.name).getColonne().name.toString() == "[" + Colonna.findById(c2.id).getName().toString() + "]"
    }

    void "Trovare card all'interno di una colonna"() {
        given:"Creo una colonna e la aggiungo alla board"
        def b1 = new Board(name: "b1" , admin: 1).save(flush:true)
        def c1 = new Colonna(name: "c1").save(flush: true)
        def c2 = new Colonna(name: "c2").save(flush: true)
        def card = new Card(titolo: "card", descrizione: "descrzione", scadenza: new Date('10/10/2020')).save(flush: true)
        c1.addToCards(card).save(flush: true)
        b1.addToColonne(c1).save(flush: true)
        b1.addToColonne(c2).save(flush: true)

        expect:"le colonne vengono trovate dentro la board a cui sono state assegnate"
        Colonna.findByName(c1.name).getCards().titolo.toString() == "[" + Card.findById(card.id).getTitolo().toString() + "]"
    }
}
