package edu.uniandes.annotations.core;

import java.lang.annotation.*;

/**
 * Esta anotación se usa para indicar que un atributo es una llave primaria<br>
 * A diferencia de {@link FK}, esta anotación es funcional y se usa para la creacion de las queries CRUD<br>
 * Ejemplo 1:
 * En esta situacion solo se marca la llave primaria, como no se especifica la secuencia, se asume que es asignada por el usuario
 * <pre>
 *     {@literal @}PK
 *     private int id;
 * </pre>
 * Ejemplo 2:
 * En esta situacion se especifica la secuencia de la llave primaria, por lo que se va a usar mas adelante en el metodo
 * {@link edu.uniandes.persistence.SQLUtil#nextByClassName(javax.jdo.PersistenceManager, String)}
 * <pre>
 *     {@literal @}PK(sequence = "seq_cliente")
 *     private int id;
 * </pre>
 *
 * @see edu.uniandes.annotations.core.FK
 * @see edu.uniandes.persistence.SQLUtil#nextByClassName(javax.jdo.PersistenceManager, String)
 * @see edu.uniandes.persistence.SQLUtil#nextBySequenceName(javax.jdo.PersistenceManager, String)
 */
@Documented @Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PK {
    String sequence() default "";
}