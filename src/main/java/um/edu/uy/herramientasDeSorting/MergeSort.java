package um.edu.uy.herramientasDeSorting;

import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

import java.util.Comparator;

public class MergeSort { //reutilizado de mi practico


    public static <T> void sort(MiLista<T> lista, Comparator<T> comparador) {
        if (lista == null || lista.size() <= 1) {
            return; // No necesita ordenarse
        }
        mergeSort(lista, comparador);
    }

    private static <T> void mergeSort(MiLista<T> lista, Comparator<T> comparador) {
        if (lista.size() <= 1) {
            return;
        }

        int mid = lista.size() / 2;
        MiLista<T> izquierda = new MiArrayList<>();
        MiLista<T> derecha = new MiArrayList<>();

        for (int i = 0; i < mid; i++) {
            izquierda.add(lista.get(i));
        }
        for (int i = mid; i < lista.size(); i++) {
            derecha.add(lista.get(i));
        }

        mergeSort(izquierda, comparador);
        mergeSort(derecha, comparador);

        merge(lista, izquierda, derecha, comparador);
    }

    private static <T> void merge(MiLista<T> lista, MiLista<T> izquierda, MiLista<T> derecha, Comparator<T> comparador) {
        int i = 0; // Índice para la sublista izquierda
        int j = 0; // Índice para la sublista derecha
        int k = 0; // Índice para la lista principal

        while (i < izquierda.size() && j < derecha.size()) {
            // Se usa el comparador para decidir el orden
            if (comparador.compare(izquierda.get(i), derecha.get(j)) <= 0) {
                lista.set(k++, izquierda.get(i++));
            } else {
                lista.set(k++, derecha.get(j++));
            }
        }

        while (i < izquierda.size()) {
            lista.set(k++, izquierda.get(i++));
        }

        while (j < derecha.size()) {
            lista.set(k++, derecha.get(j++));
        }
    }


}
