package um.edu.uy.cargadoresDeData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import um.edu.uy.clases.Calificacion;
import um.edu.uy.clases.Usuario;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CargadorDeCalificaciones {

    private final CSVFormat formatoLectura = CSVFormat.DEFAULT.builder()
            .setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build();

    public void cargarCalificaciones(String filePath, MiLista<Calificacion> todasLasCalificaciones,
                                     HashTable<Integer, MiLista<Calificacion>> calificacionesPorPelicula,
                                     HashTable<Integer, Usuario> usuarios) {
        try (Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(in, formatoLectura)) {
            for (CSVRecord record : parser) {
                procesadoPorFila(record, todasLasCalificaciones, calificacionesPorPelicula, usuarios);
            }
        } catch (IOException e) {
            System.err.println("ERROR de I/O al leer el archivo CSV de calificaciones: " + e.getMessage());
        }
    }

    private void procesadoPorFila(CSVRecord record, MiLista<Calificacion> todasLasCalificaciones,
                                  HashTable<Integer, MiLista<Calificacion>> calificacionesPorPelicula,
                                  HashTable<Integer, Usuario> usuarios) {
        try {
            int userId = Integer.parseInt(record.get("userId"));
            int idPelicula = Integer.parseInt(record.get("movieId"));
            double puntaje = Double.parseDouble(record.get("rating"));
            long timestamp = Long.parseLong(record.get("timestamp"));
            LocalDate fecha = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();

            Calificacion calificacion = new Calificacion(userId, idPelicula, puntaje, fecha);

            // 1. Añadir a la lista plana de todas las calificaciones
            todasLasCalificaciones.add(calificacion);

            // 2. Añadir a la tabla hash de calificaciones agrupadas por película
            MiLista<Calificacion> listaParaPelicula = calificacionesPorPelicula.obtener(idPelicula);
            if (listaParaPelicula == null) {
                listaParaPelicula = new MiArrayList<>();
                calificacionesPorPelicula.insertar(idPelicula, listaParaPelicula);
            }
            listaParaPelicula.add(calificacion);

            // 3. Añadir al usuario correspondiente
            Usuario usuario = usuarios.obtener(userId);
            if (usuario == null) {
                usuario = new Usuario(userId);
                usuarios.insertar(userId, usuario);
            }
            usuario.getCalificacionesDelUsuario().add(calificacion);

        } catch (IllegalArgumentException | ElementoYaExistenteException e) {
            // Ignoramos las filas con datos inválidos o que causen errores de inserción
        }
    }
}
