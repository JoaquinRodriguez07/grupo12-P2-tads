package um.edu.uy.cargadoresDeData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import um.edu.uy.clases.Calificacion;
import um.edu.uy.clases.Usuario;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.excepciones.ElementoYaExistenteException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CargadorDeCalificaciones {

    // Se mantiene la configuración del formato del CSV
    private final CSVFormat formatoLectura = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true)
            .build();


    public void cargarCalificaciones(String filePath, MiLista<Calificacion> todasLasCalificaciones, HashTable<Integer, Usuario> usuarios) {
        try (Reader in = new FileReader(filePath);
             CSVParser parser = new CSVParser(in, formatoLectura)) {
            for (CSVRecord record : parser) {
                procesadoPorFila(record, todasLasCalificaciones, usuarios);
            }
        } catch (IOException e) {
            System.err.println("ERROR de I/O al leer el archivo CSV de calificaciones: " + filePath + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void procesadoPorFila(CSVRecord record, MiLista<Calificacion> todasLasCalificaciones, HashTable<Integer, Usuario> usuarios) {
        try {
            // Extracción y Parseo de Datos
            int userId = Integer.parseInt(record.get("userId"));
            int idPelicula = Integer.parseInt(record.get("movieId"));
            double puntaje = Double.parseDouble(record.get("rating"));
            long timeStampLong = Long.parseLong(record.get("timestamp"));
            LocalDate fecha = Instant.ofEpochSecond(timeStampLong).atZone(ZoneId.systemDefault()).toLocalDate();

            // 1. Se crea el objeto Calificacion
            Calificacion calificacion = new Calificacion(userId, idPelicula, puntaje, fecha);

            // 2. Se añade la calificación a la lista general de todas las calificaciones
            todasLasCalificaciones.add(calificacion);

            // 3. Se busca o se crea el usuario y se le añade la calificación
            Usuario usuario = usuarios.obtener(userId);
            if (usuario == null) {
                // Si el usuario no existe, se crea uno nuevo
                usuario = new Usuario(userId);
                try {
                    usuarios.insertar(userId, usuario);
                } catch (ElementoYaExistenteException e) {
                    // No debería ocurrir si la lógica es correcta
                }
            }
            // Se añade la calificación a la lista de calificaciones de ese usuario
            usuario.getCalificacionesDelUsuario().add(calificacion);

        } catch (IllegalArgumentException e) {
            // Ignoramos filas con datos mal formateados
            // System.err.println("ERROR: Fila con datos inválidos. Fila: " + record.getRecordNumber() + ", Datos: " + record.toString());
        }
    }
}