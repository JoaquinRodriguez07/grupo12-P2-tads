package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class Calificaciones implements Comparable<Calificaciones> {
    private int usuario;
    private int idPelicula;
    private double puntaje;
    private LocalDate fecha;

    public Calificaciones(int usuario, int idPelicula, double puntaje, LocalDate fecha) {
        this.usuario = usuario;
        this.idPelicula = idPelicula;
        this.puntaje = puntaje;
        this.fecha = fecha;
    }

    @Override
    public int compareTo(Calificaciones o) { //chequear esto de aca
        return 0;
    }
}
