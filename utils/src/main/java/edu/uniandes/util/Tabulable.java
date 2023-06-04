package edu.uniandes.util;

public interface Tabulable {
    /**
     * Un metodo para convertir un objeto en una representacion en arreglo<br>
     * Para mas tarde ser mostrado en una tabla<br>
     *
     * @return Una representacion en arreglo del objeto en cuestion<br>
     * La primera fila es el encabezado con los nombres de los campos de la clase<br>
     * La segunda fila es el valor de los campos de la clase<br>
     */
    Object[][] toTable();
}
