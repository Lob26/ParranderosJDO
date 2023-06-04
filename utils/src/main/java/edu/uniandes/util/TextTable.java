package edu.uniandes.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Esta clase representa una tabla de texto que puede ser construida y formateada para mostrar datos en forma tabular.
 */
public class TextTable {
    private final List<List<Object>> matrix = new ArrayList<>();
    private final StringBuilder out = new StringBuilder();
    private int[] widths;

    private TextTable() {}

    /**
     * Crea una instancia de `TextTable` para construir una tabla de texto.
     *
     * @return una nueva instancia de `TextTable`
     */
    public static TextTable builder() {return new TextTable();}

    /**
     * Agrega una fila a la tabla de texto.
     *
     * @param row el arreglo de objetos que representa una fila de la tabla
     * @return la instancia actual de `TextTable` para permitir un encadenamiento adicional
     */
    public TextTable append(Object[] row) {
        matrix.add(Arrays.stream(row).toList());
        return this;
    }

    /**
     * Agrega múltiples filas a la tabla de texto.
     *
     * @param rows el arreglo bidimensional de objetos que representa múltiples filas de la tabla
     * @return la instancia actual de `TextTable` para permitir un encadenamiento adicional
     */
    public TextTable appends(Object[][] rows) {
        Arrays.stream(rows).forEach(this::append);
        return this;
    }

    /**
     * Devuelve la representación en cadena de la tabla de texto.
     *
     * @return la representación en cadena de la tabla de texto
     */
    @Override
    public String toString() {
        build();
        return out.toString();
    }

    /**
     * Construye la tabla de texto. Calcula los anchos de las columnas, imprime la línea horizontal superior,
     * imprime la primera fila, imprime las filas restantes, e imprime la línea horizontal inferior si hay más de una fila.
     */
    private void build() {
        calculateWidths();
        printHorizontalLine();
        printRow(0);
        printHorizontalLine();
        IntStream.range(1, matrix.size()).forEach(this::printRow);
        if (matrix.size() > 1) printHorizontalLine();
    }

    /** Calcula los anchos de las columnas en función del contenido de la matriz. */
    private void calculateWidths() {
        int cols = matrix.get(0).size();
        widths = new int[cols];
        for (int j = 0; j < cols; j++) {
            int max = 0;
            for (List<Object> objects : matrix) {
                if (objects.get(j).toString().length() > max) {
                    max = objects.get(j).toString().length();
                }
            }
            widths[j] = max;
        }
    }

    /** Imprime una línea horizontal en la tabla de texto. */
    private void printHorizontalLine() {
        out.append("+");
        Arrays.stream(widths).forEach(width -> {
            out.append("-".repeat(Math.max(0, width + 2)));
            out.append("+");
        });
        out.append("\n");
    }

    /**
     * Imprime una fila específica en la tabla de texto.
     *
     * @param rowIndex el índice de la fila a imprimir
     */
    private void printRow(int rowIndex) {
        List<Object> row = matrix.get(rowIndex);
        out.append("|");
        IntStream.range(0, row.size()).forEach(i -> {
            String value = row.get(i).toString();
            out.append(" ").append(value).append(" ".repeat(Math.max(0, widths[i]) - value.length())).append(" |");
        });
        out.append("\n");
    }
}
