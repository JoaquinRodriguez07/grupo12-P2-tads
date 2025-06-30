
package um.edu.uy.tadsAuxiliares.arraylist;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MiArrayList<T> implements MiLista<T> {
    private T[] datos;
    private int tamanio;
    private static final int CAPACIDAD_INICIAL = 10;

    @SuppressWarnings("unchecked")
    public MiArrayList() {
        datos = (T[]) new Object[CAPACIDAD_INICIAL];
        tamanio = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0; // El iterador mantiene su propia posición.

            @Override
            public boolean hasNext() {
                // Hay un siguiente elemento si la posición actual es menor que el tamaño de la lista.
                return currentIndex < tamanio;
            }

            @Override
            public T next() {
                // Si no hay siguiente elemento, se lanza una excepción (comportamiento estándar).
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                // Se devuelve el dato en la posición actual y luego se incrementa la posición.
                return datos[currentIndex++];
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void redimensionar() {
        // <-- CAMBIO: Se usa Object[] aquí también para mantener la consistencia y seguridad.
        T[] nuevo = (T[]) new Object[datos.length * 2];
        for (int i = 0; i < tamanio; i++) {
            nuevo[i] = datos[i];
        }
        datos = nuevo;
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
        if (elemento == null) { // Manejo de nulos
            for (int i = 0; i < tamanio; i++) {
                if (datos[i] == null) return i;
            }
        } else {
            for (int i = 0; i < tamanio; i++) {
                if (elemento.equals(datos[i])) {
                    return i;
                }
            }
        }
        return -1;
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


}