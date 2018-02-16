package kanbanboard

class Colonna {

    String name
    static hasMany = [cards:Card]
    static belongsTo = [Board]

}
