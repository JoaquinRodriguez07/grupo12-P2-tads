
package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.excepciones.ElementoNoExistenteException;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.herramientasDeSorting.MergeSort;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;
import um.edu.uy.tadsAuxiliares.hashtable.Objeto;

import java.util.Comparator;

import java.time.Month;


@Getter
@Setter
public class Sistema {

    private final DataParaCargadores datos;
    private boolean generosProcesados = false;

    public Sistema(DataParaCargadores datos) {
        this.datos = datos;
    }

    public void funcion1() {
        System.out.println("\n--- Ejecutando Consulta 1: Top 5 Películas por Idioma ---");
        long startTime = System.currentTimeMillis();

        // 1. Agrupar películas por idioma
        HashTable<String, MiArrayList<Pelicula>> peliculasPorIdioma = new HashCerradaLineal<>(200);
        for (Pelicula p : datos.getPeliculas()) {
            String idioma = p.getIdiomaOriginal();
            if (idioma != null && !idioma.isEmpty()) {
                MiArrayList<Pelicula> listaDelIdioma = peliculasPorIdioma.obtener(idioma);
                if (listaDelIdioma == null) {
                    listaDelIdioma = new MiArrayList<>();
                    try {
                        peliculasPorIdioma.insertar(idioma, listaDelIdioma);
                    } catch (Exception ignored) {}
                }
                listaDelIdioma.add(p);
            }
        }

        String[] idiomas = {"en", "fr", "it", "es", "pt"};
        String[] titulos = {"Inglés", "Francés", "Italiano", "Español", "Portugués"};

        Comparator<Pelicula> comparador = (p1, p2) -> Integer.compare(p2.getConteoCalificaciones(), p1.getConteoCalificaciones() //si uso esto va mas rapido
        );

        // 3. Procesar cada idioma
        for (int i = 0; i < idiomas.length; i++) {
            System.out.println("\n-- " + titulos[i] + " --");
            MiArrayList<Pelicula> listaAProcesar = peliculasPorIdioma.obtener(idiomas[i]);

            if (listaAProcesar != null && !listaAProcesar.isEmpty()) {

                for (Pelicula p : listaAProcesar) {
                    p.setConteoCalificaciones(obtenerCantidadCalificaciones(p.getId()));
                }

                MergeSort.sort(listaAProcesar, comparador);
                imprimirTopPeliculas(listaAProcesar, 5, titulos[i]);

            } else {
                System.out.println("No se encontraron películas para este idioma.");
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (endTime - startTime) + "ms");
    }


    private void imprimirTopPeliculas(MiArrayList<Pelicula> peliculas, int topN, String nombreIdioma) {
        int limite = Math.min(topN, peliculas.size());
        for (int i = 0; i < limite; i++) {
            Pelicula p = peliculas.get(i);
            System.out.println(p.getId() + ", " + p.getTitulo() + ", " + p.getConteoCalificaciones() + ", " + nombreIdioma);
        }
    }

    private int obtenerCantidadCalificaciones(int idPelicula) {
        MiLista<Calificacion> calif = datos.getCalificacionesPorPelicula().obtener(idPelicula);
        return (calif != null) ? calif.size() : 0; //SIMPLFIQUE EL IF ASI ME QUEDA MAS CORTITO
    }

    public void funcion2() {
        System.out.println("\n--- Ejecutando Consulta 2: Top 10 Películas con Mejor Calificación Media ---");
        long startTime = System.currentTimeMillis();

        MiArrayList<Pelicula> candidatas = new MiArrayList<>();

        for (Integer idPelicula : datos.getCalificacionesPorPelicula().keys()) {

            if (obtenerCantidadCalificaciones(idPelicula) > 100) {
                Pelicula p = datos.getPeliculas().obtener(idPelicula);
                if (p != null) {
                    p.setCalificacionPromedio(calcularPromedioCalificacion(p));
                    candidatas.add(p);
                }
            }
        }

        Comparator<Pelicula> comp = (p1, p2) -> Double.compare(p2.getCalificacionPromedio(), p1.getCalificacionPromedio());
        MergeSort.sort(candidatas, comp);

        System.out.println("\n-- Top 10 Películas por Calificación Media --");
        int limite = Math.min(10, candidatas.size());
        for (int i = 0; i < limite; i++) {
            Pelicula p = candidatas.get(i);
            System.out.printf("%d, %s, %.2f\n", p.getId(), p.getTitulo(), p.getCalificacionPromedio());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private double calcularPromedioCalificacion(Pelicula pelicula) {
        MiLista<Calificacion> calificaciones = datos.getCalificacionesPorPelicula().obtener(pelicula.getId());
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Calificacion c : calificaciones) {
            suma += c.getPuntaje();
        }
        return suma / calificaciones.size();
    }

    public void funcion3() {
        System.out.println("\n Ejecutando Consulta 3: Top 5 Colecciones por Ingresos");
        long startTime = System.currentTimeMillis();

        // 1. Calculamos los ingresos de cada colección de forma optimizada.
        for (Coleccion c : datos.getColecciones()) {
            double totalIngresos = 0;
            for (Pelicula p : c.getPeliculas()) {
                totalIngresos += p.getIngresos();
            }
            c.setIngresos(totalIngresos);
        }

        // 2. Creamos una lista temporal para poder ordenarla.
        MiArrayList<Coleccion> listaParaOrdenar = new MiArrayList<>();
        for (Coleccion c : datos.getColecciones()) {
            listaParaOrdenar.add(c);
        }
        // 3. Ordenamos la lista por ingresos.
        Comparator<Coleccion> comp = (c1, c2) -> Double.compare(c2.getIngresos(), c1.getIngresos());
        MergeSort.sort(listaParaOrdenar, comp);
        // 4. Imprimimos el resultado con el formato solicitado.
        System.out.println("\nTop 5 Colecciones por Ingresos Generados");
        int limite = Math.min(5, listaParaOrdenar.size());
        for (int i = 0; i < limite; i++) {
            Coleccion c = listaParaOrdenar.get(i);

            StringBuilder idsPeliculasStr = new StringBuilder("[");
            boolean esPrimero = true;
            for (Integer idPelicula : c.getPeliculas().keys()) {
                if (!esPrimero) {
                    idsPeliculasStr.append(",");
                }
                idsPeliculasStr.append(idPelicula);
                esPrimero = false;
            }
            idsPeliculasStr.append("]");

            System.out.printf("%d,%s,%d,%s,%.2f\n",
                    c.getId(),
                    c.getTitulo(),
                    c.getPeliculas().tamanio(),
                    idsPeliculasStr.toString(),
                    c.getIngresos()
            );
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void funcion4() {
        System.out.println("\n Ejecutando Consulta 4: Top 10 Directores por Calificación (Mediana)");
        long startTime = System.currentTimeMillis();

        MiArrayList<Persona> directoresCalificados = new MiArrayList<>();

        for (Persona p : datos.getPersonas()) {

            if (p.isDirector() && p.getIdsPeliculasDirigidas().size() > 1) {
                MiArrayList<Double> calificaciones = obtenerCalificacionesDeDirector(p);
                if (calificaciones.size() > 100) {
                    p.setCalificacion(calculoMediana(calificaciones));
                    directoresCalificados.add(p);
                }
            }
        }
        Comparator<Persona> comp = (p1, p2) -> Double.compare(p2.getCalificacion(), p1.getCalificacion());
        MergeSort.sort(directoresCalificados, comp);

        System.out.println("\n-- Top 10 Directores por Calificación Mediana --");
        int limite = Math.min(10, directoresCalificados.size());
        for (int i = 0; i < limite; i++) {
            Persona p = directoresCalificados.get(i);
            System.out.printf("%s,%d,%.2f\n", p.getNombre(), p.getIdsPeliculasDirigidas().size(), p.getCalificacion());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (System.currentTimeMillis() - startTime) + "ms");

    }
    private MiArrayList<Double> obtenerCalificacionesDeDirector(Persona director) {
        MiArrayList<Double> lista = new MiArrayList<>();
        for (Integer idPelicula : director.getIdsPeliculasDirigidas()) {
            MiLista<Calificacion> califPeli = datos.getCalificacionesPorPelicula().obtener(idPelicula);
            if (califPeli != null) {
                for (Calificacion c : califPeli) {
                    lista.add(c.getPuntaje());
                }
            }
        }
        return lista;
    }
    private double calculoMediana(MiArrayList<Double> lista) {
        if (lista.isEmpty()) {
            return 0.0;
        }
        MergeSort.sort(lista, Double::compare);
        int n = lista.size();
        if (n % 2 != 0) {
            // Si el tamaño es impar, la mediana es el elemento del medio.
            return lista.get(n / 2);
        } else {
            // Si el tamaño es par, es el promedio de los dos del medio.
            double medio1 = lista.get(n / 2 - 1);
            double medio2 = lista.get(n / 2);
            return (medio1 + medio2) / 2.0;
        }
    }

    public void funcion5() {
        System.out.println("\n--- Ejecutando Consulta 5: Actor más Popular por Mes ---");
        long startTime = System.currentTimeMillis();
        @SuppressWarnings("unchecked")
        HashTable<Integer, ActorStats>[] statsPorMes = (HashTable<Integer, ActorStats>[]) new HashTable[12];
        for (int i = 0; i < 12; i++) {
            statsPorMes[i] = new HashCerradaLineal<>(30001);
        }

        for (Calificacion calificacion : datos.getCalificaciones()) {
            if (calificacion.getFecha() != null) {
                // El mes va de 1 a 12, el índice del array de 0 a 11.
                int monthIndex = calificacion.getFecha().getMonthValue() - 1;
                HashTable<Integer, ActorStats> statsDelMes = statsPorMes[monthIndex];
                MiLista<Persona> actores = datos.getActoresPorPelicula().obtener(calificacion.getIdPelicula());
                if (actores != null) {
                    for (Persona actor : actores) {
                        actualizarStatsActor(statsDelMes, actor, calificacion.getIdPelicula());
                    }
                }
            }
        }

        for (int month = 1; month <= 12; month++) {
            imprimirGanadorDelMes(month, statsPorMes[month - 1]);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void actualizarStatsActor(HashTable<Integer, ActorStats> statsDelMes, Persona actor, int movieId) {
        ActorStats stats = statsDelMes.obtener(actor.getId());
        if (stats == null) {
            stats = new ActorStats(actor.getNombre());
            try {
                statsDelMes.insertar(actor.getId(), stats);
            } catch (Exception ignored) {}
        }
        stats.ratingCount++;
        if (!stats.movies.pertenece(movieId)) {
            try {
                stats.movies.insertar(movieId, true);
            } catch (Exception ignored) {}
        }
    }

    private void imprimirGanadorDelMes(int month, HashTable<Integer, ActorStats> statsDelMes) {
        ActorStats actorGanador = null;

        for (ActorStats currentStats : statsDelMes) {
            if (actorGanador == null || currentStats.ratingCount > actorGanador.ratingCount) {
                actorGanador = currentStats;
            }
        }

        System.out.println("\n-- Mes: " + Month.of(month) + " --");
        if (actorGanador != null) {
            System.out.printf("%s,%s,%d,%d\n",
                    Month.of(month).name(),
                    actorGanador.nombre,
                    actorGanador.movies.tamanio(),
                    actorGanador.ratingCount);
        } else {
            System.out.println("No se encontraron calificaciones para este mes.");
        }
    }

    public void funcion6() {
        System.out.println("\n--- Ejecutando Consulta 6: Usuarios con más Calificaciones por Género ---");
        long startTime = System.currentTimeMillis();

        if (!generosProcesados) {
            prepararCalificacionesPorGeneroParaUsuarios();
        }

        // 2. Contar la popularidad de cada género.
        HashTable<String, Integer> conteoGeneros = new HashCerradaLineal<>(50);
        for (Calificacion calificacion : datos.getCalificaciones()) {
            Pelicula p = datos.getPeliculas().obtener(calificacion.getIdPelicula());
            if (p != null) {
                for (String genero : p.getGeneros()) {
                    Integer count = conteoGeneros.obtener(genero);
                    try {
                        if (count == null) conteoGeneros.insertar(genero, 1);
                        else conteoGeneros.actualizar(genero, count + 1);
                    } catch (Exception ignored) {}
                }
            }
        }
        // 3. Ordenar los géneros por popularidad de forma eficiente.
        MiArrayList<Objeto<String, Integer>> generosOrdenados = new MiArrayList<>();
        for (Objeto<String, Integer> entry : conteoGeneros.entries()) {
            generosOrdenados.add(entry);
        }
        MergeSort.sort(generosOrdenados, (e1, e2) -> e2.getValor().compareTo(e1.getValor()));

        // 4. Encontrar el mejor usuario para CADA género en UNA SOLA PASADA.
        HashCerradaLineal<String, UsuarioConConteo> topUsuariosPorGenero = new HashCerradaLineal<>(25); // 25 géneros aprox.
        for (Usuario u : datos.getUsuarios()) {
            for (Objeto<String, Integer> califGenero : u.getCalificacionesPorGenero().entries()) {
                String genero = califGenero.getClave();
                int conteoUsuario = califGenero.getValor();

                UsuarioConConteo topActual = topUsuariosPorGenero.obtener(genero);
                if (topActual == null || conteoUsuario > topActual.conteo) {
                    try {
                        // Usamos la clase de ayuda para guardar al usuario y su conteo.
                        topUsuariosPorGenero.actualizar(genero, new UsuarioConConteo(u, conteoUsuario));
                    } catch (ElementoNoExistenteException e) {
                        try {
                            topUsuariosPorGenero.insertar(genero, new UsuarioConConteo(u, conteoUsuario));
                        } catch (ElementoYaExistenteException ex) { /* Ignored */ }
                    }
                }
            }
        }
        // 5. Imprimir los resultados.
        System.out.println("\n-- Top Usuarios por Género (Top 10 Géneros más Vistos) --");
        int limite = Math.min(10, generosOrdenados.size());
        for (int i = 0; i < limite; i++) {
            String generoTop = generosOrdenados.get(i).getClave();
            // OPTIMIZADO: Obtenemos el resultado con una búsqueda O(1), sin bucles anidados.
            UsuarioConConteo ganador = topUsuariosPorGenero.obtener(generoTop);
            if (ganador != null) {
                System.out.printf("%d,%s,%d\n", ganador.usuario.getId(), generoTop, ganador.conteo);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void prepararCalificacionesPorGeneroParaUsuarios() {
        System.out.println("  (Procesando géneros de usuarios por primera vez, esto puede tardar un momento...)");
        for (Usuario usuario : datos.getUsuarios()) {
            for (Calificacion calificacion : usuario.getCalificacionesDelUsuario()) {
                Pelicula pelicula = datos.getPeliculas().obtener(calificacion.getIdPelicula());
                if (pelicula != null) {
                    for (String genero : pelicula.getGeneros()) {
                        usuario.agregarCalificacionPorGenero(genero);
                    }
                }
            }
        }
        this.generosProcesados = true;
    }

    private static class ActorStats { //esto me es re util auqnue debo de ver como lo implemento mejor
        String nombre;
        int ratingCount = 0;
        HashTable<Integer, Boolean> movies = new HashCerradaLineal<>(53);
        ActorStats(String nombre) {
            this.nombre = nombre;
        }
    }

    //eestuve investigando un caho y funciona esto, hay que verlo
    private static class UsuarioConConteo {
        Usuario usuario;
        int conteo;

        public UsuarioConConteo(Usuario usuario, int conteo) {
            this.usuario = usuario;
            this.conteo = conteo;
        }
    }


}