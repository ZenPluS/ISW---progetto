package kanbanboard

class Card {

    String titolo
    String descrizione
    Date scadenza

    static hasMany = [users:Utenti]
    static belongsTo = [Colonna,Utenti]


}
