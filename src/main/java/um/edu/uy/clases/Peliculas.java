package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Peliculas {
    private String id;
    private String titulo;
    private String idiomaOriginal;
    private String coleccion;
    private double ingresos;

    public Peliculas(String id, String titulo, String idiomaOriginal, String coleccion, double ingresos) {
        this.id = id;
        this.titulo = titulo;
        this.idiomaOriginal = idiomaOriginal;
        this.coleccion = coleccion;
        this.ingresos = ingresos;
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
}