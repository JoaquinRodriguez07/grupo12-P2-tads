package um.edu.uy;

import um.edu.uy.clases.Peliculas;
import um.edu.uy.cargadoresDeData.CargadorDeMovies;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

public class Main {
    public static void main(String[] args) {
        CargadorDeMovies loader = new CargadorDeMovies();
        String csvFile = "movies_metadata.csv";

        System.out.println("Iniciando carga de películas...");
        long startTime = System.currentTimeMillis();

        HashTable<String, Peliculas> peliculasHash = loader.cargadorMoviesAHash(csvFile);

        long endTime = System.currentTimeMillis();
        System.out.println("--- Carga Finalizada ---");
        System.out.println("Total de películas cargadas en la tabla hash: " + peliculasHash.tamanio());
        System.out.println("Tiempo total de carga: " + (endTime - startTime) + " ms");


        String testId = "9263";
        Peliculas testMovie = peliculasHash.obtener(testId); //aca me fijo si esta

        if (testMovie != null) {
            System.out.println("\nPelícula encontrada con ID " + testId + ":");
            System.out.println(testMovie);
        } else {
            System.out.println("\nPelícula con ID " + testId + " no encontrada.");
        }

        String IdTrucho = "99999999";
        Peliculas noExisteId = peliculasHash.obtener(IdTrucho); //a ver si me tira excepcion con una que no funca

        if (noExisteId != null) {
            System.out.println("\nPelícula encontrada con ID " + IdTrucho + ":");
            System.out.println(noExisteId);
        } else {
            System.out.println("\nPelícula con ID " + IdTrucho + " no encontrada.");
        }

        System.out.println(peliculasHash);
    }
}