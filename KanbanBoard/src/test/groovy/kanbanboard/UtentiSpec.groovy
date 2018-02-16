package kanbanboard

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class UtentiSpec extends Specification implements DomainUnitTest<Utenti> {


    void "Creazione Utente"() {
        given:"Creo un utente"
        new Utenti(username: "utente", password: "1234").save(flush: true)

        expect:"L'utente esiste"
        Utenti.findAll().size()==1
    }

    void "Cambio nome utente"() {
        given:"Creo l'utente e cambio l'username"
        def user = new Utenti(username: "utente", password: "1234").save(flush: true)
        Utenti.findByUsername("utente").username = "utente2"

        expect:"Nome utente cambiato"
        Utenti.findById(user.id).username == "utente2"
    }

    void "Cambio password utente"() {
        given:"Creo l'utente e cambio la password"
        def user = new Utenti(username: "utente", password: "1234").save(flush: true)
        Utenti.findById(user.id).password = "5678"

        expect:"Password cambiata"
        Utenti.findById(user.id).password == "5678"
    }

    void "Creo utente che poi crea una board"() {
        given:"Creo l'utente, poi la board"
        def user = new Utenti(username: "utente", password: "1234").save(flush: true)
        def board = new Board(name: "Board" , admin: user.id).save(flush:true)

        expect:"L'admin della board è user"
        board.admin == user.id
    }

    void "Creo utente e gli assegno una card"() {
        given:"Creo l'utente, poi la board"
        def user = new Utenti(username: "utente", password: "1234").save(flush: true)
        def card = new Card(titolo: "card", descrizione: "descrizione", scadenza: new Date('10/10/2020')).save(flush: true)
        card.addToUsers(user).save(flush: true)

        expect:"La card è stata assegnata"
        Card.findById(card.id).users.size() == 1
    }

    void "Tentativo di registrare due utenti con lo stesso nome"(){
        given:"Creo due utenti con lo stesso nome"
        def user1 = new Utenti(username: "utente", password: "1234").save(flush: true)
        def user2 = new Utenti(username: "utente", password: "5678").save(flush: true)

        expect:"user2 non deve esistere"
        user2 == null
    }

    void "Ricerca tra utenti multiple"() {
        given:"Creo 4 utenti"
        new Utenti(username: "1", password: "1").save(flush:true)
        new Utenti(username: "2", password: "2").save(flush:true)
        new Utenti(username: "3", password: "3").save(flush:true)
        new Utenti(username: "4", password: "4").save(flush:true)

        expect:"La board viene trovata"
        Utenti.findByUsername("3") != null
    }
}