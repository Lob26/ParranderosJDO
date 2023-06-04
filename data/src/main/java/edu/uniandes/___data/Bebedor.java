package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;
import edu.uniandes.annotations.core.Query;

import java.util.List;

@Query(k="DELETE BY nombre", v="DELETE FROM bebedor b WHERE b.nombre = ?")
@Query(k="SELECT BY nombre", v="SELECT b FROM bebedor b WHERE b.nombre = ?")
@Query(k="cambiar ciudad", v="UPDATE bebedor b SET b.ciudad = ? WHERE b.id = ?")
@Data("bebedor") class Bebedor {
    @PK(sequence = "parranderos_sequence") long id;
    String nombre;
    String ciudad;
    String presupuesto;
    List<Object[]> visitasRealizadas;
    List<Object[]> bebidasQueLeGustan;
}
