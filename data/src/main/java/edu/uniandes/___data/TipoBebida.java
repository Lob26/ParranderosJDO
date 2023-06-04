package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;
import edu.uniandes.annotations.core.Query;

@Query(k="DELETE BY nombre", v="DELETE FROM tipobebida WHERE nombre = ?")
@Query(k="SELECT BY nombre", v="SELECT * FROM tipobebida WHERE nombre = ?")
@Data("tipobebida") class TipoBebida {
    @PK(sequence = "parranderos_sequence") long id;
    String nombre;
}
