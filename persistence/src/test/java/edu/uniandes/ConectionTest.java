package edu.uniandes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import static javax.jdo.Query.SQL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConectionTest {
    private PersistenceManagerFactory emf;

    @AfterEach void tearDown() {
        if (emf != null && !emf.isClosed()) emf.close();
    }

    @Test void testConnectionOK() {
        assertDoesNotThrow(() -> emf = JDOHelper.getPersistenceManagerFactory("Parranderos"),
                           "Revisar el persistence.xml. No deberia haber fallado");
    }

    @Test void testConnectionInaccessible() {
        assertThrows(Exception.class, () -> emf = JDOHelper.getPersistenceManagerFactory("ParranderosErrorDS"),
                     "Revisar el persistence.xml. Deberia haber fallado por la base de datos invalida");
    }

    @Test void testInvalidUser() {
        assertThrows(Exception.class, () -> emf = JDOHelper.getPersistenceManagerFactory("ParranderosErrorUser"),
                     "Revisar el persistence.xml. Deberia haber fallado por las credenciales invalidas");
    }

    @Test void testInvalidTables() {
        assertThrows(Exception.class, () -> {
            emf = JDOHelper.getPersistenceManagerFactory("Parranderos");
            emf.getPersistenceManager().newQuery(SQL, "SELECT * FROM tipobebidatest").execute();
        }, "Deberia fallar. La tabla consultada no existe en la BD");
    }

    @Test void testInvalidColumns() {
        assertThrows(Exception.class, () -> {
            emf = JDOHelper.getPersistenceManagerFactory("Parranderos");
            emf.getPersistenceManager().newQuery(SQL, "SELECT * FROM tipobebida WHERE idtest = 1").execute();
        }, "Deberia fallar. La columna consultada no existe en la BD");
    }

}
