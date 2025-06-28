// Archivo: um/edu/uy/tadsAuxiliares/arraylist/MiLista.java

package um.edu.uy.tadsAuxiliares.arraylist;

import um.edu.uy.clases.Calificacion;

// <-- CAMBIO: Hacemos que la interfaz extienda Iterable<T>.
// Esto nos permitirá usar cualquier MiLista en un bucle for-each.
public interface MiLista<T> extends Iterable<T> {
    void add(T elemento);
    T get(int indice);
    void set(int indice, T elemento);
    T remove(int indice);
    int size();
    boolean isEmpty();
    void clear();
    boolean contains(T elemento);
    int indexOf(T elemento);
    void addAll(MiLista<T> otraLista);
}