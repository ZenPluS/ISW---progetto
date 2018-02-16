package kanbanboard

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UtentiController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def show() {
        render(view:'/login/auth')
    }

    def create() {
        respond new Utenti(params)
    }

    @Transactional
    def save(Utenti utenti) {
        if (utenti == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (utenti.hasErrors()) {
            transactionStatus.setRollbackOnly()
            if(Utenti.findByUsername(params.username) != null && Utenti.findByUsername(params.username).count() > 0 || params.username == " " || params.username == "") {
                if (params.username == " " || params.username == ""){
                    flash.message = "Credenziali non valide"
                }
                else{
                    flash.message = "Questo username esiste già"
                }
            }
            respond utenti.errors, view:'create'
            return
        }
        utenti.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'utenti.label', default: 'Utenti'), utenti.id])
                redirect utenti
            }
            '*' { respond utenti, [status: CREATED] }
        }
    }
}
