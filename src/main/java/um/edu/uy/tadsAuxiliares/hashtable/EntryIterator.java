package um.edu.uy.tadsAuxiliares.hashtable;

import java.util.Iterator;
import java.util.NoSuchElementException;

class EntryIterator<K extends Comparable<K>, T> implements Iterator<Objeto<K, T>> {
    // ... (código idéntico a KeyIterator, pero devuelve el Objeto<K, T> completo)
    private final Objeto<K, T>[] tabla;
    private final int capacidad;
    private final int size;
    private final Objeto<K, T> tombstone;
    private int currentIndex;
    private int foundCount;

    public EntryIterator(Objeto<K, T>[] tabla, int capacidad, int size, Objeto<K, T> tombstone) {
        this.tabla = tabla;
        this.capacidad = capacidad;
        this.size = size;
        this.tombstone = tombstone;
        this.currentIndex = 0;
        this.foundCount = 0;
    }

    @Override
    public boolean hasNext() {
        if (foundCount >= size) return false;
        while (currentIndex < capacidad && (tabla[currentIndex] == null || tabla[currentIndex] == tombstone)) {
            currentIndex++;
        }
        return currentIndex < capacidad;
    }

    @Override
    public Objeto<K, T> next() {
        if (!hasNext()) throw new NoSuchElementException("No hay más entradas en la iteración.");
        Objeto<K, T> entry = tabla[currentIndex];
        foundCount++;
        currentIndex++;
        return entry;
    }
}