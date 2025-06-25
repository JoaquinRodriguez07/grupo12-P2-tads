package um.edu.uy.tadsAuxiliares.arraylist;


import um.edu.uy.clases.Calificaciones;

public class MiArrayList<T extends Comparable<T>> implements MiLista<T> {
    private T[] datos;
    private int tamanio;
    private static final int CAPACIDAD_INICIAL = 10;

    @SuppressWarnings("unchecked")
    public MiArrayList() {
        datos = (T[]) new Comparable[CAPACIDAD_INICIAL];
        tamanio = 0;
    }

    @Override
    public void add(T elemento) {
        if (tamanio == datos.length) {
            redimensionar();
        }
        datos[tamanio++] = elemento;
    }

    @Override
    public T get(int indice) {
        validarIndice(indice);
        return datos[indice];
    }

    @Override
    public void set(int indice, T elemento) {
        validarIndice(indice);
        datos[indice] = elemento;
    }

    @Override
    public T remove(int indice) {
        validarIndice(indice);
        T eliminado = datos[indice];
        for (int i = indice; i < tamanio - 1; i++) {
            datos[i] = datos[i + 1];
        }
        datos[--tamanio] = null;
        return eliminado;
    }

    @Override
    public int size() {
        return tamanio;
    }

    @Override
    public boolean isEmpty() {
        return tamanio == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < tamanio; i++) {
            datos[i] = null;
        }
        tamanio = 0;
    }

    @Override
    public boolean contains(T elemento) {
        return indexOf(elemento) != -1;
    }

    @Override
    public int indexOf(T elemento) {
        for (int i = 0; i < tamanio; i++) {
            if (datos[i].equals(elemento)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private void redimensionar() {
        T[] nuevo = (T[]) new Comparable[datos.length * 2];
        for (int i = 0; i < tamanio; i++) {
            nuevo[i] = datos[i];
        }
        datos = nuevo;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }


    @Override
    public void addAll(MiLista<T> otraLista) {
        for (int i = 0; i < otraLista.size(); i++) {
            this.add(otraLista.get(i));
        }
    }

    @Override
    public int compareTo(MiLista<Calificaciones> o) {
        return 0;
    }
}
