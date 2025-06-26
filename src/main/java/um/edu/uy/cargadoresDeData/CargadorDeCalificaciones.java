package um.edu.uy.cargadoresDeData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import um.edu.uy.clases.Calificaciones;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.excepciones.ElementoYaExistenteException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class CargadorDeCalificaciones {

    //igual que en movies
    CSVFormat formatoLectura = CSVFormat.DEFAULT.builder() // Usa el formato CSV por defecto (comas como separadores)
            .setHeader()             // Indica que la primera línea del CSV es un encabezado
            .setSkipHeaderRecord(true) // Le dice al parser que no te devuelva la línea del encabezado como un registro de datos
            .setIgnoreHeaderCase(true) //  Permite que los nombres de las columnas no le importe si esta en mayus o minus (Por ejemplo, "ID" y "id" se tratarán como la misma columna)
            .setTrim(true)           // Elimina espacios en blanco iniciales/finales de cada campo leído
            .build();                // Construye la configuración final del formato


    public HashTable<Integer, MiLista<Calificaciones>> cargarCalificacionesAHash(String filePath) {

        HashCerradaLineal<Integer, MiLista<Calificaciones>> calificacionesHash = new HashCerradaLineal<>(124001);//hay que buscar la manera de que esto sea dinamico no que sea asignado, de utlima se refactoriza. Para calcular esto uso el factor de carga

        try (Reader in = new FileReader(filePath);
             CSVParser parser = new CSVParser(in, formatoLectura)) {
                for (CSVRecord record : parser) {//lo que me importa est aca, pq esto me itera sobre cada fila del excel (CSV)
                    procesadoPorFila(record, calificacionesHash);
                }

            } catch (IOException e) {
                System.err.println("ERROR de I/O al leer el archivo CSV de calificaciones: " + filePath + " - " + e.getMessage());
                e.printStackTrace();
        }

        return calificacionesHash;
    }

    private void procesadoPorFila(CSVRecord record, HashCerradaLineal<Integer, MiLista<Calificaciones>> calificacionesHash) {

        int userId;
        int idPelicula;
        double puntaje = 0.0;
        LocalDate fecha = null;
        Calificaciones calificacion = null; // la puse aca pq me da error de scope
        MiLista<Calificaciones> calificacionesParaPelicula = null; //lo mismo

        try {
            // Extracción y Parseo de Datos
            userId = Integer.parseInt(record.get(0).trim());
            idPelicula = Integer.parseInt(record.get(1).trim());
            puntaje = Double.parseDouble(record.get(2).trim());

            // Parseo del timeStamp (long) y conversión a LocalDate.
            long timeStampLong = Long.parseLong(record.get(3).trim());
            fecha = Instant.ofEpochMilli(timeStampLong * 1000L) .atZone(ZoneId.systemDefault()).toLocalDate(); // Importante para la zona horaria

            // Con todos los datos extraídos y parseados, creamos el objeto.
            calificacion = new Calificaciones(userId, idPelicula, puntaje, fecha);

            // Inserción en la tabla hash
            calificacionesParaPelicula = calificacionesHash.obtener(idPelicula);

            if (calificacionesParaPelicula == null) {
                // Si no hay lista para esta película, creamos una nueva MiArrayList.
                calificacionesParaPelicula = new MiArrayList<>(); //investigar si tengo que meter el comparable aca o no

                calificacionesParaPelicula.add(calificacion); // Añadimos la calificación actual a la nueva lista.


                // Insertamos esta nueva MiLista en la HashCerradaLineal.

                try {
                    calificacionesHash.insertar(idPelicula, calificacionesParaPelicula);
                } catch (ElementoYaExistenteException e) {
                    // pero se maneja por si acaso hay un error lógico en la implementación de la hash.
                    System.err.println("ERROR : ID Película " + idPelicula + " no se encontró, pero luego 'insertar' lanzó ElementoYaExistenteException.");
                }
            } else {
                // Si la lista ya existe, simplemente añadimos la nueva calificación a ella.
                calificacionesParaPelicula.add(calificacion);
            }

        } catch (IndexOutOfBoundsException e) {
            // Captura si una fila no tiene suficientes columnas (ej. menos de 4).
            System.err.println("ERROR: Fila con menos columnas de las esperadas. Fila: " + record.getRecordNumber() + ", Datos: " + record.toString() + ". Error: " + e.getMessage());
    }
}}
