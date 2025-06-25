package um.edu.uy;

import um.edu.uy.clases.Calificaciones;
import um.edu.uy.clases.Peliculas;
import um.edu.uy.cargadoresDeData.CargadorDeCalificaciones;
import um.edu.uy.cargadoresDeData.CargadorDeMovies;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal; // Necesario para el cast
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean cierre = true;

        HashTable<Integer, Peliculas> peliculasHash = null;
        HashTable<Integer, MiLista<Calificaciones>> calificacionesPorPeliculaHash = null;

        boolean datosCargados = false;
        long tiempoTotalCargaMs = 0;

        while (cierre) {
            System.out.println("Menu principal");
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

                        //Carga de Películas
                        CargadorDeMovies moviesLoader = new CargadorDeMovies();
                        String moviesCsvFile = "movies_metadata.csv";

                        try {
                            peliculasHash = moviesLoader.cargadorMoviesAHash(moviesCsvFile);
                            System.out.println("  - Películas únicas cargadas: " + peliculasHash.tamanio() + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar películas: " + e.getMessage()); // Usar System.out
                            e.printStackTrace();
                            datosCargados = false;
                            break;
                        }

                        // --- Carga de Calificaciones ---
                        CargadorDeCalificaciones calificacionesLoader = new CargadorDeCalificaciones();
                        String ratingsCsvFile = "ratings_1mm.csv";

                        try {
                            calificacionesPorPeliculaHash = calificacionesLoader.cargarCalificacionesAHash(ratingsCsvFile);
                            System.out.println("  - Películas con calificaciones cargadas: " + calificacionesPorPeliculaHash.tamanio() + ".");

                            // --- Calcular y mostrar el total de calificaciones individuales ---
                            long totalCalificacionesIndividuales = 0;
                            // Necesitamos hacer un cast a HashCerradaLineal para acceder a getValores()
                            HashCerradaLineal<Integer, MiLista<Calificaciones>> tempCalificacionesHash =
                                    (HashCerradaLineal<Integer, MiLista<Calificaciones>>) calificacionesPorPeliculaHash;

                            // getValores() devuelve una MiLista<MiLista<Calificaciones>>
                            MiLista<MiLista<Calificaciones>> todasLasListasDeCalificaciones = tempCalificacionesHash.getValores();

                            // Iterar sobre cada MiLista<Calificaciones> y sumar sus tamaños
                            for (int i = 0; i < todasLasListasDeCalificaciones.size(); i++) {
                                MiLista<Calificaciones> listaDeCalificacionesPorPelicula = todasLasListasDeCalificaciones.get(i);
                                totalCalificacionesIndividuales += listaDeCalificacionesPorPelicula.size();
                            }
                            System.out.println("  - Total de calificaciones individuales cargadas: " + totalCalificacionesIndividuales + ".");

                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar calificaciones: " + e.getMessage()); // Usar System.out
                            e.printStackTrace();
                            peliculasHash = null;
                            datosCargados = false;
                            break;
                        }

                        long endTime = System.currentTimeMillis();
                        tiempoTotalCargaMs = endTime - startTime;
                        datosCargados = true;

                        System.out.println("Carga de datos exitosa. Tiempo total de ejecución: " + tiempoTotalCargaMs + " ms.");
                    }
                    break;

                case "2":
                    if (!datosCargados) {
                        System.out.println("ERROR: Los datos no han sido cargados. Por favor, seleccione la opción 1 primero.");
                        break;
                    }

                    boolean cierre2 = true;
                    while (cierre2) {
                        System.out.println("\n--- Menú de Consultas ---");
                        System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
                        //resto de las opciones de consulta
                        System.out.println("7. Volver al menú principal.");
                        System.out.print("Ingrese su opción: ");
                        String respuesta2 = scanner.nextLine().trim();

                        switch (respuesta2) {
                            case "7":
                                cierre2 = false;
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opción no válida. Intente de nuevo.");
                                break;
                        }
                    }
                    break;

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