package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

@Getter
@Setter
public class DataParaCargadores {

    private MiLista<Calificacion> calificaciones;
    private HashTable<Integer,Usuario> usuarios;
    private HashTable<Integer, Pelicula> peliculas;
    private HashTable<Integer, Persona>  personas;
    private HashTable<Integer, Coleccion> colecciones;


    public DataParaCargadores(int capacidadHashUsuarios, int capacidadHashPeliculas, int capacidadPersonas,  int capacidadColecciones) {
        this.calificaciones = new MiArrayList<>();
        this.usuarios = new HashCerradaLineal<>(capacidadHashUsuarios);
        this.peliculas = new HashCerradaLineal<>(capacidadHashPeliculas);
        this.personas = new HashCerradaLineal<>(capacidadPersonas);
        this.colecciones = new HashCerradaLineal<>(capacidadColecciones);
    }

}

