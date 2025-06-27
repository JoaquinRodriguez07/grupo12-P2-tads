package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

@Getter
@Setter
public class Coleccion {
    private String id;
    private String titulo;
    private int cantidadPeliculas;
    private MiLista<Peliculas> peliculas;
    private double ingresos;

    public Coleccion(String id, String titulo, int cantidadPeliculas, MiLista<Peliculas> peliculas, double ingresos) {
        this.id = id;
        this.titulo = titulo;
        this.cantidadPeliculas = cantidadPeliculas;
        this.peliculas = peliculas;
        this.ingresos = ingresos;
    }
}
