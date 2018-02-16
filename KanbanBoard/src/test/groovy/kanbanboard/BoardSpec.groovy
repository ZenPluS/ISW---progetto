package kanbanboard

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BoardSpec extends Specification implements DomainUnitTest<Board> {


    void "Creazione Board"() {
        given:"Creo board"
        new Board(name: "Board" , admin: 1).save(flush:true)

        expect:"La board esiste"
            Board.findAll().size()==1
    }

    void "Rinomina Board"() {
        given:"Creo board e rinomino"
        new Board(name: "Board" , admin: 1).save(flush:true)
        Board.findByName("Board").name = "Board2"

        expect:"La board esiste con un altro nome "
        Board.findByAdmin(1).name == "Board2"
    }

    void "Ricerca per Admin"() {
        given:"Creo le board"
        new Board(name: "B1" , admin: 1).save(flush:true)
        new Board(name: "B2" , admin: 3).save(flush:true)

        expect:"La board viene trovata"
        Board.findByAdmin(1).admin == 1
    }

    void "Creo la board e aggiungo una colonna"() {
        given:"Creo una colonna e la aggiungo alla board"
        def board = new Board(name: "Board" , admin: 1).save(flush:true)
        def colonna = new Colonna(name: "colonna").save(flush: true)
        board.addToColonne(colonna).save(flush: true)


        expect:"La colonna viene trovata"
        Board.findByAdmin(1).colonne.count(colonna) == 1
    }

    void "Creo la board e aggiungo due utenti"() {
        given:"Creo la board, creo due utenti e li aggiungo alla board"
        def board = new Board(name: "Board" , admin: 1).save(flush:true)
        def u1 = new Utenti(username: "u1", password: "u1").save(flush: true)
        def u2 = new Utenti(username: "u2", password: "u2").save(flush: true)
        board.addToUsers(u1)
        board.addToUsers(u2)

        expect:"Gli utenti sono presenti"
        Board.findByAdmin(1).users.size() == 2
    }

    void "Ricerca tra board multiple"() {
        given:"Creo 4 board"
        new Board(name: "Board 1" , admin: 1).save(flush:true)
        new Board(name: "Board 2" , admin: 11).save(flush:true)
        new Board(name: "Board 3" , admin: 21).save(flush:true)
        new Board(name: "Board 4" , admin: 31).save(flush:true)

        expect:"La board viene trovata"
        Board.findByName("Board 4") != null
    }

    void "Numero di board di un utente"() {
        given:"Creo 4 board"
        new Board(name: "Board 1" , admin: 1).save(flush:true)
        new Board(name: "Board 2" , admin: 1).save(flush:true)
        new Board(name: "Board 3" , admin: 1).save(flush:true)
        new Board(name: "Board 4" , admin: 1).save(flush:true)

        expect:"L'utente è admin di 4 board"
        Board.findByAdmin(1).count() == 4
    }
}
