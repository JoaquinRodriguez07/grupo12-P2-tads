package um.edu.uy.tadsAuxiliares.arraylist;

import um.edu.uy.clases.Calificacion;

public interface MiLista<T> extends Comparable<MiLista<Calificacion>> {
    void add(T elemento);
    T get(int indice);
    void set(int indice, T elemento);
    T remove(int indice);
    int size();
    boolean isEmpty();
    void clear();
    boolean contains(T elemento);
    int indexOf(T elemento);

    void addAll(MiLista<T> otraPila);
}