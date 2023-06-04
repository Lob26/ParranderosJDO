package edu.uniandes.persistence;

import edu.uniandes.data.*;
import edu.uniandes.util.Tabulable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jdo.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ParranderosP {
    private final static Logger LOG = LogManager.getLogger(ParranderosP.class);
    private static final String SI = "Si";
    private static ParranderosP INSTANCE;
    private final PersistenceManagerFactory pmf;

    private final SQLHandler<Bar> sqlBar;
    private final SQLHandler<Bebedor> sqlBebedor;
    private final SQLHandler<Bebida> sqlBebida;
    private final SQLHandler<Gustan> sqlGustan;
    private final SQLHandler<Sirven> sqlSirven;
    private final SQLHandler<TipoBebida> sqlTipoBebida;
    private final SQLHandler<Visitan> sqlVisitan;

    private final SQLUtil sqlUtil;
    private final SQLReq sqlReq;

    static {
        getInstance(); //Usado para generar la instancia
    }

    {
        sqlBar = new SQLHandler<>(Bar.class);
        sqlBebedor = new SQLHandler<>(Bebedor.class);
        sqlBebida = new SQLHandler<>(Bebida.class);
        sqlGustan = new SQLHandler<>(Gustan.class);
        sqlSirven = new SQLHandler<>(Sirven.class);
        sqlTipoBebida = new SQLHandler<>(TipoBebida.class);
        sqlVisitan = new SQLHandler<>(Visitan.class);

        sqlUtil = new SQLUtil();
        sqlReq = new SQLReq();
    }

    private ParranderosP() {
        this.pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");
    }

    private ParranderosP(String unit) {this.pmf = JDOHelper.getPersistenceManagerFactory(unit);}

    public static ParranderosP getInstance(String unit) {
        if (INSTANCE == null) INSTANCE = new ParranderosP(unit);
        return INSTANCE;
    }

    public static ParranderosP getInstance() {
        if (INSTANCE == null) INSTANCE = new ParranderosP();
        return INSTANCE;
    }

    public void close() {pmf.close();}

    /**
     * Generates an error detail message from the given exception.
     *
     * @param e the exception to generate the error detail message from.
     * @return a String containing the error detail message.
     */
    private String generateErrorDetail(Exception e) {
        return e instanceof JDODataStoreException je ? je.getNestedExceptions()[0].getMessage() : "";
    }

    /*
     *   .oooooo.                                    .
     *  d8P'  `Y8b                                 .o8
     * 888          oooo d8b  .ooooo.   .oooo.   .o888oo  .ooooo.
     * 888          `888""8P d88' `88b `P  )88b    888   d88' `88b
     * 888           888     888ooo888  .oP"888    888   888ooo888
     * `88b    ooo   888     888    .o d8(  888    888 . 888    .o
     *  `Y8bood8P'  d888b    `Y8bod8P' `Y888""8o   "888" `Y8bod8P'
     *
     * Seccion dedicadada a la creacion de tuplas
     */
    public Object[][] createBar(Object[] bar) {
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long id = sqlUtil.nextBySequenceName(pmf.getPersistenceManager(), "parranderos_sequence");
                private final String nombre = bar[0].toString();
                private final String ciudad = bar[1].toString();
                private final String presupuesto = bar[2].toString();
                private final int cantSedes = Integer.parseInt(bar[3].toString());
            };
            LOG.trace("INSERT ⟼ Bar::{} ⟼ {} added", fields.id,
                      sqlBar.create(pm, fields.id, fields.nombre, fields.ciudad, fields.presupuesto, fields.cantSedes));
            return new Bar(fields.id, fields.nombre, fields.ciudad, fields.presupuesto, fields.cantSedes).toTable();
        });
    }

    public Object[][] createBebedor(Object[] bebedor) {
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long id = sqlUtil.nextByClassName(pmf.getPersistenceManager(), "Bebedor");
                private final String nombre = bebedor[0].toString();
                private final String ciudad = bebedor[1].toString();
                private final String presupuesto = bebedor[2].toString();
            };
            LOG.trace("INSERT ⟼ Bebedor::{} ⟼ {} added", fields.id,
                      sqlBebedor.create(pm, fields.id, fields.nombre, fields.ciudad, fields.presupuesto));
            return new Bebedor(fields.id, fields.nombre, fields.ciudad, fields.presupuesto).toTable();
        });
    }

    public Object[][] createBebida(Object[] bebida) {
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long id = sqlUtil.nextByClassName(pmf.getPersistenceManager(), "Bebida");
                private final String nombre = bebida[0].toString();
                private final long idTipoBebida = Long.parseLong(bebida[2].toString());
                private final int gradoAlcohol = Integer.parseInt(bebida[1].toString());
            };
            LOG.trace("INSERT ⟼ Bebida::{} ⟼ {} added", fields.id,
                      sqlBebida.create(pm, fields.id, fields.nombre, fields.idTipoBebida, fields.gradoAlcohol));
            return new Bebida(fields.id, fields.nombre, fields.idTipoBebida, fields.gradoAlcohol).toTable();
        });
    }

    public Object[][] createGustan(Object[] gustan) {
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long idBebedor = Long.parseLong(gustan[0].toString());
                private final long idBebida = Long.parseLong(gustan[1].toString());
            };
            LOG.trace("INSERT ⟼ Gustan::{}-{} ⟼ {} added", fields.idBebedor, fields.idBebida,
                      sqlGustan.create(pm, fields.idBebedor, fields.idBebida));
            return new Gustan(fields.idBebedor, fields.idBebida).toTable();
        });
    }

    public Object[][] createSirven(Object[] sirven) {
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long idBar = Long.parseLong(sirven[0].toString());
                private final long idBebida = Long.parseLong(sirven[1].toString());
                private final String horario = sirven[2].toString();
            };
            LOG.trace("INSERT ⟼ Sirven::{}-{} ⟼ {} added", fields.idBar, fields.idBebida,
                      sqlSirven.create(pm, fields.idBar, fields.idBebida, fields.horario));
            return new Sirven(fields.idBar, fields.idBebida, fields.horario).toTable();
        });
    }

    public Object[][] createTipoBebida(Object[] tipoBebida) {
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long id = sqlUtil.nextBySequenceName(pmf.getPersistenceManager(), "parranderos_sequence");
                private final String nombre = tipoBebida[0].toString();
            };
            LOG.trace("INSERT ⟼ TipoBebida::{} ⟼ {} added", fields.id,
                      sqlTipoBebida.create(pm, fields.id, fields.nombre));
            return new TipoBebida(fields.id, fields.nombre).toTable();
        });
    }

    public Object[][] createVisitan(Object[] visitan) {//yyyy-mm-dd hh:mm:ss
        PersistenceManager pm = pmf.getPersistenceManager();
        return tx(pm, () -> {
            var fields = new Object() {
                private final long idBebedor = Long.parseLong(visitan[0].toString());
                private final long idBar = Long.parseLong(visitan[1].toString());
                private final Timestamp fechaVisita = Timestamp.valueOf(visitan[2].toString());
                private final String horario = visitan[3].toString();
            };
            LOG.trace("INSERT ⟼ Visitan::{}-{} ⟼ {} added", fields.idBebedor, fields.idBar,
                      sqlVisitan.create(pm, fields.idBebedor, fields.idBar, fields.fechaVisita, fields.horario));
            return new Visitan(fields.idBebedor, fields.idBar, fields.fechaVisita, fields.horario).toTable();
        });
    }

    /*
     * ooooooooo.                 .             o8o
     * `888   `Y88.             .o8             `"'
     *  888   .d88'  .ooooo.  .o888oo oooo d8b oooo   .ooooo.  oooo    ooo  .ooooo.
     *  888ooo88P'  d88' `88b   888   `888""8P `888  d88' `88b  `88.  .8'  d88' `88b
     *  888`88b.    888ooo888   888    888      888  888ooo888   `88..8'   888ooo888
     *  888  `88b.  888    .o   888 .  888      888  888    .o    `888'    888    .o
     * o888o  o888o `Y8bod8P'   "888" d888b    o888o `Y8bod8P'     `8'     `Y8bod8P'
     *
     * Seccion dedicada a la consulta de datos
     */

    @SuppressWarnings("unchecked")
    public <R extends Tabulable> List<Object[]> retrieveAll(Class<R> arg)
            throws ReflectiveOperationException {
        String sqlClass = "sql" + arg.getSimpleName();
        Field field = ParranderosP.class.getDeclaredField(sqlClass);
        field.setAccessible(true);
        return ((SQLHandler<R>) field.get(this)).retrieveAll(pmf.getPersistenceManager(), arg);
    }

    @SuppressWarnings("unchecked")
    public <R extends Tabulable> List<Object[]> retrieveOne(Class<R> arg, Object... pks)
            throws ReflectiveOperationException {
        assert pks.length > 1;
        String sqlClass = "sql" + arg.getSimpleName();
        Field field = ParranderosP.class.getDeclaredField(sqlClass);
        field.setAccessible(true);
        SQLHandler<R> value = (SQLHandler<R>) field.get(this);
        return value.retrieveByPK(pmf.getPersistenceManager(), arg, pks);
    }

    /*
     * oooooooooo.             oooo                .
     * `888'   `Y8b            `888              .o8
     *  888      888  .ooooo.   888   .ooooo.  .o888oo  .ooooo.
     *  888      888 d88' `88b  888  d88' `88b   888   d88' `88b
     *  888      888 888ooo888  888  888ooo888   888   888ooo888
     *  888     d88' 888    .o  888  888    .o   888 . 888    .o
     * o888bood8P'   `Y8bod8P' o888o `Y8bod8P'   "888" `Y8bod8P'
     *
     * Seccion dedicada al borrado de tuplas en la base de datos
     */

    @SuppressWarnings("unchecked")
    public <R extends Tabulable> List<Object[]> delete(Class<R> arg, Object... pks)
            throws ReflectiveOperationException {
        String sqlClass = "sql" + arg.getSimpleName();
        Field field = ParranderosP.class.getDeclaredField(sqlClass);
        field.setAccessible(true);
        SQLHandler<R> value = (SQLHandler<R>) field.get(this);
        return value.deleteByPK(pmf.getPersistenceManager(), pks);
    }

    /*
     *   .oooooo.                                              oooo      .
     *  d8P'  `Y8b                                             `888    .o8
     * 888           .ooooo.  ooo. .oo.    .oooo.o oooo  oooo   888  .o888oo  .oooo.    .oooo.o
     * 888          d88' `88b `888P"Y88b  d88(  "8 `888  `888   888    888   `P  )88b  d88(  "8
     * 888          888   888  888   888  `"Y88b.   888   888   888    888    .oP"888  `"Y88b.
     * `88b    ooo  888   888  888   888  o.  )88b  888   888   888    888 . d8(  888  o.  )88b
     *  `Y8bood8P'  `Y8bod8P' o888o o888o 8""888P'  `V88V"V8P' o888o   "888" `Y888""8o 8""888P'
     *
     * Seccion dedicada a los requerimientos de consulta
     */

    public List<Object[]> visitasRealizadas(int bebedor) {return sqlReq.visitasRealizadas(pmf.getPersistenceManager(), bebedor);}

    public List<Object[]> bebidasQueLeGustan(int bebedor) {return sqlReq.bebidasQueLeGustan(pmf.getPersistenceManager(), bebedor);}

    public List<Object[]> bebedoresNumeroVisitasRealizadas() {
        return sqlReq.bebedoresYNumeroDeVisitasRealizadas(pmf.getPersistenceManager());
    }

    public List<Object[]> cantidadBebedoresCiudadVisitanBares(String city) {
        return sqlReq.cantidadDeBebedoresDeUnaCiudadQueVisitanBares(pmf.getPersistenceManager(), city);
    }

    public List<Object[]> baresVisitadosBebedores() {
        return sqlReq.cuantosBaresVisitanLosBebedores(pmf.getPersistenceManager());
    }

    public List<Object[]> cantidadVisitasBebedor() {
        return sqlReq.bebedoresYCuantasVisitasHanHecho(pmf.getPersistenceManager());
    }

    /*
     * ooooooooooooo                                                                 .    o8o
     * 8'   888   `8                                                               .o8    `"'
     *      888      oooo d8b  .oooo.   ooo. .oo.    .oooo.o  .oooo.    .ooooo.  .o888oo oooo   .ooooo.  ooo. .oo.
     *      888      `888""8P `P  )88b  `888P"Y88b  d88(  "8 `P  )88b  d88' `"Y8   888   `888  d88' `88b `888P"Y88b
     *      888       888      .oP"888   888   888  `"Y88b.   .oP"888  888         888    888  888   888  888   888
     *      888       888     d8(  888   888   888  o.  )88b d8(  888  888   .o8   888 .  888  888   888  888   888
     *     o888o     d888b    `Y888""8o o888o o888o 8""888P' `Y888""8o `Y8bod8P'   "888" o888o `Y8bod8P' o888o o888o
     *
     * ooo        ooooo
     * `88.       .888'
     *  888b     d'888   .oooo.   ooo. .oo.    .oooo.    .oooooooo  .ooooo.  oooo d8b
     *  8 Y88. .P  888  `P  )88b  `888P"Y88b  `P  )88b  888' `88b  d88' `88b `888""8P
     *  8  `888'   888   .oP"888   888   888   .oP"888  888   888  888ooo888  888
     *  8    Y     888  d8(  888   888   888  d8(  888  `88bod8P'  888    .o  888
     * o8o        o888o `Y888""8o o888o o888o `Y888""8o `8oooooo.  `Y8bod8P' d888b
     *                                                  d"     YD
     *                                                  "Y88888P'
     *
     * Seccion dedicada a una forma de acortar la ejecucion de queries
     */
    @FunctionalInterface private interface TxManager { Object[][] call() throws Exception; }

    /**
     * @param pm PersistenceManager Para conseguir la sesion actual
     * @param lambda La operacion que se quiera hacer en la transaccion<br>
     *               La lambda representa una operacion para construir un elemento completo<br>
     *               <strong>Ejemplo:</strong>
     *               ClassA necesita una unica operacion<br>
     *               Entonces se hace una lambda que construya ClassA<br>
     *               tx(pm, () -> new ClassA().toTable(););
     * @return Un array de objetos que representa el objeto completo
     * @see #txs(PersistenceManager, TxManager...)
     * @see Tabulable#toTable()
     */
    private Object[][] tx(PersistenceManager pm, TxManager lambda) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Object[][] objs = lambda.call();
            tx.commit();
            return objs;
        } catch (Exception e) {
            LOG.error("{}: {}\n {}", e.getClass().getSimpleName(), e.getMessage(), generateErrorDetail(e));
            throw new RuntimeException(e);
        } finally {
            if (tx.isActive()) tx.rollback();
        }
    }

    /**
     * Metodo exclusivo para cuando las clases heredan de otras con la estrategia de MappedSupperClass
     * @param pm PersistenceManager Para conseguir la sesion actual
     * @param lambdas Cada operacion que se quiera hacer en la transaccion<br>
     *                Cada item en los de lambdas representa una operacion para construir un elemento completo<br>
     *                <strong>Ejemplo:</strong><br>
     *                ClassB hereda ClassA, entonces para construir un objeto de ClassB se deben hacer 2 operaciones<br>
     *                1. Construir ClassA<br>
     *                2. Construir ClassB<br>
     *                Entonces se debe pasar un array de 2 lambdas, donde la primera construye ClassA y la segunda ClassB<br>
     *                txs(pm,<br>
     *                () -> new ClassA().toTable();,<br>
     *                () -> new ClassB().toTable();<br>
     *                );
     * @return Un array de arrays de objetos, donde cada array de objetos representa un elemento completo
     * @see #tx(PersistenceManager, TxManager) para cuando se necesita una unica operacion
     * @see Tabulable#toTable()
     */
    private Object[][][] txs(PersistenceManager pm, TxManager... lambdas) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            //noinspection AnonymousInnerClassMayBeStatic
            Object[][][] objs = new ArrayList<Object[][]>(){{
                for (TxManager l : lambdas) {
                    Object[][] call = l.call();
                    if (call != null) add(call);
                }
            }}.toArray(Object[][][]::new);
            tx.commit();
            return objs;
        } catch (Exception e) {
            LOG.error("{}: {}\n {}", e.getClass().getSimpleName(), e.getMessage(), generateErrorDetail(e));
            throw new RuntimeException(e);
        } finally {
            if (tx.isActive()) tx.rollback();
        }
    }
}
