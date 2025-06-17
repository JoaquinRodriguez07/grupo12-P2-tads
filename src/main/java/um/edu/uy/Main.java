package um.edu.uy;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean cierre = true;

        while (cierre) {
            //renzo me dijo que lo haga lo mas parecido... puse los mismos espacios por si acaso
            System.out.println("Menu principal");
            System.out.println("       Seleccione la opción que desee:");
            System.out.println("    1. Carga de datos");
            System.out.println("    2. Ejecutar consultas");
            System.out.println("    3. Salir");
            String respuesta = scanner.nextLine(); //esto hace que escribas en la siguiente linea
            switch (respuesta) {
                case "1":
                    //aca iria lo de cargar los datos. mientras tanto voy a poner un print para saber que funciona
                    System.out.println("Carga de datos exitosa, tiempo de ejecución de la carga: <aca va el tiempo de ejecución>");

                    break;

                case "2":
                    boolean cierre2 = true;
                    while (cierre2) {
                        System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
                        System.out.println("2. Top 10 de las películas que mejor calificación media tienen por parte de los usuarios.");
                        System.out.println("3. Top 5 de las colecciones que más ingresos generaron.");
                        System.out.println("4. Top 10 de los directores que mejor calificación tienen.");
                        System.out.println("5. Actor con más calificaciones recibidas en cada mes del año.");
                        System.out.println("6. Usuarios con más calificaciones por género");
                        System.out.println("7. Salir");
                        String respuesta2 = scanner.nextLine();
                        switch (respuesta2) {
                            case "1":
                                //funcion. de mientras pongo algo.
                                System.out.println("funcion 1");

                                break;

                            case "2":
                                //funcion 2. de mientras pongo algo.
                                System.out.println("funcion 2");

                                break;

                            case "3":
                                //funcion 3. de mientras pongo algo.
                                System.out.println("funcion 3");

                                break;

                            case "4":
                                //funcion 4. de mientras pongo algo.
                                System.out.println("funcion 4");

                                break;

                            case "5":
                                //funcion 5. de mientras pongo algo.
                                System.out.println("funcion 5");

                                break;

                            case "6":
                                //funcion 6. de mientras pongo algo.
                                System.out.println("funcion 6");

                                break;

                            case "7":
                                //preguntarle a renzo si tiene que terminar este menu y volver al anterior o terminar ambos menus.
                                //por ahora lo deje en que terminara este menu y regresara al otro.
                                cierre2 = false;
                                break;

                            default:
                                System.out.println("Opción no valida. Intente de nuevo.");
                                break;
                        }
                    }

                    break;

                case "3":
                    cierre = false;
                    break;

                default:
                    System.out.println("Opción no valida. Intente de nuevo.");
                    break;
            }
        }
    }
}