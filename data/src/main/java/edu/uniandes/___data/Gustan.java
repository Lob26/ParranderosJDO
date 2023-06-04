package edu.uniandes.___data;

import edu.uniandes.annotations.core.Data;
import edu.uniandes.annotations.core.PK;

@Data("gustan") class Gustan {
    @PK long idBebedor;
    @PK long idBebida;
}
