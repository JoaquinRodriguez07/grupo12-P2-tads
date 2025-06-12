package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

@Getter
@Setter
public class Persona {
    private String nombre;
    private MiLista rol;

    public Persona(String nombre) {
        this.nombre = nombre;
        this.rol = new MiArrayList();
    }
}
