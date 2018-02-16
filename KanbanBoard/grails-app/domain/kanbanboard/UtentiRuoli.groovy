package kanbanboard

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class UtentiRuoli implements Serializable {

	private static final long serialVersionUID = 1

	Utenti utenti
	Ruoli ruoli

	@Override
	boolean equals(other) {
		if (other instanceof UtentiRuoli) {
			other.utentiId == utenti?.id && other.ruoliId == ruoli?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (utenti) {
            hashCode = HashCodeHelper.updateHash(hashCode, utenti.id)
		}
		if (ruoli) {
		    hashCode = HashCodeHelper.updateHash(hashCode, ruoli.id)
		}
		hashCode
	}

	static UtentiRuoli get(long utentiId, long ruoliId) {
		criteriaFor(utentiId, ruoliId).get()
	}

	static boolean exists(long utentiId, long ruoliId) {
		criteriaFor(utentiId, ruoliId).count()
	}

	private static DetachedCriteria criteriaFor(long utentiId, long ruoliId) {
		UtentiRuoli.where {
			utenti == Utenti.load(utentiId) &&
			ruoli == Ruoli.load(ruoliId)
		}
	}

	static UtentiRuoli create(Utenti utenti, Ruoli ruoli, boolean flush = false) {
		def instance = new UtentiRuoli(utenti: utenti, ruoli: ruoli)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(Utenti u, Ruoli r) {
		if (u != null && r != null) {
			UtentiRuoli.where { utenti == u && ruoli == r }.deleteAll()
		}
	}

	static int removeAll(Utenti u) {
		u == null ? 0 : UtentiRuoli.where { utenti == u }.deleteAll() as int
	}

	static int removeAll(Ruoli r) {
		r == null ? 0 : UtentiRuoli.where { ruoli == r }.deleteAll() as int
	}

	static constraints = {
	    utenti nullable: false
		ruoli nullable: false, validator: { Ruoli r, UtentiRuoli ur ->
			if (ur.utenti?.id) {
				if (UtentiRuoli.exists(ur.utenti.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['utenti', 'ruoli']
		version false
	}
}
