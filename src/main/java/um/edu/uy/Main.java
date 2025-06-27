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

        // Se inicializa el contenedor de datos
        DataParaCargadores cargadores = new DataParaCargadores(180001, 64801, 305009, 60007); // Usando primos cercanos

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
                            // SOLUCIÓN: Se pasan las HashTables del contenedor al método del cargador
                            moviesLoader.cargadorMoviesAHash(moviesCsvFile, cargadores.getPeliculas(), cargadores.getColecciones());
                            System.out.println("  - Películas únicas cargadas: " + cargadores.getPeliculas().tamanio() + ".");
                            System.out.println("  - Colecciones únicas cargadas: " + cargadores.getColecciones().tamanio() + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar películas: " + e.getMessage());
                            break; // Detener la carga si falla
                        }

                        // --- Carga de Calificaciones ---
                        CargadorDeCalificaciones calificacionesLoader = new CargadorDeCalificaciones();
                        String ratingsCsvFile = "ratings.csv"; // Usar el archivo completo
                        try {
                            // SOLUCIÓN: Se le pasa la MiLista al cargador para que la llene
                            calificacionesLoader.cargarCalificaciones(ratingsCsvFile, cargadores.getCalificaciones(), cargadores.getUsuarios());
                            System.out.println("  - Total de calificaciones individuales cargadas: " + cargadores.getCalificaciones().size() + ".");
                        } catch (Exception e) {
                            System.out.println("  - ERROR al cargar calificaciones: " + e.getMessage());
                            break;
                        }

                        // --- Carga de Actores y Directores ---
                        CargadorDeCredits creditsLoader = new CargadorDeCredits();
                        String creditsCsvFile = "credits.csv";
                        try {
                            // SOLUCIÓN: Se pasa la HashTable de personas al cargador
                            creditsLoader.cargarCredits(creditsCsvFile, cargadores.getPersonas());
                            System.out.println("  - Personas únicas (Actores/Directores) cargadas: " + cargadores.getPersonas().tamanio() + ".");

                            // --- Conteo de Actores y Directores ---
                            long totalActores = 0;
                            long totalDirectores = 0;
                            HashCerradaLineal<Integer, Persona> tempPersonasHash = (HashCerradaLineal<Integer, Persona>) cargadores.getPersonas();
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
    private static void procesarCalificacionesPorGenero(DataParaCargadores cargadores){
        MiLista<Calificacion> calificacionesList = cargadores.getCalificaciones();
        for (int i = 0; i < calificacionesList.size(); i++) {
            Calificacion calificacion = calificacionesList.get(i);

            Usuario usuario = cargadores.getUsuarios().obtener(calificacion.getUsuario());
            Pelicula pelicula = cargadores.getPeliculas().obtener(calificacion.getIdPelicula());

            if (usuario != null && pelicula != null) {
                MiLista<String> generosPelicula = pelicula.getGeneros();

                if (generosPelicula != null) {
                    for (int j = 0; j < generosPelicula.size(); j++) {
                        String genero = generosPelicula.get(j);
                        usuario.agregarCalificacionPorGenero(genero);
                    }
                }
            }
        }
    }
}
