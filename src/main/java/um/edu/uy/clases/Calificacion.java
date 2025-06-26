package um.edu.uy.clases;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode //esto creo que nos va asolucioanr la vida para comparar cosas en las funciones pero no se
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
    public int compareTo(Calificaciones otra) {
        // Criterio de comparación:
        // 1. Primero, compara por idPelicula (el ID de la película)
        int idPeliculaComparison = Integer.compare(this.idPelicula, otra.idPelicula);
        if (idPeliculaComparison != 0) {
            return idPeliculaComparison;
        }

        // 2. Si las películas son las mismas, compara por usuario
        int usuarioComparison = Integer.compare(this.usuario, otra.usuario);
        if (usuarioComparison != 0) {
            return usuarioComparison;
        }

        // 3. Si película y usuario son los mismos, compara por fecha
        // (LocalDate tiene su propio compareTo)
        if (this.fecha != null && otra.fecha != null) {
            int fechaComparison = this.fecha.compareTo(otra.fecha);
            if (fechaComparison != 0) {
                return fechaComparison;
            }
        } else if (this.fecha == null && otra.fecha != null) {
            return -1; // Una fecha nula es "menor"
        } else if (this.fecha != null && otra.fecha == null) {
            return 1; // Una fecha nula es "menor"
        }

        // 4si todo lo anterior es igual, compara por puntaje
        return Double.compare(this.puntaje, otra.puntaje);
    }


    //LO DEJO ACA POR SI LO DE LOMBOK NO FUNCA
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Calificaciones that = (Calificaciones) o;
//        return usuario == that.usuario &&
//                idPelicula == that.idPelicula &&
//                Double.compare(puntaje, that.puntaje) == 0 &&
//                Objects.equals(fecha, that.fecha); // Objects.equals maneja nulos
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(usuario, idPelicula, puntaje, fecha);
//    }

}
