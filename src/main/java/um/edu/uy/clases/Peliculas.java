package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

@Getter
@Setter
public class Peliculas {
    private String id;
    private String titulo;
    private String idiomaOriginal;
    private String coleccion;
    private double ingresos;

    private HashTable<String, Peliculas> peliculas;
    //lista personas participantes


    public Peliculas(String id, String titulo, String idiomaOriginal, double ingresos) {
        this.id = id;
        this.titulo = titulo;
        this.idiomaOriginal = idiomaOriginal;
        this.ingresos = ingresos;
    }
}
