package um.edu.uy.clases;

import um.edu.uy.clases.Calificacion;
import um.edu.uy.clases.DataParaCargadores;
import um.edu.uy.clases.Pelicula;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

public class Sistema {

    private final DataParaCargadores datos;


    public Sistema(DataParaCargadores datos) {
        this.datos = datos;
    }


    public void funcion1() {
        System.out.println("\n--- Ejecutando Consulta 1: Top 5 Películas por Idioma ---");
        long startTime = System.currentTimeMillis();

        // Obtener la lista de todas las películas. Es necesario hacer un "cast" a la clase concreta
        // para poder usar métodos específicos como .getValores() que no están en la interfaz HashTable.
        MiLista<Pelicula> todasLasPeliculas = datos.getPeliculas().getValores();

        // Crear listas para cada idioma
        MiArrayList<Pelicula> peliculasEs = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasEn = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasFr = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasIt = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasPt = new MiArrayList<>();

        // Clasificar cada película en su lista de idioma correspondiente
        for (int i = 0; i < todasLasPeliculas.size(); i++) {
            Pelicula pelicula = todasLasPeliculas.get(i);
            String idioma = pelicula.getIdiomaOriginal();

            if (idioma != null) {
                switch (idioma) {
                    case "es":
                        peliculasEs.add(pelicula);
                        break;
                    case "en":
                        peliculasEn.add(pelicula);
                        break;
                    case "fr":
                        peliculasFr.add(pelicula);
                        break;
                    case "it":
                        peliculasIt.add(pelicula);
                        break;
                    case "pt":
                        peliculasPt.add(pelicula);
                        break;
                }
            }
        }

        // Imprimir el top 5 para cada idioma
        System.out.println("\n-- Top 5 Español --");
        imprimirTop5PeliculasPorCalificaciones(peliculasEs);

        System.out.println("\n-- Top 5 Inglés --");
        imprimirTop5PeliculasPorCalificaciones(peliculasEn);

        System.out.println("\n-- Top 5 Francés --");
        imprimirTop5PeliculasPorCalificaciones(peliculasFr);

        System.out.println("\n-- Top 5 Italiano --");
        imprimirTop5PeliculasPorCalificaciones(peliculasIt);

        System.out.println("\n-- Top 5 Portugués --");
        imprimirTop5PeliculasPorCalificaciones(peliculasPt);

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (endTime - startTime) + "ms");
    }


    private void imprimirTop5PeliculasPorCalificaciones(MiArrayList<Pelicula> peliculas) {
        if (peliculas.isEmpty()) {
            System.out.println("No se encontraron películas para este idioma.");
            return;
        }

        HashTable<Integer, MiLista<Calificacion>> calificacionesPorPelicula = datos.getCalificacionesPorPelicula();

        // Ordenar la lista usando BubbleSort basado en el número de calificaciones
        for (int i = 0; i < peliculas.size() - 1; i++) {
            for (int j = 0; j < peliculas.size() - i - 1; j++) {
                Pelicula p1 = peliculas.get(j);
                Pelicula p2 = peliculas.get(j + 1);

                MiLista<Calificacion> calif1 = calificacionesPorPelicula.obtener(p1.getId());
                int nroCalif1;
                if (calif1 != null) {
                    nroCalif1 = calif1.size();
                } else {
                    nroCalif1 = 0;
                }

                MiLista<Calificacion> calif2 = calificacionesPorPelicula.obtener(p2.getId());
                int nroCalif2;
                if (calif2 != null) {
                    nroCalif2 = calif2.size();
                } else {
                    nroCalif2 = 0;
                }

                if (nroCalif1 < nroCalif2) {
                    // Intercambiar elementos
                    peliculas.set(j, p2);
                    peliculas.set(j + 1, p1);
                }
            }
        }

        // Imprimir el top 5
        int limite = Math.min(5, peliculas.size());
        for (int i = 0; i < limite; i++) {
            Pelicula p = peliculas.get(i);
            MiLista<Calificacion> calif = calificacionesPorPelicula.obtener(p.getId());

            int nroCalif;
            if (calif != null) {
                nroCalif = calif.size();
            } else {
                nroCalif = 0;
            }

            // Formato: Id de la película, Título de la película, Total de evaluaciones, Idioma Original
            System.out.println(p.getId() + ", " + p.getTitulo() + ", " + nroCalif + ", " + p.getIdiomaOriginal());
        }
    }


    public void funcion2() {
        System.out.println("\n--- Ejecutando Consulta 2: Top 10 Películas con Mejor Calificación Media ---");
        long startTime = System.currentTimeMillis();

        MiLista<Pelicula> peliculasConCalificaciones = new MiArrayList<>();
        MiLista<Integer> idsPeliculasConCalif = ((HashCerradaLineal<Integer, MiLista<Calificacion>>) datos.getCalificacionesPorPelicula()).getClaves();

        for(int i = 0; i < idsPeliculasConCalif.size(); i++) {
            Integer idPelicula = idsPeliculasConCalif.get(i);
            MiLista<Calificacion> calificaciones = datos.getCalificacionesPorPelicula().obtener(idPelicula);

            // Requisito: Solo considerar películas con más de 100 calificaciones
            if (calificaciones != null && calificaciones.size() > 100) {
                Pelicula p = datos.getPeliculas().obtener(idPelicula);
                if (p != null) {
                    peliculasConCalificaciones.add(p);
                }
            }
        }

        // Ordenar la lista usando BubbleSort basado en la calificación promedio
        for (int i = 0; i < peliculasConCalificaciones.size() - 1; i++) {
            for (int j = 0; j < peliculasConCalificaciones.size() - i - 1; j++) {
                Pelicula p1 = peliculasConCalificaciones.get(j);
                Pelicula p2 = peliculasConCalificaciones.get(j + 1);

                if (calcularPromedioCalificacion(p1) < calcularPromedioCalificacion(p2)) {
                    // Intercambiar elementos
                    peliculasConCalificaciones.set(j, p2);
                    peliculasConCalificaciones.set(j + 1, p1);
                }
            }
        }

        // Imprimir el top 10
        System.out.println("\n-- Top 10 Películas por Calificación Media --");
        int limite = Math.min(10, peliculasConCalificaciones.size());
        for (int i = 0; i < limite; i++) {
            Pelicula pelicula = peliculasConCalificaciones.get(i);
            // Formato: Id de la película, Título de la película, Calificación media
            System.out.printf("%d, %s, %.2f\n", pelicula.getId(), pelicula.getTitulo(), calcularPromedioCalificacion(pelicula));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (endTime - startTime) + "ms");
    }


    private double calcularPromedioCalificacion(Pelicula pelicula) {
        MiLista<Calificacion> calificaciones = datos.getCalificacionesPorPelicula().obtener(pelicula.getId());
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0.0;
        }

        double sumaTotal = 0;
        for (int i = 0; i < calificaciones.size(); i++) {
            sumaTotal += calificaciones.get(i).getPuntaje();
        }

        return sumaTotal / calificaciones.size();
    }



    public void funcion3() {
        System.out.println("\n--- Ejecutando Consulta 3: Top 5 Colecciones por Ingresos ---");
        long startTime = System.currentTimeMillis();

        MiLista<Coleccion> todasLasColecciones = datos.getColecciones().getValores();

        // 1. Calcular el total de ingresos para cada colección
        for (int i = 0; i < todasLasColecciones.size(); i++) {
            Coleccion coleccion = todasLasColecciones.get(i);
            double totalIngresos = 0;

            MiLista<Pelicula> peliculasDeLaColeccion = coleccion.getPeliculas().getValores();
            for (int j = 0; j < peliculasDeLaColeccion.size(); j++) {
                totalIngresos += peliculasDeLaColeccion.get(j).getIngresos();
            }
            coleccion.setIngresos(totalIngresos); // Guardamos el total calculado en el objeto
        }

        // 2. Ordenar las colecciones por ingresos (de mayor a menor)
        for (int i = 0; i < todasLasColecciones.size() - 1; i++) {
            for (int j = 0; j < todasLasColecciones.size() - i - 1; j++) {
                Coleccion c1 = todasLasColecciones.get(j);
                Coleccion c2 = todasLasColecciones.get(j + 1);
                if (c1.getIngresos() < c2.getIngresos()) {
                    todasLasColecciones.set(j, c2);
                    todasLasColecciones.set(j + 1, c1);
                }
            }
        }

        // 3. Imprimir el Top 5
        System.out.println("\n-- Top 5 Colecciones por Ingresos Generados --");
        int limite = Math.min(5, todasLasColecciones.size());
        for (int i = 0; i < limite; i++) {
            Coleccion coleccion = todasLasColecciones.get(i);
            MiLista<Integer> idsPeliculas = ((HashCerradaLineal<Integer, Pelicula>) coleccion.getPeliculas()).getClaves();

            // Construir el string de IDs
            StringBuilder idsStr = new StringBuilder("[");
            for (int k = 0; k < idsPeliculas.size(); k++) {
                idsStr.append(idsPeliculas.get(k));
                if (k < idsPeliculas.size() - 1) {
                    idsStr.append(",");
                }
            }
            idsStr.append("]");

            // Formato: Id, Título, Cantidad de películas, [IDs], Ingresos
            System.out.printf("%d, %s, %d, %s, %.2f\n",
                    coleccion.getId(),
                    coleccion.getTitulo(),
                    idsPeliculas.size(),
                    idsStr.toString(),
                    coleccion.getIngresos());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (endTime - startTime) + "ms");
    }


    public void funcion4() {
        System.out.println("\n--- Ejecutando Consulta 4: Top 10 Directores por Calificación (Mediana) ---");
        long startTime = System.currentTimeMillis();

        MiLista<Persona> todosLasPersonas = datos.getPersonas().getValores();
        MiArrayList<Persona> directoresCalificados = new MiArrayList<>();

        // 1. Filtrar directores y calcular su mediana
        for (int i = 0; i < todosLasPersonas.size(); i++) {
            Persona persona = todosLasPersonas.get(i);

            // Requisito 1: Ser director y tener más de una película
            if (persona.isDirector() && persona.getIdsPeliculasDirigidas().size() > 1) {
                MiLista<Integer> idsPeliculasDirigidas = persona.getIdsPeliculasDirigidas();
                MiArrayList<Double> calificacionesTotales = new MiArrayList<>();

                // Recolectar todas las calificaciones de todas sus películas
                for (int j = 0; j < idsPeliculasDirigidas.size(); j++) {
                    Integer idPelicula = idsPeliculasDirigidas.get(j);
                    MiLista<Calificacion> califDePelicula = datos.getCalificacionesPorPelicula().obtener(idPelicula);
                    if (califDePelicula != null) {
                        for (int k = 0; k < califDePelicula.size(); k++) {
                            calificacionesTotales.add(califDePelicula.get(k).getPuntaje());
                        }
                    }
                }

                // Requisito 2: Tener más de 100 evaluaciones en total
                if (calificacionesTotales.size() > 100) {
                    double mediana = calculoMediana(calificacionesTotales);
                    persona.setCalificacion(mediana); // Se usa un campo temporal en Persona para la mediana
                    directoresCalificados.add(persona);
                }
            }
        }

        // 2. Ordenar los directores por su calificación (mediana)
        for (int i = 0; i < directoresCalificados.size() - 1; i++) {
            for (int j = 0; j < directoresCalificados.size() - i - 1; j++) {
                if (directoresCalificados.get(j).getCalificacion() < directoresCalificados.get(j+1).getCalificacion()) {
                    Persona temp = directoresCalificados.get(j);
                    directoresCalificados.set(j, directoresCalificados.get(j+1));
                    directoresCalificados.set(j+1, temp);
                }
            }
        }

        // 3. Imprimir el Top 10
        System.out.println("\n-- Top 10 Directores por Calificación Mediana --");
        int limite = Math.min(10, directoresCalificados.size());
        for (int i = 0; i < limite; i++) {
            Persona director = directoresCalificados.get(i);
            // Formato: Nombre del director, Cantidad de películas, Mediana de su calificación
            System.out.printf("%s, %d, %.2f\n",
                    director.getNombre(),
                    director.getIdsPeliculasDirigidas().size(),
                    director.getCalificacion());
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Tiempo de ejecución de la consulta: " + (endTime - startTime) + "ms");

    }


    private double calculoMediana(MiArrayList<Double> calificaciones) {
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0;
        }
        // Ordenar la lista (BubbleSort)
        for (int i = 0; i < calificaciones.size() - 1; i++) {
            for (int j = 0; j < calificaciones.size() - i - 1; j++) {
                if (calificaciones.get(j) > calificaciones.get(j + 1)) {
                    double temp = calificaciones.get(j);
                    calificaciones.set(j, calificaciones.get(j + 1));
                    calificaciones.set(j + 1, temp);
                }
            }
        }

        int n = calificaciones.size();
        if (n % 2 != 0) {
            // Si es impar, la mediana es el elemento del medio
            return calificaciones.get(n / 2);
        } else {
            // Si es par, es el promedio de los dos elementos centrales
            double medio1 = calificaciones.get(n / 2 - 1);
            double medio2 = calificaciones.get(n / 2);
            return (medio1 + medio2) / 2.0;
        }
    }



}
