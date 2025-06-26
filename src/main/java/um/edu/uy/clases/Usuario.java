package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.clases.Calificacion.*;

@Getter
@Setter
public class Usuario implements Comparable<Usuario> {
    private int id;
    private MiLista<Calificacion> calificacionesDelUsuario;

    public Usuario(int id) {
        this.id = id;
        this.calificacionesDelUsuario = new MiArrayList<>();
    }

    @Override
    public int compareTo(Usuario o) { //arreglar este compareTo
        return 0;
    }
}
