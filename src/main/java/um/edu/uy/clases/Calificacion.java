package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Calificacion {
    private Usuario usuario;
    private String idPelicula;
    private double puntaje;
    private Date fecha;

    public Calificacion(Usuario usuario, String idPelicula, double puntaje, Date fecha) {
        this.usuario = usuario;
        this.idPelicula = idPelicula;
        this.puntaje = puntaje;
        this.fecha = fecha;
    }

}
