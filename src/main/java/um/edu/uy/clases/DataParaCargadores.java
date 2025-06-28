package um.edu.uy.clases;

import lombok.Getter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

@Getter
public class DataParaCargadores {

    private final HashTable<Integer, Pelicula> peliculas;
    private final HashTable<Integer, Coleccion> colecciones;
    private final HashTable<Integer, Persona> personas;
    private final HashTable<Integer, Usuario> usuarios;
    private final MiLista<Calificacion> calificaciones;
    private final HashTable<Integer, MiLista<Calificacion>> calificacionesPorPelicula;

    private final HashTable<Integer, MiLista<Persona>> actoresPorPelicula; // lo hago para acelerar la consulta, puedo acceder mucho mas rapido.

    public DataParaCargadores(int capUsuarios, int capPeliculas, int capPersonas, int capColecciones, int capPeliculasConRatings) {
        this.usuarios = new HashCerradaLineal<>(capUsuarios);
        this.peliculas = new HashCerradaLineal<>(capPeliculas);
        this.personas = new HashCerradaLineal<>(capPersonas);
        this.colecciones = new HashCerradaLineal<>(capColecciones);
        this.calificaciones = new MiArrayList<>();
        this.calificacionesPorPelicula = new HashCerradaLineal<>(capPeliculasConRatings); //aca ajuste pq no todas las peliculas tienen calificaciones aparentemente

        this.actoresPorPelicula = new HashCerradaLineal<>(capPeliculas);  // Se inicializa la nueva tabla hash
    }
}