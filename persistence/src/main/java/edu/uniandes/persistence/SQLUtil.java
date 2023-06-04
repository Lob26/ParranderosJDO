package edu.uniandes.persistence;

import edu.uniandes.___data.SeqUtils;
import javax.jdo.PersistenceManager;

import static javax.jdo.Query.SQL;

class SQLUtil {
    long nextByClassName(PersistenceManager pm, String classWithSequence) {
        return (long) pm.newQuery(SQL, "SELECT " + SeqUtils.sequence(classWithSequence) + ".nextval FROM dual").executeUnique();
    }

    long nextBySequenceName(PersistenceManager pm, String sequenceName) {
        return (long) pm.newQuery(SQL, "SELECT " + sequenceName + ".nextval FROM dual").executeUnique();
    }
}
