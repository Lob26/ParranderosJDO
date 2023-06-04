package edu.uniandes.persistence;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/** Esta clase proporciona métodos para ejecutar consultas SQL específicas y purificar los resultados obtenidos. */
@SuppressWarnings({"unchecked", "rawtypes"}) class SQLReq {
    private static final Properties P = new Properties();

    static {
        try {P.loadFromXML(SQLReq.class.getResourceAsStream("/META-INF/data_query.xml"));}
        catch (IOException e) {throw new RuntimeException(e);}
    }

    /**
     * Crea una consulta utilizando el PersistenceManager y el nombre especificados, junto con un filtro opcional.
     *
     * @param pm    el PersistenceManager utilizado para crear la consulta
     * @param name  el nombre de la consulta SQL
     * @param filer el filtro para la consulta (opcional)
     * @return la consulta Query creada
     */
    private static Query query(PersistenceManager pm, String name, String... filer) {
        return pm.newQuery(Query.SQL, P.getProperty(name) + (filer.length == 0 ? "" : " AND " + filer[0]));
    }

    /**
     * Purifica los resultados de las filas añadiendo los encabezados y retornando una lista de arreglos de objetos.
     *
     * @param rows   las filas de resultados obtenidas de la base de datos
     * @param header los encabezados de las columnas
     * @return una lista de arreglos de objetos purificados que incluye los encabezados y las filas de resultados
     */
    static List<Object[]> purify(List<Object[]> rows, String... header) {
        return new LinkedList<>() {{
            add(header);
            addAll(rows);
        }};
    }

    /**
     * Obtiene las visitas realizadas por un bebedor específico en forma purificada.
     *
     * @param pm        el PersistenceManager utilizado para ejecutar la consulta
     * @param idBebedor el ID del bebedor
     * @return una lista de arreglos de objetos purificados que representan las visitas realizadas
     */
    List<Object[]> visitasRealizadas(PersistenceManager pm, int idBebedor) {
        return purify((List<Object[]>) query(pm, "visitas realizadas").setParameters(idBebedor).executeList(),
                      "ID", "Nombre", "Ciudad", "Presupuesto", "Cantidad de sedes", "Fecha de visita", "Horario");
    }

    /**
     * Obtiene las bebidas que le gustan a un bebedor específico en forma purificada.
     *
     * @param pm        el PersistenceManager utilizado para ejecutar la consulta
     * @param idBebedor el ID del bebedor
     * @return una lista de arreglos de objetos purificados que representan las bebidas que le gustan al bebedor
     */
    List<Object[]> bebidasQueLeGustan(PersistenceManager pm, int idBebedor) {
        return purify((List<Object[]>) query(pm, "bebidas que le gustan").setParameters(idBebedor).executeList(),
                      "ID", "Nombre", "ID tipo de bebida", "Grado de alcohol", "Tipo de bebida");
    }

    /**
     * Obtiene los bebedores y el número de visitas realizadas en forma purificada.
     *
     * @param pm el PersistenceManager utilizado para ejecutar la consulta
     * @return una lista de arreglos de objetos purificados que representan los bebedores y el número de visitas
     */
    List<Object[]> bebedoresYNumeroDeVisitasRealizadas(PersistenceManager pm) {
        return purify((List<Object[]>) query(pm, "bebedores y numero de visitas realizadas").executeList(),
                      "ID", "Nombre", "Ciudad", "Presupuesto", "Número de visitas");
    }

    /**
     * Obtiene la cantidad de bebedores de una ciudad que visitan bares en forma purificada.
     *
     * @param pm     el PersistenceManager utilizado para ejecutar la consulta
     * @param ciudad la ciudad para la cual se desea obtener la cantidad de bebedores
     * @return una lista de arreglos de objetos purificados que representan la cantidad de bebedores de la ciudad
     */
    List<Object[]> cantidadDeBebedoresDeUnaCiudadQueVisitanBares(PersistenceManager pm, String ciudad) {
        return purify((List<Object[]>) query(pm, "cantidad de bebedores de una ciudad que visitan bares")
                .setParameters(ciudad).executeList(),
                      "Cantidad de bebedores");
    }

    /**
     * Obtiene la cantidad de bares visitados por los bebedores en forma purificada.
     *
     * @param pm el PersistenceManager utilizado para ejecutar la consulta
     * @return una lista de arreglos de objetos purificados que representan la cantidad de bares visitados por los bebedores
     */
    List<Object[]> cuantosBaresVisitanLosBebedores(PersistenceManager pm) {
        return purify((List<Object[]>) query(pm, "cuantos bares visitan los bebedores").executeList(),
                      "ID", "Nombre", "Ciudad", "Presupuesto", "Número de bares visitados");
    }

    /**
     * Obtiene los bebedores y la cantidad de visitas que han realizado en forma purificada.
     *
     * @param pm el PersistenceManager utilizado para ejecutar la consulta
     * @return una lista de arreglos de objetos purificados que representan los bebedores y la cantidad de visitas realizadas
     */
    List<Object[]> bebedoresYCuantasVisitasHanHecho(PersistenceManager pm) {
        return purify((List<Object[]>) query(pm, "bebedores y cuantas visitas han hecho").executeList(),
                      "ID", "Nombre", "Ciudad", "Presupuesto", "Número de visitas");
    }
}

