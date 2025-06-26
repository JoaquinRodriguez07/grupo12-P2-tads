package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;

@Getter@Setter
public class UMovie {
    HashCerradaLineal<Integer, Peliculas> peliculasHashTable;
    HashCerradaLineal<Integer, MiLista<Calificaciones>> calificacionesHashTable;

    public UMovie(HashCerradaLineal<Integer, Peliculas> peliculasHashTable, HashCerradaLineal<Integer, MiLista<Calificaciones>> calificacionesHashTable) {
        this.peliculasHashTable = peliculasHashTable;
        this.calificacionesHashTable = calificacionesHashTable;
    }

    public void funcion1(){
        long startTime = System.currentTimeMillis();
        MiLista<Integer> idsPeliculas = peliculasHashTable.getClaves(); //consigo una lista con todas las claves de la lista peliculas
        MiArrayList<Peliculas> peliculasEs = new MiArrayList<>();
        MiArrayList<Peliculas> peliculasEn = new MiArrayList<>();
        MiArrayList<Peliculas> peliculasFr = new MiArrayList<>();
        MiArrayList<Peliculas> peliculasIt = new MiArrayList<>();
        MiArrayList<Peliculas> peliculasPt = new MiArrayList<>();

        for (int i = 0; i < idsPeliculas.size(); i++) {
            Integer idPelicula = idsPeliculas.get(i); //de esta forma consigo los ids de las peliculas que estan en la hashtable
            Peliculas pelicula = peliculasHashTable.obtener(idPelicula); //y asi consigo la pelicula relacionada con ese id
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

    private void imprimirTop5Peliculas(MiArrayList<Peliculas> peliculas) {
        if (peliculas.isEmpty()){
            System.out.println("No hay peliculas que imprimir");
            return; //si la lista esta vacia entonces volvemos
        }

        // para ordenar la lista uso el algoritmo bubblesort
        // quiero usar la cantidad de calificaciones que tiene la película para ordenar la lista
        int numPeliculas = peliculas.size();
        for (int i = 0; i < numPeliculas - 1; i++) {
            for (int j = 0; j < numPeliculas - i - 1; j++) {
                Peliculas pelicula1 = peliculas.get(j);
                MiLista<Calificaciones> calificaciones1 = calificacionesHashTable.obtener(pelicula1.getId());
                int numCalificaciones1 = (calificaciones1 != null) ? calificaciones1.size() : 0; //es mas corto que poner if-else

                Peliculas pelicula2 = peliculas.get(j + 1);
                MiLista<Calificaciones> calificaciones2 = calificacionesHashTable.obtener(pelicula2.getId());
                int numCalificaciones2 = (calificaciones2 != null) ? calificaciones2.size() : 0;

                if (numCalificaciones1 < numCalificaciones2) {
                    peliculas.set(j, pelicula2);
                    peliculas.set(j + 1, pelicula1);
                }
            }
        }
        for (int i = 0; i < Math.min(5, numPeliculas); i++) { //math.min hace que el menor numero entre a y b, en este caso, el 5 y el tamaño de la lista que ordenamos
            Peliculas pelicula = peliculas.get(i);
            MiLista<Calificaciones> calificaciones = calificacionesHashTable.obtener(pelicula.getId());
            int numCalificaciones = (calificaciones != null) ? calificaciones.size() : 0;
            // volvemos a tener q conseguir estos valores, pero ahora la lista ya esta ordenada
            System.out.println(pelicula.getId() + ", " + pelicula.getTitulo() + ", " + numCalificaciones + ", " + pelicula.getIdiomaOriginal());
        }
    }

    public void funcion2(){
        long startTime = System.currentTimeMillis();
        MiLista<Integer> idsPeliculas = peliculasHashTable.getClaves();
        MiArrayList<Peliculas> top10 = new MiArrayList<>();
        MiLista<Integer> calificaciones = calificacionesHashTable.getClaves();

        for (int i = 0; i < idsPeliculas.size(); i++) {
            Integer idPelicula = idsPeliculas.get(i);
            Peliculas pelicula = peliculasHashTable.obtener(idPelicula);
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
                Peliculas pelicula1 = top10.get(j);
                double puntaje1 = calcularPromedioCalificacion(pelicula1);
                Peliculas pelicula2 = top10.get(j + 1);
                double puntae2 = calcularPromedioCalificacion(pelicula2);
                if (puntaje1 < puntae2){
                    top10.set(j, pelicula2);
                    top10.set(j + 1, pelicula1);
                }
            }
        }
        System.out.println("--Top 10--");
        for (int i = 0; i < Math.min(10, numPeliculas); i++) {
            Peliculas pelicula = top10.get(i);
            double puntaje = calcularPromedioCalificacion(pelicula);
            System.out.println(pelicula.getId() + ", " + pelicula.getTitulo() + ", " + puntaje);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de consulta: " + (endTime - startTime) + "ms");

    }
    //logica para calcular el promedio
    private double calcularPromedioCalificacion(Peliculas pelicula){
        MiLista<Calificaciones> calificaciones = calificacionesHashTable.obtener(pelicula.getId());
        if (calificaciones.isEmpty()) {
            return 0;
        }
        double sumaCalificaciones = 0;
        for (int i = 0; i < calificaciones.size(); i++) {
            sumaCalificaciones += calificaciones.get(i).getPuntaje();
        }
        return sumaCalificaciones / calificaciones.size();
    }


    public void funcion3(){
        long startTime = System.currentTimeMillis();

    }
}
