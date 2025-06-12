package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

@Getter
@Setter
public class Coleccion {
    private String id;
    private String titulo;
    private int cantidadPeliculas;
    private HashTable<String, Peliculas> peliculas;

    public Coleccion(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }
}
