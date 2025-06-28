// Archivo: um/edu/uy/tadsAuxiliares/hashtable/HashIterator.java

package um.edu.uy.tadsAuxiliares.hashtable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Un iterador externo para la clase HashCerradaLineal.
 * Esta clase no es pública, ya que solo necesita ser visible dentro de su propio paquete.
 * @param <K> El tipo de la clave.
 * @param <T> El tipo del valor.
 */
class HashIterator<K extends Comparable<K>, T> implements Iterator<T> {

    // Campos para almacenar el estado de la tabla hash que se está iterando
    private final Objeto<K, T>[] tabla;
    private final int capacidad;
    private final int size;
    private final Objeto<K, T> tombstone;

    // Campos para el estado interno del iterador
    private int currentIndex;
    private int foundCount;

    /**
     * Constructor que recibe el estado de la HashCerradaLineal.
     * @param tabla El array interno de la tabla hash.
     * @param capacidad La capacidad total del array.
     * @param size El número de elementos reales en la tabla.
     * @param tombstone La instancia del objeto que se usa para marcar elementos borrados.
     */
    public HashIterator(Objeto<K, T>[] tabla, int capacidad, int size, Objeto<K, T> tombstone) {
        this.tabla = tabla;
        this.capacidad = capacidad;
        this.size = size;
        this.tombstone = tombstone;
        this.currentIndex = 0;
        this.foundCount = 0;
    }

    @Override
    public boolean hasNext() {
        if (foundCount >= size) {
            return false;
        }
        while (currentIndex < capacidad && (tabla[currentIndex] == null || tabla[currentIndex] == tombstone)) {
            currentIndex++;
        }
        return currentIndex < capacidad;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No hay más elementos en la iteración.");
        }
        T value = tabla[currentIndex].getValor();
        foundCount++;
        currentIndex++;
        return value;
    }
}