package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;
import edu.uniandes.annotations.core.Query;

import java.sql.Timestamp;

@Query(k="DELETE BY bebedor", v="DELETE FROM visitan WHERE idBebedor = ?")
@Query(k="DELETE BY bar", v="DELETE FROM visitan WHERE idBar = ?")
@Data("visitan") class Visitan {
    @PK long idBebedor;
    @PK long idBar;
    Timestamp fechaVisita;
    String horario;
}
