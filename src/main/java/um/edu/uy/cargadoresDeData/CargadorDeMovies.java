package um.edu.uy.cargadoresDeData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import um.edu.uy.clases.Coleccion;
import um.edu.uy.clases.Pelicula;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargadorDeMovies {

    // Patrones para extraer información de los campos tipo JSON
    private static final Pattern COLLECTION_ID_PATTERN = Pattern.compile("'id':\\s*(\\d+)");
    private static final Pattern COLLECTION_NAME_PATTERN = Pattern.compile("'name':\\s*'([^']*)'");
    private static final Pattern GENRE_NAME_PATTERN = Pattern.compile("'name':\\s*'([^']*)'");


    public void cargadorMoviesAHash(String rutaArchivo, HashTable<Integer, Pelicula> peliculasHash, HashTable<Integer, Coleccion> coleccionesHash) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        try (Reader in = new FileReader(rutaArchivo, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(in, format)) {
            for (CSVRecord record : parser) {
                procesadoDeLaFila(record, peliculasHash, coleccionesHash);
            }
        } catch (IOException e) {
            System.out.println("Error de I/O al leer el archivo CSV: " + rutaArchivo + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void procesadoDeLaFila(CSVRecord record, HashTable<Integer, Pelicula> peliculasHash, HashTable<Integer, Coleccion> coleccionesHash) {
        try {
            // Se parsean los campos básicos de la película
            int peliculaId = Integer.parseInt(record.get("id"));
            String titulo = record.get("title");
            String idiomaOriginal = record.get("original_language");
            double ingresos = parseDouble(record.get("revenue"));

            // Se parsea la información de la colección
            String collectionRaw = record.get("belongs_to_collection");
            String[] collectionInfo = parseCollectionInfo(collectionRaw); // Devuelve [id, nombre] o null

            // Se crea el objeto Pelicula (el nombre de la colección se guarda por simplicidad)
            String nombreColeccion = (collectionInfo != null) ? collectionInfo[1] : null;
            Pelicula pelicula = new Pelicula(peliculaId, titulo, idiomaOriginal, nombreColeccion, ingresos);

            // Se parsean y añaden los géneros a la película
            String generoRaw = record.get("genres");
            MiLista<String> generos = parseGeneros(generoRaw);
            pelicula.setGeneros(generos);

            // Se inserta la película en la tabla hash principal de películas
            peliculasHash.insertar(pelicula.getId(), pelicula);

            // --- Lógica para manejar las colecciones ---
            if (collectionInfo != null) {
                // La película pertenece a una saga
                int coleccionId = Integer.parseInt(collectionInfo[0]);
                String coleccionNombre = collectionInfo[1];

                Coleccion coleccion = coleccionesHash.obtener(coleccionId);
                if (coleccion == null) {
                    // Si la colección no existe, se crea una nueva
                    coleccion = new Coleccion(coleccionId, coleccionNombre);
                    coleccionesHash.insertar(coleccionId, coleccion);
                }
                // Se añade la película a la tabla hash de la colección
                coleccion.getPeliculas().insertar(pelicula.getId(), pelicula);

            } else {
                // La película NO pertenece a una saga, se trata como su propia colección
                int coleccionId = pelicula.getId();
                // Se verifica que no exista ya una colección con este ID (poco probable pero seguro)
                if (!coleccionesHash.pertenece(coleccionId)) {
                    Coleccion coleccionPropia = new Coleccion(coleccionId, pelicula.getTitulo());
                    coleccionPropia.getPeliculas().insertar(pelicula.getId(), pelicula);
                    coleccionesHash.insertar(coleccionId, coleccionPropia);
                }
            }

        } catch (NumberFormatException e) {
            // Ignoramos filas con ID inválido
        } catch (IllegalArgumentException | ElementoYaExistenteException e) {
            // System.out.println("Error procesando registro: " + e.getMessage());
        }
    }

    /**
     * Parsea el string JSON de géneros y devuelve una lista de nombres de género.
     */
    private MiLista<String> parseGeneros(String genresRaw) {
        MiLista<String> generos = new MiArrayList<>();
        if (genresRaw == null || genresRaw.isEmpty() || genresRaw.equals("[]")) {
            return generos;
        }
        Matcher matcher = GENRE_NAME_PATTERN.matcher(genresRaw);
        while (matcher.find()) {
            generos.add(matcher.group(1));
        }
        return generos;
    }

    /**
     * Parsea el string JSON de la colección y devuelve su ID y nombre.
     * @return Un array de String `[id, nombre]` o `null` si no hay colección.
     */
    private String[] parseCollectionInfo(String collectionRaw) {
        if (collectionRaw == null || collectionRaw.isEmpty()) {
            return null;
        }
        Matcher idMatcher = COLLECTION_ID_PATTERN.matcher(collectionRaw);
        Matcher nameMatcher = COLLECTION_NAME_PATTERN.matcher(collectionRaw);

        if (idMatcher.find() && nameMatcher.find()) {
            return new String[]{idMatcher.group(1), nameMatcher.group(1)};
        }
        return null;
    }

    private double parseDouble(String valueStr) {
        if (valueStr != null && !valueStr.trim().isEmpty()) {
            try {
                return Double.parseDouble(valueStr.trim());
            } catch (NumberFormatException e) {
                // Ignorar si no es un número válido
            }
        }
        return 0.0;
    }
}