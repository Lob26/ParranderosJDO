package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;
import edu.uniandes.annotations.core.Query;

@Query(k="bares y cantidad de bebidas servidas", v="SELECT idbar, count(*) as numbebidas FROM sirven GROUP BY idbar")
@Data("sirven") class Sirven {
    @PK long idBar;
    @PK long idBebida;
    String horario;
}
