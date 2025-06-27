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
        Scanner scanner = new Scanner(System.in);
        boolean cierre = true;

        // Se inicializa el contenedor de datos con capacidades de número primo
        DataParaCargadores datos = new DataParaCargadores(180001, 64801, 305009, 60007);

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

                        // --- Carga de Películas y Colecciones ---
                        CargadorDeMovies moviesLoader = new CargadorDeMovies();
                        String moviesCsvFile = "movies_metadata.csv";
                        try {
                            // SOLUCIÓN: Se pasan las HashTables del contenedor al método del cargador.
                            // El método es void, no devuelve nada, solo modifica las tablas.
                            moviesLoader.cargadorMoviesAHash(moviesCsvFile, datos.getPeliculas(), datos.getColecciones());
                            System.out.println("  - Películas únicas cargadas: " + datos.getPeliculas().tamanio() + ".");
                            System.out.println("  - Colecciones únicas cargadas: " + datos.getColecciones().tamanio() + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar películas: " + e.getMessage());
                            e.printStackTrace();
                            break; // Detener la carga si falla
                        }

                        // --- Carga de Calificaciones y Usuarios ---
                        CargadorDeCalificaciones calificacionesLoader = new CargadorDeCalificaciones();
                        String ratingsCsvFile = "ratings_1mm.csv"; // Usar el archivo completo
                        try {
                            // SOLUCIÓN: Se pasan la MiLista y la HashTable al método del cargador.
                            calificacionesLoader.cargarCalificaciones(ratingsCsvFile, datos.getCalificaciones(), datos.getUsuarios());
                            System.out.println("  - Total de calificaciones individuales cargadas: " + datos.getCalificaciones().size() + ".");
                            System.out.println("  - Usuarios únicos cargados: " + datos.getUsuarios().tamanio() + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar calificaciones: " + e.getMessage());
                            e.printStackTrace();
                            break;
                        }

                        // --- Carga de Actores y Directores ---
                        CargadorDeCredits creditsLoader = new CargadorDeCredits();
                        String creditsCsvFile = "credits.csv";
                        try {
                            // SOLUCIÓN: Se pasa la HashTable de personas al cargador.
                            creditsLoader.cargarCredits(creditsCsvFile, datos.getPersonas());
                            System.out.println("  - Personas únicas (Actores/Directores) cargadas: " + datos.getPersonas().tamanio() + ".");

                            // --- Conteo de Actores y Directores ---
                            long totalActores = 0;
                            long totalDirectores = 0;
                            HashCerradaLineal<Integer, Persona> tempPersonasHash = (HashCerradaLineal<Integer, Persona>) datos.getPersonas();
                            MiLista<Persona> todasLasPersonas = tempPersonasHash.getValores();

                            for (int i = 0; i < todasLasPersonas.size(); i++) {
                                Persona p = todasLasPersonas.get(i);
                                if (p.isActor()) totalActores++;
                                if (p.isDirector()) totalDirectores++;
                            }

                            System.out.println("  - Total de actores cargados: " + totalActores + ".");
                            System.out.println("  - Total de directores cargados: " + totalDirectores + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar créditos: " + e.getMessage());
                            e.printStackTrace();
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
                    // El menú de consultas se manejaría aquí...
                    System.out.println("Menú de consultas no implementado.");
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
