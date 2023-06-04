package edu.uniandes.business;

import edu.uniandes.persistence.ParranderosP;
import edu.uniandes.util.Tabulable;
import edu.uniandes.util.TextTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/** Esta clase proporciona métodos para crear, asociar, recuperar y eliminar entidades utilizando el objeto de persistencia ParranderosP. */
public class ParranderosB {
    private final static Logger LOG = LogManager.getLogger(ParranderosB.class);
    private final ParranderosP persistence;
    private final SimpleDateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timestampSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor de la clase ParranderosB.
     *
     * @param persistence el objeto de persistencia ParranderosP utilizado para realizar las operaciones de base de datos
     */
    public ParranderosB(ParranderosP persistence) {
        this.persistence = persistence;
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

    /**
     * Crea un nuevo bar en la base de datos.
     *
     * @param bar los datos del bar a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createBar(Object[] bar) {
        LOG.info("START ⟼ Creando un bar {}", bar[0]);
        Object[][] response = persistence.createBar(bar);
        LOG.info("END ⟼ Bar {} creado", bar[0]);
        return TextTable.builder().append(response).toString();
    }

    /**
     * Crea un nuevo bebedor en la base de datos.
     *
     * @param bebedor los datos del bebedor a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createBebedor(Object[] bebedor) {
        LOG.info("START ⟼ Creando un bebedor {}", bebedor[0]);
        Object[][] response = persistence.createBebedor(bebedor);
        LOG.info("END ⟼ Bebedor {} creado", bebedor[0]);
        return TextTable.builder().append(response).toString();
    }

    /**
     * Crea una nueva bebida en la base de datos.
     *
     * @param bebida los datos de la bebida a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createBebida(Object[] bebida) {
        LOG.info("START ⟼ Creando una bebida {}", bebida[0]);
        Object[][] response = persistence.createBebida(bebida);
        LOG.info("END ⟼ Bebida {} creada", bebida[0]);
        return TextTable.builder().append(response).toString();
    }

    /**
     * Crea una asociación "gustan" entre un bebedor y una bebida en la base de datos.
     *
     * @param gustan los datos de la asociación "gustan" a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createGustan(Object[] gustan) {
        LOG.info("START ⟼ Asociando {} y {}", gustan[0], gustan[1]);
        Object[][] response = persistence.createGustan(gustan);
        LOG.info("END ⟼ {} y {} asociado", gustan[0], gustan[1]);
        return TextTable.builder().append(response).toString();
    }

    /**
     * Crea una asociación "sirven" entre un bar y una bebida en la base de datos.
     *
     * @param sirven los datos de la asociación "sirven" a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createSirven(Object[] sirven) {
        LOG.info("START ⟼ Asociando {} y {}", sirven[0], sirven[1]);
        Object[][] response = persistence.createSirven(sirven);
        LOG.info("END ⟼ {} y {} asociado", sirven[0], sirven[1]);
        return TextTable.builder().append(response).toString();
    }

    /**
     * Crea un nuevo tipo de bebida en la base de datos.
     *
     * @param tipoBebida los datos del tipo de bebida a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createTipoBebida(Object[] tipoBebida) {
        LOG.info("START ⟼ Creando un tipo de bebida {}", tipoBebida[0]);
        Object[][] response = persistence.createTipoBebida(tipoBebida);
        LOG.info("END ⟼ Tipo de bebida {} creado", tipoBebida[0]);
        return TextTable.builder().append(response).toString();
    }

    /**
     * Crea una asociación "visitan" entre un bebedor y un bar en la base de datos.
     *
     * @param visitan los datos de la asociación "visitan" a crear
     * @return una representación en formato de tabla de texto del resultado de la operación
     */
    public String createVisitan(Object[] visitan) {
        LOG.info("START ⟼ Asociando {} y {}", visitan[0], visitan[1]);
        Object[][] response = persistence.createVisitan(visitan);
        LOG.info("END ⟼ {} y {} asociado", visitan[0], visitan[1]);
        return TextTable.builder().append(response).toString();
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

    /**
     * Recupera todas las entidades de un tipo específico de la base de datos.
     *
     * @param arg la clase de la entidad a recuperar
     * @return una representación en formato de tabla de texto de las entidades recuperadas
     * @throws ReflectiveOperationException si se produce un error al realizar la operación de reflexión
     */
    public <R extends Tabulable> String retrieveAll(Class<R> arg)
            throws ReflectiveOperationException {
        LOG.info("START ⟼ Recuperando todos los {}", arg.getSimpleName());
        List<Object[]> vos = persistence.retrieveAll(arg);
        LOG.info("END ⟼ {} de {} recuperados", vos.size(), arg.getSimpleName());

        TextTable builder = TextTable.builder();
        vos.forEach(builder::append);

        return arg.getSimpleName() + "::\n" + builder;
    }

    /**
     * Recupera una entidad específica de la base de datos utilizando su clase y claves primarias.
     *
     * @param arg la clase de la entidad a recuperar
     * @param pks las claves primarias de la entidad a recuperar
     * @return una representación en formato de tabla de texto de la entidad recuperada
     * @throws ReflectiveOperationException si se produce un error al realizar la operación de reflexión
     */
    public <R extends Tabulable> String retrieveOne(Class<R> arg, Object... pks)
            throws ReflectiveOperationException {
        LOG.info("START ⟼ Recuperando {} con id(s) {}", arg.getSimpleName(), Arrays.toString(pks));
        List<Object[]> vo = persistence.retrieveOne(arg, pks);
        LOG.info("END ⟼ {} con id(s) {} recuperado [{}]", arg, Arrays.toString(pks), vo);

        TextTable builder = TextTable.builder();
        vo.forEach(builder::append);

        return arg.getSimpleName() + " PK:" + Arrays.toString(pks) + "::\n" + builder;
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

    /**
     * Elimina una entidad específica de la base de datos utilizando su clase y claves primarias.
     *
     * @param arg la clase de la entidad a eliminar
     * @param pks las claves primarias de la entidad a eliminar
     * @return una representación en formato de tabla de texto del resultado de la operación
     * @throws ReflectiveOperationException si se produce un error al realizar la operación de reflexión
     */
    public <R extends Tabulable> String delete(Class<R> arg, Object... pks)
            throws ReflectiveOperationException {
        LOG.info("START ⟼ Borrando {} con id(s) {}", arg.getSimpleName(), pks);
        List<Object[]> l = persistence.delete(arg, pks);
        LOG.info("END ⟼ {} {} borrado(s)", l.get(0), arg);
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return arg.getSimpleName() + " PK:" + Arrays.toString(pks) + "::\n" + builder;
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
    public String visitasRealizadas(int bebedor) {
        LOG.info("START ⟼ Consulta: visitasRealizadas");
        List<Object[]> l = persistence.visitasRealizadas(bebedor);
        LOG.info("END ⟼ Consulta: visitasRealizadas");
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return "Consulta::\n" + builder;
    }

    public String bebidasQueLeGustan(int bebedor) {
        LOG.info("START ⟼ Consulta: bebidasQueLeGustan");
        List<Object[]> l = persistence.bebidasQueLeGustan(bebedor);
        LOG.info("END ⟼ Consulta: bebidasQueLeGustan");
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return "Consulta::\n" + builder;
    }

    public String bebedoresNumeroVisitasRealizadas() {
        LOG.info("START ⟼ Consulta: bebedoresNumeroVisitasRealizadas");
        List<Object[]> l = persistence.bebedoresNumeroVisitasRealizadas();
        LOG.info("END ⟼ Consulta: bebedoresNumeroVisitasRealizadas");
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return "Consulta::\n" + builder;
    }

    public String cantidadBebedoresCiudadVisitanBares(String city) {
        LOG.info("START ⟼ Consulta: cantidadBebedoresCiudadVisitanBares");
        List<Object[]> l = persistence.cantidadBebedoresCiudadVisitanBares(city);
        LOG.info("END ⟼ Consulta: cantidadBebedoresCiudadVisitanBares");
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return "Consulta::\n" + builder;
    }

    public String baresVisitadosBebedores() {
        LOG.info("START ⟼ Consulta: baresVisitadosBebedores");
        List<Object[]> l = persistence.baresVisitadosBebedores();
        LOG.info("END ⟼ Consulta: baresVisitadosBebedores");
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return "Consulta::\n" + builder;
    }

    public String cantidadVisitasBebedor() {
        LOG.info("START ⟼ Consulta: cantidadVisitasBebedor");
        List<Object[]> l = persistence.cantidadVisitasBebedor();
        LOG.info("END ⟼ Consulta: cantidadVisitasBebedor");
        TextTable builder = TextTable.builder();
        l.forEach(builder::append);

        return "Consulta::\n" + builder;
    }
}
