package edu.uniandes.util;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Esta clase representa un mapa ordenado, que almacena pares clave-valor en un orden especifico.
 *
 * @param <K> el tipo de la clave
 * @param <V> el tipo del valor
 */
@SuppressWarnings("unused") public class OrderedMap<K, V> {
    private final LinkedList<OMEntry<K, V>> iterable = new LinkedList<>();
    private final Map<K, Integer> table = new HashMap<>();

    /**
     * Agrega una clave y un valor al mapa ordenado.
     *
     * @param key   la clave a agregar
     * @param value el valor a agregar
     * @return el valor anterior asociado a la clave, o null si la clave no estaba presente
     */
    public V put(K key, V value) {
        Integer index = table.get(key);
        if (index == null) { // No esta presente
            iterable.add(new OMEntry<>(key, value));
            table.put(key, iterable.size() - 1);
        } else iterable.get(index).setValue(value);
        return value;
    }

    /**
     * Agrega los elementos de uno o mas mapas ordenados al mapa actual.
     *
     * @param maps los mapas ordenados de los cuales agregar elementos
     */
    @SafeVarargs public final void putAll(OrderedMap<? extends K, ? extends V>... maps) {
        for (OrderedMap<? extends K, ? extends V> map : maps) map.forEach(this::put);
    }

    /**
     * Obtiene el valor asociado a una clave en el mapa ordenado.
     *
     * @param key la clave a buscar
     * @return el valor asociado a la clave, o null si la clave no esta presente en el mapa
     */
    public V get(K key) {
        Integer index = table.get(key);
        if (index == null) return null;
        return iterable.get(index).value;
    }

    /**
     * Obtiene el valor en la posicion dada en el mapa ordenado.
     *
     * @param index la posicion del valor a obtener
     * @return el valor en la posicion dada
     */
    public V get(int index) {return iterable.get(index).value;}

    /**
     * Obtiene todas las claves del mapa ordenado.
     *
     * @param <T> el tipo de las claves
     * @return un arreglo con todas las claves del mapa
     */
    @SuppressWarnings("unchecked") public <T> T[] getKeys() {
        return (T[]) iterable.stream().map(OMEntry::getKey).toArray();
    }

    /**
     * Obtiene todos los valores del mapa ordenado.
     *
     * @param <T> el tipo de los valores
     * @return un arreglo con todos los valores del mapa
     */
    @SuppressWarnings("unchecked") public <T> T[] getValues() {
        return (T[]) iterable.stream().map(OMEntry::getValue).toArray();
    }

    /**
     * Elimina una clave y su valor asociado del mapa ordenado.
     *
     * @param key la clave a eliminar
     * @return true si la clave se encontraba en el mapa y fue eliminada, o false si la clave no estaba presente
     */
    public boolean remove(K key) {
        Integer index = table.remove(key);
        if (index == null) return false;
        iterable.remove((int) index);
        return true;
    }

    /**
     * Elimina todos los elementos del mapa ordenado.
     */
    public void clear() {
        iterable.clear();
        table.clear();
    }

    /**
     * Itera sobre cada par clave-valor del mapa ordenado y aplica la accion especificada.
     *
     * @param action la accion a aplicar a cada par clave-valor
     * @throws NullPointerException            si la accion es nula
     * @throws ConcurrentModificationException si se produce una modificacion concurrente al iterar
     */
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (OMEntry<K, V> entry : iterable) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    /**
     * Esta clase interna representa una entrada del mapa ordenado.
     *
     * @param <K> el tipo de la clave
     * @param <V> el tipo del valor
     */
    static class OMEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        /**
         * Crea una nueva entrada para el mapa ordenado con la clave y el valor especificados.
         *
         * @param key   la clave de la entrada
         * @param value el valor de la entrada
         */
        private OMEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Obtiene la clave de la entrada.
         *
         * @return la clave de la entrada
         */
        @Override public K getKey() {return key;}

        /**
         * Obtiene el valor de la entrada.
         *
         * @return el valor de la entrada
         */
        @Override public V getValue() {return value;}

        /**
         * Devuelve una representacion en cadena de la entrada.
         *
         * @return una cadena que representa la entrada en el formato "clave=valor"
         */
        @Override public String toString() {return key + "=" + value;}

        /**
         * Establece el valor de la entrada y devuelve el valor anterior.
         *
         * @param value el nuevo valor de la entrada
         * @return el valor anterior de la entrada
         */
        @Override public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}

