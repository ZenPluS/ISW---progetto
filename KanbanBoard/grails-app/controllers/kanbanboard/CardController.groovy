package kanbanboard

import grails.transaction.Transactional

@Transactional(readOnly = true)
class CardController {

    def create(){
        if (params.columnId != null) {
            session.currentCol = params.columnId
        }
        respond  currentColumn: Colonna.findById(session.currentCol) , currentBoard: Board.findById(session.currentBoard)
    }

    def createCard(){
        if(params.nomeCard.equals(" ") || params.descrizione.equals(" "))
        {
            flash.message = "I campi non possono essere vuoti"
            redirect(controller: "Card" , action:"create")
        } else {
            def card = new Card(titolo: params.nomeCard, descrizione: params.descrizione, scadenza: params.scadenza)
            Colonna.findById(session.currentCol).addToCards(card)
            Colonna.findById(session.currentCol).save(flush: true)
            card.save(flush: true)
            redirect(controller: "Board", action: "show", id: Board.findById(session.currentBoard).id)
        }
    }

    def update() {
        def idCard = params.idCard
        def idColonna = session.currentCol

        if (params.titolo.equals(" ") || params.titolo == null || params.descrizione.equals(" ") || params.descrizione == null) {
            flash.message = "I campi non possono essere vuoti"
            render(view: "edit", model: [currentCard: Card.findById(idCard) , currentColumn: Colonna.findById(idColonna) , currentBoard: Board.findById(session.currentBoard)])
        }
        else {
            Card.findById(idCard).titolo = params.titolo
            Card.findById(idCard).descrizione = params.descrizione
            Card.findById(idCard).scadenza = params.scadenza

            if (params.utentiBoard!=null) {
                (params.utentiBoard).each {
                    Card.findById(idCard).addToUsers(Utenti.findById(it))
                    Utenti.findById(it).addToCards(Card.findById(idCard))
                    Utenti.findById(it).save(flush: true)
                }
            }

            if (params.utentiCard!=null) {
                (params.utentiCard).each {
                    Card.findById(idCard).removeFromUsers(Utenti.findById(it))
                    Utenti.findById(it).removeFromCards(Card.findById(idCard))
                    Utenti.findById(it).save(flush: true)
                }
            }

            if (params.colonnaCard!=null && params.colonnaCard != ('null')) {
                Colonna.findById(session.currentCol).removeFromCards(Card.findById(idCard))
                Colonna.findById(params.colonnaCard).addToCards(Card.findById(idCard))
            }

            if(idColonna != null && Colonna.findById(idColonna).cards.size() == 0 && Colonna.findById(idColonna).name.equals("Card non assegnate") )
            {
                Board.findById(session.currentBoard).removeFromColonne(Colonna.findById(idColonna))
                Board.findById(session.currentBoard).save(flush:true)
                Colonna.findById(idColonna).delete(flush:true)
            }
            Card.findById(idCard).save(flush:true)

            redirect(controller: "Board" , action:"show" , id: Board.findById(session.currentBoard).id )
        }
    }

    def edit(Card card){
        session.currentCard=card.id
        session.currentCol = params.currentCol
        respond currentCard: card , currentBoard: Board.findById(session.currentBoard) , currentColumn:  Colonna.findById(session.currentCol)
    }

    def delete(){
        def idColonna = params.currentCol
        def idCard = params.currentCard
        def idCNA
        if (params.idCNA != null){idCNA = params.idCNA}

        Card.findById(idCard).users.collect().each {
            Card.findById(idCard).removeFromUsers(it)
        }
        Card.findById(idCard).save(flush:true)
        Colonna.findById(idColonna).removeFromCards(Card.findById(idCard))
        Colonna.findById(idColonna).save(flush:true)
        Card.findById(idCard).delete(flush:true)

        if(idCNA != null && Colonna.findById(idCNA).cards.size() == 0 && Colonna.findById(idColonna).name.equals("Card non assegnate") )
        {
            Board.findById(session.currentBoard).removeFromColonne(Colonna.findById(idCNA))
            Board.findById(session.currentBoard).save(flush:true)
            Colonna.findById(idCNA).delete(flush:true)
        }

        redirect(controller: "Board" , action:"show" , id: Board.findById(session.currentBoard).id )
    }

}
