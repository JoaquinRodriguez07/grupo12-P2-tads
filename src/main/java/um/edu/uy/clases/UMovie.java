package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;

@Getter@Setter
public class UMovie {
    HashCerradaLineal<Integer, Pelicula> peliculasHashTable;
    HashCerradaLineal<Integer, MiLista<Calificacion>> calificacionesHashTable;
    HashCerradaLineal<Integer, Persona> personasHashTable;
    HashCerradaLineal<Integer, Coleccion> coleccionesHashTable;
    HashCerradaLineal<Integer, Usuario> usuariosHashTable;

    public UMovie(HashCerradaLineal<Integer, Pelicula> peliculasHashTable,
                  HashCerradaLineal<Integer, MiLista<Calificacion>> calificacionesHashTable,
                  HashCerradaLineal<Integer, Persona> personasHashTable, HashCerradaLineal<Integer, Coleccion> coleccionesHashTable,
                  HashCerradaLineal<Integer, Usuario> usuariosHashTable) {
        this.peliculasHashTable = peliculasHashTable;
        this.calificacionesHashTable = calificacionesHashTable;
        this.personasHashTable = personasHashTable;
        this.coleccionesHashTable = coleccionesHashTable;
        this.usuariosHashTable = usuariosHashTable;
    }

    public void funcion1(){
        long startTime = System.currentTimeMillis();
        MiLista<Integer> idsPeliculas = peliculasHashTable.getClaves(); //consigo una lista con todas las claves de la lista peliculas
        MiArrayList<Pelicula> peliculasEs = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasEn = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasFr = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasIt = new MiArrayList<>();
        MiArrayList<Pelicula> peliculasPt = new MiArrayList<>();

        for (int i = 0; i < idsPeliculas.size(); i++) {
            Integer idPelicula = idsPeliculas.get(i); //de esta forma consigo los ids de las peliculas que estan en la hashtable
            Pelicula pelicula = peliculasHashTable.obtener(idPelicula); //y asi consigo la pelicula relacionada con ese id
            String idioma = pelicula.getIdiomaOriginal();

            if (idioma.equalsIgnoreCase("es")) { //con el equalsIgnoreCase ignora las mayúsculas, por si acaso. no lo hago con un switch porque no tiene este metodo
                peliculasEs.add(pelicula);
            }else if (idioma.equalsIgnoreCase("en")) {
                peliculasEn.add(pelicula);
            }else if (idioma.equalsIgnoreCase("fr")) {
                peliculasFr.add(pelicula);
            }else if (idioma.equalsIgnoreCase("it")) {
                peliculasIt.add(pelicula);
            }else if (idioma.equalsIgnoreCase("pt")) { //creo que pt es portugués
                peliculasPt.add(pelicula);
            }
        }
        System.out.println("--Top 5 Español--");
        imprimirTop5Peliculas(peliculasEs);
        System.out.println(" ");

        System.out.println("--Top 5 Ingles--");
        imprimirTop5Peliculas(peliculasEn);
        System.out.println(" ");

        System.out.println("--Top 5 Frances--");
        imprimirTop5Peliculas(peliculasFr);
        System.out.println(" ");

        System.out.println("--Top 5 Italia--");
        imprimirTop5Peliculas(peliculasIt);
        System.out.println(" ");

        System.out.println("--Top 5 Portugues--");
        imprimirTop5Peliculas(peliculasPt);

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (endTime - startTime) + "ms");
    }

    private void imprimirTop5Peliculas(MiArrayList<Pelicula> peliculas) {
        if (peliculas.isEmpty()){
            System.out.println("No hay peliculas que imprimir");
            return; //si la lista esta vacia entonces volvemos
        }

        // para ordenar la lista uso el algoritmo bubblesort
        // quiero usar la cantidad de calificaciones que tiene la película para ordenar la lista
        int numPeliculas = peliculas.size();
        for (int i = 0; i < numPeliculas - 1; i++) {
            for (int j = 0; j < numPeliculas - i - 1; j++) {
                Pelicula pelicula1 = peliculas.get(j);
                MiLista<Calificacion> calificaciones1 = calificacionesHashTable.obtener(pelicula1.getId());
                int numCalificaciones1 = (calificaciones1 != null) ? calificaciones1.size() : 0; //es mas corto que poner if-else

                Pelicula pelicula2 = peliculas.get(j + 1);
                MiLista<Calificacion> calificaciones2 = calificacionesHashTable.obtener(pelicula2.getId());
                int numCalificaciones2 = (calificaciones2 != null) ? calificaciones2.size() : 0;

                if (numCalificaciones1 < numCalificaciones2) {
                    peliculas.set(j, pelicula2);
                    peliculas.set(j + 1, pelicula1);
                }
            }
        }
        for (int i = 0; i < Math.min(5, numPeliculas); i++) { //math.min hace que el menor numero entre a y b, en este caso, el 5 y el tamaño de la lista que ordenamos
            Pelicula pelicula = peliculas.get(i);
            MiLista<Calificacion> calificaciones = calificacionesHashTable.obtener(pelicula.getId());
            int numCalificaciones = (calificaciones != null) ? calificaciones.size() : 0;
            // volvemos a tener q conseguir estos valores, pero ahora la lista ya esta ordenada
            System.out.println(pelicula.getId() + ", " + pelicula.getTitulo() + ", " + numCalificaciones + ", " + pelicula.getIdiomaOriginal());
        }
    }

    //----------------------------------------------------------------------------------------------------------------------

    public void funcion2(){
        long startTime = System.currentTimeMillis();
        MiLista<Integer> idsPeliculas = peliculasHashTable.getClaves();
        MiArrayList<Pelicula> top10 = new MiArrayList<>();
        MiLista<Integer> calificaciones = calificacionesHashTable.getClaves();

        for (int i = 0; i < idsPeliculas.size(); i++) {
            Integer idPelicula = idsPeliculas.get(i);
            Pelicula pelicula = peliculasHashTable.obtener(idPelicula);
            if (calificaciones.contains(pelicula.getId())) {
                top10.add(pelicula);
            }
        }
        //como solo tengo que ordenar una vez la lista, no hace falta hacer la funcion auxiliar como en la funcion1 para ordenar
        int numPeliculas = top10.size();
        if (numPeliculas == 0) {
            System.out.println("No hay peliculas que imprimir");
            return;
        }
        for (int i = 0; i < numPeliculas - 1; i++) {
            for (int j = 0; j < numPeliculas - i - 1; j++) {
                Pelicula pelicula1 = top10.get(j);
                double puntaje1 = calcularPromedioCalificacion(pelicula1);
                Pelicula pelicula2 = top10.get(j + 1);
                double puntae2 = calcularPromedioCalificacion(pelicula2);
                if (puntaje1 < puntae2){
                    top10.set(j, pelicula2);
                    top10.set(j + 1, pelicula1);
                }
            }
        }
        System.out.println("--Top 10--");
        for (int i = 0; i < Math.min(10, numPeliculas); i++) {
            Pelicula pelicula = top10.get(i);
            double puntaje = calcularPromedioCalificacion(pelicula);
            System.out.println(pelicula.getId() + ", " + pelicula.getTitulo() + ", " + puntaje);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de consulta: " + (endTime - startTime) + "ms");

    }
    //logica para calcular el promedio
    private double calcularPromedioCalificacion(Pelicula pelicula){
        MiLista<Calificacion> calificaciones = calificacionesHashTable.obtener(pelicula.getId());
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0;
        }
        double sumaCalificaciones = 0;
        for (int i = 0; i < calificaciones.size(); i++) {
            sumaCalificaciones += calificaciones.get(i).getPuntaje();
        }
        return sumaCalificaciones / calificaciones.size();
    }

    //----------------------------------------------------------------------------------------------------------------------


    public void funcion3(){
        long startTime = System.currentTimeMillis();

        MiArrayList<Coleccion> colecciones = new MiArrayList<>();
        MiLista<Coleccion> todasLasColecciones = coleccionesHashTable.getValores();
        for (int i = 0; i < todasLasColecciones.size(); i++) {
            Coleccion coleccion = todasLasColecciones.get(i);
            double totalIngresos = 0;
            //pongo las peliculas que son parte de la coleccion en una lista, y consigo los ingresos para sumarlos al total
            HashCerradaLineal<Integer, Pelicula> peliculasEnHash = (HashCerradaLineal<Integer, Pelicula>) coleccion.getPeliculas();
            MiLista<Pelicula> peliculasDeColeccion = peliculasEnHash.getValores();
            if (peliculasDeColeccion != null) {
                for (int j = 0; j < peliculasDeColeccion.size(); j++) {
                    Pelicula pelicula = peliculasDeColeccion.get(j);
                    totalIngresos += pelicula.getIngresos();
                }
            }
            coleccion.setIngresos(totalIngresos);
            colecciones.add(coleccion);
            //ahora la coleccion ya tiene ingresado el toal de los ingresos y lo agrego a la lista de coleccion que voy a organizar
        }
        if (colecciones.isEmpty()) {
            System.out.println("No hay colecciones que imprimir");
            return; //esto es siempre por si acaso
        }
        //ordeno
        int numColecciones = colecciones.size();
        for (int i = 0; i < numColecciones - 1; i++) {
            for (int j = 0; j < numColecciones - i - 1; j++) {
                Coleccion coleccion1 = colecciones.get(j);
                Coleccion coleccion2 = colecciones.get(j + 1);
                if (coleccion1.getIngresos() < coleccion2.getIngresos()) {
                    colecciones.set(j, coleccion2);
                    colecciones.set(j + 1, coleccion1);
                }
            }
        }
        System.out.println("--Top 5 Colecciones--");
        for (int i = 0; i < Math.min(5, numColecciones); i++) {
            Coleccion coleccion = colecciones.get(i);

            //uso un stringbuilder para poder poner los ids de las peliculas en un string del modo que pide la letra
            StringBuilder idspeliculas = new StringBuilder("[");
            HashCerradaLineal<Integer, Pelicula> peliculasEnHash = (HashCerradaLineal<Integer, Pelicula>) coleccion.getPeliculas();
            MiLista<Pelicula> peliculasDeColeccion = peliculasEnHash.getValores();
            if (peliculasDeColeccion != null) {
                for (int j = 0; j < peliculasDeColeccion.size(); j++) {
                    //a la pelicula que este en la coleccion, la agrego al stringbuilder
                    idspeliculas.append(peliculasDeColeccion.get(j).getId());
                    if (j < peliculasDeColeccion.size() - 1) {
                        idspeliculas.append(",");
                    }
                }
            }
            idspeliculas.append("]");
            System.out.println(coleccion.getId() + ", " + coleccion.getTitulo() + ", " + coleccion.getCantidadPeliculas()
                    + ", " + idspeliculas + ", " + coleccion.getIngresos());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de consulta: " + (endTime - startTime) + "ms");
    }

    //----------------------------------------------------------------------------------------------------------------------

    public void funcion4(){
        long startTime = System.currentTimeMillis();
        MiArrayList<Persona> directores = new MiArrayList<>(); //para almacenar a los directores
        MiLista<Persona> todasLasPersonas = personasHashTable.getValores();

        for (int i = 0; i < todasLasPersonas.size(); i++){
            Persona persona = todasLasPersonas.get(i);
            if (persona.isDirector() && persona.getIdsPeliculasDirigidas().size() > 0){ //si es director y con al menos 1 pelicula dirigida
                MiLista<Integer> idsPeliculasDirigidas = persona.getIdsPeliculasDirigidas();
                MiArrayList<Double> calificacionesDirector = new MiArrayList<>();

                //ahora tenemos que conseguir su calificacion y guardarsela. tenemos que conseguir todas las calificaciones de sus peliculas para eso
                for (int j = 0; j < idsPeliculasDirigidas.size(); j++) {
                    Integer idPelicula = idsPeliculasDirigidas.get(j);
                    MiLista<Calificacion> calificacionesDePeliculas = calificacionesHashTable.obtener(idPelicula);
                    if (calificacionesDePeliculas != null) {
                        for (int k = 0; k < calificacionesDePeliculas.size(); k++) {
                            calificacionesDirector.add(calificacionesDePeliculas.get(k).getPuntaje());
                            //de esta forma conseguimos todas las calificacones de las peliculas en las que estuvo el director
                        }
                    }
                }
                //mas de una pelicula y mas de 100 calificaciones
                if (idsPeliculasDirigidas.size() > 1 && calificacionesDirector.size() > 100) {
                    double mediana = calculoMediana(calificacionesDirector);
                    persona.setCalificacion(mediana);
                    directores.add(persona);
                    //agregamos el director a la lista de directores que creamos al inicio, y tambien agregamos la mediana como calificacion
                }
            }
        }
        int numDirectores = directores.size();
        if (numDirectores == 0){
            System.out.println("No hay directores para imprimir");
            return;
        }
        //ordenar
        for (int i = 0; i < numDirectores - 1; i++) {
            for (int j = 0; j < numDirectores - i - 1; j++) {
                Persona director1 = directores.get(j);
                Persona director2 = directores.get(j + 1);

                if (director1.getCalificacion() < director2.getCalificacion()){
                    directores.set(j, director2);
                    directores.set(j + 1, director1);
                }
            }
        }
        System.out.println("--Top 10 directores--");
        for (int i = 0; i < Math.min(10, numDirectores); i++) {
            Persona director = directores.get(i);
            System.out.println(director.getNombre() + ", " + director.getIdsPeliculasDirigidas().size() + ", " + director.getCalificacion());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de consulta: " + (endTime - startTime) + "ms");
    }

    private double calculoMediana (MiArrayList<Double> calificaciones){
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0;
        }
        int n = calificaciones.size();
        //organizar
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (calificaciones.get(j) > calificaciones.get(j + 1)) {
                    double temp = calificaciones.get(j);
                    calificaciones.set(j, calificaciones.get(j + 1));
                    calificaciones.set(j + 1, temp);
                }
            }
        }
        if (n % 2 == 1) {
            return calificaciones.get(n / 2); //si es impar, entonces se agarra el valor del medio
        } else {
            double medio1 = calificaciones.get(n / 2 - 1);
            double medio2 = calificaciones.get(n / 2);
            return (medio1 + medio2) / 2; //si es par, entonces agarro los dos valores que serian el medio, y hago la mediana con esos
        }
    }

    //----------------------------------------------------------------------------------------------------------------------

    public void funcion6() throws ElementoYaExistenteException {
        long startTime = System.currentTimeMillis();

        System.out.println("-- Top10 Usuarios con mas calificaciones por genero --");
        MiLista<Usuario> todosLosUsuarios = usuariosHashTable.getValores();

        if (todosLosUsuarios == null || todosLosUsuarios.isEmpty()){
            System.out.println("No hay usuarios con mas calificaciones por genero");
            return;
        }

        //lo dejo asi porque me olvide lo del top10 :(
    }
}