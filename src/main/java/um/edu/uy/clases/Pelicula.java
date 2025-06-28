package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;


@Getter
@Setter
public class Pelicula implements Comparable<Pelicula> {
    private int id;
    private String titulo;
    private String idiomaOriginal;
    private String coleccion;
    private double ingresos;
    private MiLista<String> generos;
    private int conteoCalificaciones;
    private double calificacionPromedio;



    public Pelicula(int id, String titulo, String idiomaOriginal, String coleccion, double ingresos) {
        this.id = id;
        this.titulo = titulo;
        this.idiomaOriginal = idiomaOriginal;
        this.coleccion = coleccion;
        this.ingresos = ingresos;
        this.generos = new MiArrayList<>();
        this.conteoCalificaciones = 0;
        this.calificacionPromedio = 0.0;

    }

    @Override
    public String toString() {
        return "Peliculas{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", idiomaOriginal='" + idiomaOriginal + '\'' +
                ", coleccion='" + coleccion + '\'' +
                ", ingresos=" + ingresos +
                '}';
    }

    @Override
    public int compareTo(Pelicula o) {
        return 0;
    }
}