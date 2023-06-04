package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;
import edu.uniandes.annotations.core.Query;

@Query(k="DELETE BY nombre", v="DELETE FROM bebida WHERE nombre = ?")
@Query(k="SELECT BY nombre", v="SELECT * FROM bebida WHERE nombre = ?")
@Query(k="DELETE no servidas", v="DELETE FROM bebida WHERE id NOT IN (SELECT idbebida FROM sirven)")
@Data("bebida") class Bebida {
    @PK(sequence = "parranderos_sequence") long id;
    String nombre;
    long idTipoBebida;
    int gradoAlcohol;
}
