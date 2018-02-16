package kanbanboard

import grails.transaction.Transactional

@Transactional(readOnly = true)
class ColonnaController {

    def springSecurityService

    private lookupUser(){
        return (Utenti) Utenti.get(springSecurityService.currentUserId)
    }

    def create() {
        respond currentBoard: Board.findById(session.currentBoard)
    }

    def createColumn(){
        if (params.nomeColonna == null || params.nomeColonna.equals(" ") || params.nomeColonna.equals("Card non assegnate")) {
            if(params.nomeColonna.equals("Card non assegnate")){
                flash.message = "La colonna non può chiamarsi 'Card non assegnate' "
                redirect(controller: "Colonna" , action:"create")
            }
            else{
                flash.message = "Il campo 'Nome della colonna' non può essere vuoto"
                redirect(controller: "Colonna" , action:"create")
            }
        }
        else {
            def c = new Colonna(name: params.nomeColonna)
            Board.findById(session.currentBoard).addToColonne(c)
            Board.findById(session.currentBoard).save(flush: true)
            c.save(flush:true)

            redirect(controller: "Board" , action:"show" , id: Board.findById(session.currentBoard).id)
        }

    }

    @Transactional
    def update() {
        def idColonna = params.idColonna
        def idUser = lookupUser().id
        session.currentUser = lookupUser().id

        if (params.nomeColonna == null || params.nomeColonna.equals(" ") || params.nomeColonna.equals("Card non assegnate")) {
            if(params.nomeColonna.equals("Card non assegnate")){
                flash.message = "La colonna non può chiamarsi 'Card non assegnate' "
                redirect(controller: "Colonna" , action:"edit" , id: Colonna.findById(session.currentCol).id , params: [idCNA: params.idCNA])
            }
            else{
                flash.message = "Il campo 'Nome' non può essere vuoto"
            redirect(controller: "Colonna" , action:"edit" , id: Colonna.findById(session.currentCol).id , params: [idCNA: params.idCNA])
            }
        }
        else {
            Colonna.findById(idColonna).name = params.nomeColonna
            Colonna.findById(idColonna).save(flush:true)

            if(params.currentCards != null) {
                if(params.currentCards instanceof String) {
                    Utenti.findById(idUser).removeFromCards(Card.findById(params.currentCards))
                    Colonna.findById(idColonna).removeFromCards(Card.findById(params.currentCards))
                    Card.findById(params.currentCards).removeFromUsers(Utenti.findById(idUser))

                    Utenti.findById(idUser).save(flush: true)
                    Colonna.findById(idColonna).save(flush: true)
                    Card.findById(params.currentCards).delete(flush: true)
                } else {
                    (params.currentCards).each {
                        Utenti.findById(idUser).removeFromCards(Card.findById(it))
                        Colonna.findById(idColonna).removeFromCards(Card.findById(it))
                        Card.findById(it).removeFromUsers(Utenti.findById(idUser))

                        Utenti.findById(idUser).save(flush: true)
                        Colonna.findById(idColonna).save(flush: true)
                        Card.findById(it).delete(flush: true)
                    }
                }
            }

            if(params.CNACards != null) {
                if(params.CNACards instanceof String) {
                    Colonna.findById(params.idCNA).removeFromCards(Card.findById(params.CNACards))
                    Colonna.findById(idColonna).addToCards(Card.findById(params.CNACards))
                    Colonna.findById(params.idCNA).save(flush:true)
                    Colonna.findById(idColonna).save(flush:true)

                    if(Colonna.findById(params.idCNA).cards.size() == 0)
                    {
                        Board.findById(session.currentBoard).removeFromColonne(Colonna.findById(params.idCNA))
                        Board.findById(session.currentBoard).save(flush:true)
                        Colonna.findById(params.idCNA).delete(flush:true)
                    }
                } else {
                    (params.CNACards).each {
                        Colonna.findById(params.idCNA).removeFromCards(Card.findById(it))
                        Colonna.findById(idColonna).addToCards(Card.findById(it))
                        Colonna.findById(params.idCNA).save(flush:true)
                        Colonna.findById(idColonna).save(flush:true)
                        
                        if(Colonna.findById(params.idCNA).cards.size() == 0)
                        {
                            Board.findById(session.currentBoard).removeFromColonne(Colonna.findById(params.idCNA))
                            Board.findById(session.currentBoard).save(flush:true)
                            Colonna.findById(params.idCNA).delete(flush:true)
                        }
                    }
                }
            }

            redirect(controller: "Board" , action:"show" , id: Board.findById(session.currentBoard).id )
        }
    }

    def edit(Colonna c) {
        session.currentCol = c.id
        respond currentColumn: c, currentBoard: Board.findById(session.currentBoard),  colonnaCNA: Colonna.findById(params.idCNA)

    }

    def delete(Colonna colonna)
    {

        if (colonna == null) {
            colonna = Colonna.findById(session.currentCol)
        }

        def hasCardNonAssegnate = false
        def cardNonAssegnateID
        def cardsCount = 0
        Board.findById(session.currentBoard).colonne.each {
            if (it.name.equals("Card non assegnate")){
                hasCardNonAssegnate = true
                cardNonAssegnateID = it.id
            }
        }
        colonna.cards.each {cardsCount++}

        if (hasCardNonAssegnate == true){
            Colonna.findById(cardNonAssegnateID).cards.addAll(colonna.cards)
            Board.findById(session.currentBoard).removeFromColonne(colonna)
            colonna.cards.removeAll(colonna.cards)
            colonna.delete flush:true
        }
        else {
            if (cardsCount > 0){
                colonna.name = "Card non assegnate"
                colonna.save flush:true
            }
            else{
                Board.findById(session.currentBoard).removeFromColonne(colonna)
                colonna.delete flush:true
            }
        }

        redirect(controller: "Board" , action:"show" , id: Board.findById(session.currentBoard).id)
    }
}
