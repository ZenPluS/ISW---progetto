package kanbanboard

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class Utenti implements Serializable {

    private static final long serialVersionUID = 1
    static hasMany = [boards:Board, cards: Card]
    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Ruoli> getAuthorities() {
        (UtentiRuoli.findAllByUtenti(this) as List<UtentiRuoli>)*.ruoli as Set<Ruoli>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
	    password column: '`password`'
    }
}
