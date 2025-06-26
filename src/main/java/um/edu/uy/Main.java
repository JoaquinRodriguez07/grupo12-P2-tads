package um.edu.uy;

import um.edu.uy.clases.Calificaciones;
import um.edu.uy.clases.Peliculas;
import um.edu.uy.clases.Persona;
import um.edu.uy.cargadoresDeData.CargadorDeCalificaciones;
import um.edu.uy.cargadoresDeData.CargadorDeMovies;
import um.edu.uy.cargadoresDeData.CargadorDeCredits;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal; // Necesario para el cast
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean cierre = true; // Controla el bucle del menú principal

        // Declarar las tablas hash fuera del switch
        HashTable<Integer, Peliculas> peliculasHash = null;
        HashTable<Integer, MiLista<Calificaciones>> calificacionesPorPeliculaHash = null;
        HashTable<Integer, Persona> personasHash = null;

        // Banderas y variables para el estado de la carga
        boolean datosCargados = false;
        long tiempoTotalCargaMs = 0;

        while (cierre) {
            System.out.println("\nMenu principal");
            System.out.println("       Seleccione la opción que desee:");
            System.out.println("    1. Carga de datos");
            System.out.println("    2. Ejecutar consultas");
            System.out.println("    3. Salir");
            System.out.print("Ingrese su opción: ");
            String respuesta = scanner.nextLine().trim();

            switch (respuesta) {
                case "1":
                    if (datosCargados) {
                        System.out.println("¡Los datos ya fueron cargados previamente! Tiempo de la última carga: " + tiempoTotalCargaMs + " ms.");
                    } else {
                        System.out.println("Iniciando carga de datos...");
                        long startTime = System.currentTimeMillis();

                        // --- Carga de Películas ---
                        CargadorDeMovies moviesLoader = new CargadorDeMovies();
                        String moviesCsvFile = "movies_metadata.csv"; // <<--- ¡ACTUALIZA ESTA RUTA!

                        try {
                            peliculasHash = moviesLoader.cargadorMoviesAHash(moviesCsvFile);
                            System.out.println("  - Películas únicas cargadas: " + peliculasHash.tamanio() + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar películas: " + e.getMessage());
                            datosCargados = false;
                            break;
                        }

                        // --- Carga de Calificaciones ---
                        CargadorDeCalificaciones calificacionesLoader = new CargadorDeCalificaciones();
                        String ratingsCsvFile = "ratings_small.csv"; // <<--- ¡ACTUALIZA ESTA RUTA!

                        try {
                            calificacionesPorPeliculaHash = calificacionesLoader.cargarCalificacionesAHash(ratingsCsvFile);
                            System.out.println("  - Películas con calificaciones cargadas: " + calificacionesPorPeliculaHash.tamanio() + ".");

                            long totalCalificacionesIndividuales = 0;
                            HashCerradaLineal<Integer, MiLista<Calificaciones>> tempCalificacionesHash =
                                    (HashCerradaLineal<Integer, MiLista<Calificaciones>>) calificacionesPorPeliculaHash;
                            MiLista<MiLista<Calificaciones>> todasLasListasDeCalificaciones = tempCalificacionesHash.getValores();
                            for (int i = 0; i < todasLasListasDeCalificaciones.size(); i++) {
                                totalCalificacionesIndividuales += todasLasListasDeCalificaciones.get(i).size();
                            }
                            System.out.println("  - Total de calificaciones individuales cargadas: " + totalCalificacionesIndividuales + ".");

                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar calificaciones: " + e.getMessage());
                            peliculasHash = null;
                            datosCargados = false;
                            break;
                        }

                        // --- Carga de Créditos (Actores y Directores en Persona) ---
                        CargadorDeCredits creditsLoader = new CargadorDeCredits();
                        String creditsCsvFile = "credits.csv"; // <<--- ¡ACTUALIZA ESTA RUTA!

                        try {
                            // ** CAMBIO REALIZADO AQUÍ **
                            // 1. Se inicializa la tabla hash para las personas.
                            personasHash = new HashCerradaLineal<>(263471); // Capacidad inicial grande y prima

                            // 2. Se pasa la tabla hash al método para que la pueble.
                            creditsLoader.cargarCredits(creditsCsvFile, personasHash);

                            System.out.println("  - Personas únicas (Actores/Directores) cargadas: " + personasHash.tamanio() + ".");

                            // --- Contar cuántos son Actores y Directores ---
                            long totalActores = 0;
                            long totalDirectores = 0;

                            HashCerradaLineal<Integer, Persona> tempPersonasHash =
                                    (HashCerradaLineal<Integer, Persona>) personasHash;
                            MiLista<Persona> todasLasPersonas = tempPersonasHash.getValores();

                            for (int i = 0; i < todasLasPersonas.size(); i++) {
                                Persona p = todasLasPersonas.get(i);
                                if (p.isActor()) {
                                    totalActores++;
                                }
                                if (p.isDirector()) {
                                    totalDirectores++;
                                }
                            }
                            System.out.println("  - Total de actores cargados: " + totalActores + ".");
                            System.out.println("  - Total de directores cargados: " + totalDirectores + ".");

                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar créditos: " + e.getMessage());
                            peliculasHash = null;
                            calificacionesPorPeliculaHash = null;
                            personasHash = null;
                            datosCargados = false;
                            break;
                        }

                        long endTime = System.currentTimeMillis();
                        tiempoTotalCargaMs = endTime - startTime;
                        datosCargados = true; // Éxito en todas las cargas

                        System.out.println("Carga de datos exitosa. Tiempo total de ejecución: " + tiempoTotalCargaMs + " ms.");
                    }
                    break; // Fin del case "1"

                case "2":
                    if (!datosCargados) {
                        System.out.println("ERROR: Los datos no han sido cargados. Por favor, seleccione la opción 1 primero.");
                        break;
                    }

                    boolean cierre2 = true;
                    while (cierre2) {
                        System.out.println("\n--- Menú de Consultas ---");
                        System.out.println("1. Consulta 1 (No implementada)");
                        System.out.println("7. Volver al menú principal.");
                        System.out.print("Ingrese su opción: ");
                        String respuesta2 = scanner.nextLine().trim();

                        switch (respuesta2) {
                            case "7":
                                cierre2 = false;
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opción no válida o no implementada. Intente de nuevo.");
                                break;
                        }
                    }
                    break; // Fin del case "2"

                case "3":
                    cierre = false;
                    System.out.println("Saliendo del programa. ¡Adiós!");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
        scanner.close();
    }
}
