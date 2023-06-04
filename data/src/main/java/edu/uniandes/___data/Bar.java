package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;
import edu.uniandes.annotations.core.Query;

@Query(k="DELETE BY nombre", v="DELETE FROM Bar WHERE nombre = ?")
@Query(k="SELECT BY nombre", v="SELECT * FROM Bar WHERE nombre = ?")
@Query(k="aumentar sedes bares por ciudad", v="UPDATE Bar SET cantSedes = cantSedes + 1 WHERE ciudad = ?")
@Data("bar") class Bar {
    @PK(sequence = "parranderos_sequence") long id;
    String nombre;
    String ciudad;
    String presupuesto;
    int cantSedes;
}
