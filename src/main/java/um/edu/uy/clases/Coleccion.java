package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

@Getter
@Setter
public class Coleccion implements Comparable<Coleccion> {
    private int id;
    private String titulo;
    private int cantidadPeliculas;
    private HashTable<Integer, Pelicula> peliculas;
    private double ingresos;

    public Coleccion(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.cantidadPeliculas = 0;
        peliculas = new HashCerradaLineal<>();
        this.ingresos = 0;
    }

    @Override
    public int compareTo(Coleccion o) {
        return 0;
    }
}
