package edu.uniandes.annotations.core;

import java.lang.annotation.*;

/**
 * Esta anotación se usa para indicar que un atributo es una llave foránea<br>
 * Es meramente decorativo
 * Ejemplo:
 * <pre>
 *     {@literal @}FK("Cliente.id")
 *     private int idCliente;
 * </pre>
 * Representa que el atributo idCliente es una llave foránea a la tabla Cliente en la columna id
 *
 * @see edu.uniandes.annotations.core.PK
 */
@Documented @Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FK {
    String value();
}
