package kanbanboard

class Board {

    String name
    Long admin

    static hasMany = [users:Utenti, colonne: Colonna]
    static belongsTo = [Utenti]

}
