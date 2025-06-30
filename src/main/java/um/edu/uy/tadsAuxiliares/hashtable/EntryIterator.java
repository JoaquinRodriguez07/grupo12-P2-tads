package um.edu.uy.tadsAuxiliares.hashtable;

import java.util.Iterator;
import java.util.NoSuchElementException;

class EntryIterator<K extends Comparable<K>, T> implements Iterator<Objeto<K, T>> {

    private final Objeto<K, T>[] tabla;
    private final int capacidad;
    private final int size;
    private final Objeto<K, T> tombstone;
    private int indiceActual;
    private int contadorEncontrados;

    public EntryIterator(Objeto<K, T>[] tabla, int capacidad, int size, Objeto<K, T> tombstone) {
        this.tabla = tabla;
        this.capacidad = capacidad;
        this.size = size;
        this.tombstone = tombstone;
        this.indiceActual = 0;
        this.contadorEncontrados = 0;
    }

    @Override
    public boolean hasNext() {
        if (contadorEncontrados >= size) return false;
        while (indiceActual < capacidad && (tabla[indiceActual] == null || tabla[indiceActual] == tombstone)) {
            indiceActual++;
        }
        return indiceActual < capacidad;
    }

    @Override
    public Objeto<K, T> next() {
        if (!hasNext()) throw new NoSuchElementException("No hay más entradas en la iteración.");
        Objeto<K, T> entry = tabla[indiceActual];
        contadorEncontrados++;
        indiceActual++;
        return entry;
    }
}