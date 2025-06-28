package um.edu.uy.clases;

import um.edu.uy.cargadoresDeData.CargadorDeCalificaciones;
import um.edu.uy.cargadoresDeData.CargadorDeCredits;
import um.edu.uy.cargadoresDeData.CargadorDeMovies;

import java.util.Scanner;

public class UMovie {

    private DataParaCargadores datos;
    private Sistema sistema;
    private boolean datosCargados = false;
    private long tiempoDeCarga = 0;

    public static void main(String[] args) {
        UMovie aplicacion = new UMovie();
        aplicacion.iniciar();
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMenú principal:");
            System.out.println("Seleccione la opción que desee:");
            System.out.println("1. Carga de datos");
            System.out.println("2. Ejecutar consultas");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    cargarDatos();
                    break;
                case "2":
                    menuDeConsultas(scanner);
                    break;
                case "3":
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        scanner.close();
    }

    private void cargarDatos() {
        if (datosCargados) {
            System.out.println("Los datos ya han sido cargados previamente. Tiempo: " + tiempoDeCarga + "ms");
            return;
        }

        System.out.println("Iniciando la carga de datos...");
        long startTime = System.currentTimeMillis();

        datos = new DataParaCargadores(180001, 64801, 305009, 60007, 40039);
        sistema = new Sistema(datos);

        // 2. Llamar a los cargadores para poblar las estructuras de datos
        new CargadorDeMovies().cargadorMoviesAHash("movies_metadata.csv", datos.getPeliculas(), datos.getColecciones());
        new CargadorDeCalificaciones().cargarCalificaciones("ratings_1mm.csv", datos.getCalificaciones(), datos.getCalificacionesPorPelicula(), datos.getUsuarios());
        new CargadorDeCredits().cargarCredits("credits.csv", datos.getPersonas(), datos.getActoresPorPelicula());

        long endTime = System.currentTimeMillis();
        tiempoDeCarga = endTime - startTime;
        datosCargados = true;
        System.out.println("Carga de datos exitosa, tiempo de ejecución de la carga: " + tiempoDeCarga + " ms");
    }

    private void menuDeConsultas(Scanner scanner) {
        if (!datosCargados) {
            System.out.println("ERROR: Los datos aún no han sido cargados. Por favor, seleccione la opción 1 primero.");
            return;
        }

        boolean volver = false;
        while (!volver) {
            System.out.println("\nMenú de consultas:");
            System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
            System.out.println("2. Top 10 de las películas que mejor calificación media tienen por parte de los usuarios.");
            System.out.println("3. Top 5 de las colecciones que más ingresos generaron.");
            System.out.println("4. Top 10 de los directores que mejor calificación tienen.");
            System.out.println("5. Actor con más calificaciones recibidas en cada mes del año.");
            System.out.println("6. Usuarios con más calificaciones por género");
            System.out.println("7. Salir");
            System.out.print("Ingrese su opción: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    sistema.funcion1();
                    break;
                case "2":
                    sistema.funcion2();
                    break;
                case "3":
                    sistema.funcion3();
                    break;
                case "4":
                    sistema.funcion4();
                    break;
                case "5":
                    sistema.funcion5();
                    break;
                case "6":
                    sistema.funcion6();
                    break;
                case "7":
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
    }
}
