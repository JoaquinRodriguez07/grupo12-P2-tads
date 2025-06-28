// Archivo: um/edu/uy/tadsAuxiliares/hashtable/HashIterator.java

package um.edu.uy.tadsAuxiliares.hashtable;

import java.util.Iterator;
import java.util.NoSuchElementException;


class HashIterator<K extends Comparable<K>, T> implements Iterator<T> {

    // Campos para almacenar el estado de la tabla hash que se está iterando
    private final Objeto<K, T>[] tabla;
    private final int capacidad;
    private final int size;
    private final Objeto<K, T> tombstone;

    // Campos para el estado interno del iterador
    private int currentIndex;
    private int foundCount;


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