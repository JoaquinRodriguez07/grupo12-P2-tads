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

    // Estructuras de datos principales
    private final HashTable<Integer, Pelicula> peliculas;
    private final HashTable<Integer, Coleccion> colecciones;
    private final HashTable<Integer, Persona> personas;
    private final HashTable<Integer, Usuario> usuarios;

    // --- Estructuras Clave para Calificaciones ---

    // 1. Una lista con TODAS las calificaciones. Útil para la función 5.
    private final MiLista<Calificacion> calificaciones;

    // 2. Una tabla hash para agrupar calificaciones por película. Esencial para la eficiencia de las consultas 1, 2 y 4.
    private final HashTable<Integer, MiLista<Calificacion>> calificacionesPorPelicula;

    public DataParaCargadores(int capacidadUsuarios, int capacidadPeliculas, int capacidadPersonas, int capacidadColecciones, int capacidadPeliculasConRatings) {
        this.usuarios = new HashCerradaLineal<>(capacidadUsuarios);
        this.peliculas = new HashCerradaLineal<>(capacidadPeliculas);
        this.personas = new HashCerradaLineal<>(capacidadPersonas);
        this.colecciones = new HashCerradaLineal<>(capacidadColecciones);

        this.calificaciones = new MiArrayList<>();
        this.calificacionesPorPelicula = new HashCerradaLineal<>(capacidadPeliculasConRatings);
    }
}
