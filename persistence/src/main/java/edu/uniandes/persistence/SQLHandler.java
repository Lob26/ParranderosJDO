package edu.uniandes.persistence;

import edu.uniandes.annotations.handler.TableHandler;
import edu.uniandes.util.Tabulable;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Esta clase proporciona métodos para manipular entidades en una base de datos utilizando consultas SQL.
 *
 * @param <Entity> el tipo de entidad que se manipula y que debe implementar la interfaz Tabulable (ver {@link Tabulable})
 * @see <a href="https://www.datanucleus.org/products/accessplatform/jdo/query.html">Query</a>
 */
@SuppressWarnings("unchecked")
class SQLHandler<Entity extends Tabulable> {
    private final Map<String, String> queries;

    /**
     * Crea una nueva instancia de SQLHandler y obtiene las consultas para la entidad dada.
     *
     * @param entity la clase de la entidad
     */
    SQLHandler(Class<Entity> entity) {this.queries = TableHandler.getQueries(entity);}

    /**
     * Convierte los valores booleanos en representaciones numéricas (1 o 0) en el arreglo dado para que puedan ser usados en una consulta PL/SQL Oracle.
     *
     * @param values el arreglo de valores a limpiar
     */
    private static void cleanBooleans(Object[] values) {
        IntStream.range(0, values.length).forEach(i -> {
            if (values[i] instanceof Boolean b) values[i] = b ? 1 : 0;
        });
    }

    /**
     * Crea una nueva entidad en la base de datos con los valores proporcionados.
     *
     * @param pm     el PersistenceManager utilizado para ejecutar la consulta
     * @param values los valores de los campos de la entidad
     * @return el número de filas afectadas por la operación de creación
     */
    long create(PersistenceManager pm, Object... values) {
        assert values != null && values.length >= 1;
        cleanBooleans(values);
        return (long) pm.newQuery(Query.SQL, queries.get("INSERT")).setParameters(values).execute();
    }

    /**
     * Recupera una entidad de la base de datos utilizando su clave primaria (PK).
     *
     * @param pm     el PersistenceManager utilizado para ejecutar la consulta
     * @param entity la clase de la entidad
     * @param keys   los valores de las claves primarias
     * @return una lista que contiene un arreglo de objetos con los valores de la entidad recuperada y un arreglo de
     *         cadenas con los encabezados de la entidad
     * @throws ReflectiveOperationException si se produce un error durante la reflexión de la clase de la entidad
     */
    List<Object[]> retrieveByPK(PersistenceManager pm, Class<Entity> entity, Object... keys)
            throws ReflectiveOperationException {
        assert keys != null && keys.length >= 1;
        cleanBooleans(keys);
        return List.of(
                (Object[]) pm.newQuery(Query.SQL, queries.get("SELECT_ONE")).setParameters(keys).executeUnique(),
                (String[]) entity.getMethod("getHeaders").invoke(null)
        );
    }

    /**
     * Recupera todas las entidades de la base de datos.
     *
     * @param pm     el PersistenceManager utilizado para ejecutar la consulta
     * @param entity la clase de la entidad
     * @return una lista que contiene arreglos de objetos con los valores de las entidades recuperadas y un arreglo de
     *         cadenas con los encabezados de la entidad
     * @throws ReflectiveOperationException si se produce un error durante la reflexión de la clase de la entidad
     */
    List<Object[]> retrieveAll(PersistenceManager pm, Class<Entity> entity)
            throws ReflectiveOperationException {
        return SQLReq.purify(
                (List<Object[]>) pm.newQuery(Query.SQL, queries.get("SELECT_ALL")).executeList(),
                (String[]) entity.getMethod("getHeaders").invoke(null)
        );
    }

    /**
     * Actualiza una entidad en la base de datos utilizando su clave primaria (PK) y los nuevos valores proporcionados.
     *
     * @param pm     el PersistenceManager utilizado para ejecutar la consulta
     * @param entity la clase de la entidad
     * @param values los nuevos valores de los campos de la entidad
     * @return una lista que contiene un arreglo de objetos con información sobre el resultado de la operación de
     *         actualización
     */
    List<Object[]> updateByPK(PersistenceManager pm, Class<Entity> entity, Object... values) {
        assert values != null && values.length >= 1;
        cleanBooleans(values);
        return List.of(
                new Object[]{"Filas actualizadas"},
                new Object[]{pm.newQuery(Query.SQL, queries.get("UPDATE")).setParameters(values).execute()}
        );
    }

    /**
     * Elimina una entidad de la base de datos utilizando su clave primaria (PK).
     *
     * @param pm   el PersistenceManager utilizado para ejecutar la consulta
     * @param keys los valores de las claves primarias
     * @return una lista que contiene un arreglo de objetos con información sobre el resultado de la operación de
     *         eliminación
     */
    List<Object[]> deleteByPK(PersistenceManager pm, Object... keys) {
        assert keys != null && keys.length >= 1;
        Query<Long> q = pm.newQuery(Query.SQL, queries.get("DELETE"));
        return List.of(
                new Object[]{"Filas actualizadas"},
                new Object[]{q.setParameters(keys).execute()}
        );
    }

    /**
     * Ejecuta una consulta personalizada en la base de datos utilizando la clave proporcionada y, opcionalmente, la
     * clase de la entidad y otros argumentos.
     *
     * @param pm     el PersistenceManager utilizado para ejecutar la consulta
     * @param key    la clave de la consulta personalizada
     * @param entity la clase de la entidad (puede ser nulo)
     * @param args   los argumentos adicionales para la consulta
     * @return una lista que contiene un arreglo de objetos con información sobre el resultado de la consulta
     * @throws ReflectiveOperationException si se produce un error durante la reflexión de la clase de la entidad
     */
    List<Object[]> customQuery(PersistenceManager pm, String key, Class<Entity> entity, Object... args)
            throws ReflectiveOperationException {
        var custom = pm.newQuery(
                Query.SQL,
                Optional.ofNullable(queries.get(key))
                        .orElseThrow(() -> new IllegalArgumentException("Nombre de la query invalida"))
        );

        if (entity == null) return List.of(
                new Object[]{"Filas modificadas"},
                (Object[]) custom.setParameters(args).executeUnique()
        );
        else return SQLReq.purify(
                (List<Object[]>) custom.setParameters(args).executeList(),
                (String[]) entity.getMethod("getHeaders").invoke(null)
        );
    }
}

