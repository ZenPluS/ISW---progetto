package kanbanboard

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BoardController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def dashboard() {
        render(view: "dashboard",  model: [boards:lookupUser().boards])
    }

    def springSecurityService

    private lookupUser(){
        return (Utenti) Utenti.get(springSecurityService.currentUserId)
    }

    def show(Board b){
        session.currentBoard = b.id
        respond b
    }

    def form() {
    }

    def create() {
        if (params.nomeBoard.equals(" ") || params.nomeBoard == null)
        {
            flash.message = "Il nome della board non può essere vuoto"
            redirect(controller: "Board" , action:"form")
        }
        else
        {
            def b = new Board(name: params.nomeBoard)
            b.setAdmin(lookupUser().id)
            b.addToUsers(lookupUser())
            b.save()
            lookupUser().save flush:true

            redirect(controller: "Board" , action:"show" , id: b.id)
        }
    }

    def aggiorna() {

        def idBoard = params.idBoard

        if (params.nome == null || params.nome.equals(" ")) {
            flash.message = "Il campo 'nome' non può essere vuoto"
            redirect(controller: "Board" , action: "edit" , id: idBoard)
        } else {

            Board.findById(idBoard).name = params.nome
            Board.findById(idBoard).save(flush:true)

            if (params.utenteSelezionato != null) {
                (params.utenteSelezionato).each {
                    Board.findById(idBoard).addToUsers(Utenti.findById(it))
                    Utenti.findById(it).addToBoards(Board.findById(idBoard))
                    Board.findById(idBoard).save(flush: true)
                    Utenti.findById(it).save(flush: true)
                }
            }
            if (params.utenteSelezionatoRimuovi != null) {
                (params.utenteSelezionatoRimuovi).each {
                    Board.findById(idBoard).removeFromUsers(Utenti.findById(it))
                    Utenti.findById(it).removeFromBoards(Board.findById(idBoard))
                    Board.findById(idBoard).save(flush: true)
                    Utenti.findById(it).save(flush: true)
                }
            }
            redirect(controller: "Board", action: "show", id: idBoard)
        }
    }

    def edit() {
        if(params.boardID != null){
        session.currentBoard = params.boardID}
        respond currentBoard: Board.findById(session.currentBoard)
    }

    @Transactional
    def deleteBoard(Board board)
    {
        def idBoard = session.currentBoard

        Board.findById(idBoard).users.collect().each {
            Board.findById(idBoard).removeFromUsers(it)
        }
        Utenti.findById(lookupUser().id).save(flush:true)
        Board.findById(idBoard).delete(flush: true)

        redirect(controller: "Board" , action:"dashboard")


    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'board.label', default: 'Board'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}