package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

@Getter
@Setter

public class Persona implements Comparable<Persona>{

    private int id;
    private String nombre;
    private MiLista<Integer> idsPeliculasDirigidas;
    private MiLista<Integer> idsPeliculasActuo;



    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.idsPeliculasDirigidas = new MiArrayList<>();
        this.idsPeliculasActuo = new MiArrayList<>();
    }



    public boolean isDirector() {
        return idsPeliculasDirigidas != null && !idsPeliculasDirigidas.isEmpty();
    }



    public boolean isActor() {
        return idsPeliculasActuo != null && !idsPeliculasActuo.isEmpty();
    }



    @Override
    public int compareTo(Persona otraPersona){
        return Integer.compare(this.id, otraPersona.id);
    }

}