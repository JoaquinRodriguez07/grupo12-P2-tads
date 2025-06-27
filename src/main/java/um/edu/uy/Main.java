package um.edu.uy;

import um.edu.uy.clases.*;
import um.edu.uy.cargadoresDeData.CargadorDeCalificaciones;
import um.edu.uy.cargadoresDeData.CargadorDeMovies;
import um.edu.uy.cargadoresDeData.CargadorDeCredits;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de carga de datos (con optimización para función 5)...");

        // 1. Se inicializa el contenedor de datos, incluyendo la capacidad para la nueva tabla
        // Valores: usuarios, peliculas, personas, colecciones, peliculasConRatings
        DataParaCargadores datos = new DataParaCargadores(180001, 64801, 305009, 60007, 27001);

        long startTime = System.currentTimeMillis();

        // --- Carga de Películas y Colecciones ---
        try {
            new CargadorDeMovies().cargadorMoviesAHash("movies_metadata.csv", datos.getPeliculas(), datos.getColecciones());
            System.out.println("  [OK] Carga de Películas y Colecciones finalizada.");
        } catch (Exception e) {
            System.out.println("  [ERROR] al cargar películas: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // --- Carga de Calificaciones y Usuarios ---
        try {
            new CargadorDeCalificaciones().cargarCalificaciones("ratings_1mm.csv", datos.getCalificaciones(), datos.getCalificacionesPorPelicula(), datos.getUsuarios());
            System.out.println("  [OK] Carga de Calificaciones y Usuarios finalizada.");
        } catch (Exception e) {
            System.out.println("  [ERROR] al cargar calificaciones: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // --- Carga de Créditos (Actores/Directores Y el nuevo índice) ---
        try {
            // Se le pasa la nueva tabla actoresPorPelicula para que también la llene
            new CargadorDeCredits().cargarCredits("credits.csv", datos.getPersonas(), datos.getActoresPorPelicula());
            System.out.println("  [OK] Carga de Créditos (con índice de actores) finalizada.");
        } catch (Exception e) {
            System.out.println("  [ERROR] al cargar créditos: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("\n--- Resumen de la Carga ---");
        System.out.println("Tiempo total de ejecución: " + totalTime + " ms.");
        System.out.println("---------------------------");
        System.out.println("Total de Películas: " + datos.getPeliculas().tamanio());
        System.out.println("Total de Colecciones: " + datos.getColecciones().tamanio());
        System.out.println("Total de Personas: " + datos.getPersonas().tamanio());
        System.out.println("Total de Usuarios: " + datos.getUsuarios().tamanio());
        System.out.println("Total de Calificaciones: " + datos.getCalificaciones().size());
        System.out.println("Total de Películas con Calificaciones: " + datos.getCalificacionesPorPelicula().tamanio());
        System.out.println("Total de Películas con Actores (nuevo índice): " + datos.getActoresPorPelicula().tamanio());
    }
}
