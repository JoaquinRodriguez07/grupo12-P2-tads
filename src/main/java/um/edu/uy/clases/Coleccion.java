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
    private int cantidadPeliculas; // Este campo necesitará ser actualizado si se usa
    private HashTable<Integer, Pelicula> peliculas;

    public Coleccion(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.peliculas = new HashCerradaLineal<>(10);
    }

    @Override
    public int compareTo(Coleccion o) {
        return Integer.compare(this.id, o.getId());
    }
}
