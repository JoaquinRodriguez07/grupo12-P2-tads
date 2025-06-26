package um.edu.uy.cargadoresDeData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import um.edu.uy.clases.Persona;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargadorDeCredits {

    // Expresiones regulares para extraer datos de los strings tipo JSON
    private static final Pattern CREW_MEMBER_PATTERN = Pattern.compile("\\{'credit_id':.*?\\}");
    private static final Pattern CAST_MEMBER_PATTERN = Pattern.compile("\\{'cast_id':.*?\\}");

    // Patrones para extraer campos individuales de una entrada de persona
    private static final Pattern ID_PATTERN = Pattern.compile("'id':\\s*(\\d+)");
    private static final Pattern NAME_PATTERN = Pattern.compile("'name':\\s*'([^']*)'");
    private static final Pattern JOB_PATTERN = Pattern.compile("'job':\\s*'([^']*)'");

    /**
     * Crea una tabla hash, la puebla con datos de actores y directores desde un archivo CSV, y la devuelve.
     *
     * @param rutaArchivo La ruta al archivo credits.csv.
     * @return Una HashTable con todas las personas (actores y directores) cargadas.
     */
    public HashTable<Integer, Persona> cargarCreditsAHash(String rutaArchivo) {
        // Se instancia la tabla hash aquí dentro, con una capacidad inicial grande y prima.
        HashTable<Integer, Persona> personasHashTable = new HashCerradaLineal<>(263471);

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        // Usamos FileReader con UTF-8 para leer correctamente caracteres especiales como en 'PÃ¡l FejÅ‘s'
        try (Reader in = new FileReader(rutaArchivo, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(in, format)) {

            for (CSVRecord record : parser) {
                procesadoPorFila(record, personasHashTable);
            }

        } catch (IOException e) {
            System.err.println("ERROR de I/O al leer el archivo CSV de créditos: " + e.getMessage());
            e.printStackTrace();
        }

        // Se devuelve la tabla hash ya poblada.
        return personasHashTable;
    }

    /**
     * Procesa una única fila del CSV, extrayendo el ID de la película y los datos de cast y crew.
     */
    private void procesadoPorFila(CSVRecord record, HashTable<Integer, Persona> personasHashTable) {
        try {
            int movieId = Integer.parseInt(record.get("id"));
            String castData = record.get("cast");
            String crewData = record.get("crew");

            // Procesamos la columna 'crew' para encontrar al director
            procesarCrew(crewData, movieId, personasHashTable);

            // Procesamos la columna 'cast' para encontrar a los actores
            procesarCast(castData, movieId, personasHashTable);

        } catch (NumberFormatException e) {
            System.err.println("ERROR: No se pudo parsear el ID de la película en el registro: " + record.getRecordNumber());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Columna esperada ('id', 'cast' o 'crew') no encontrada en el registro: " + record.getRecordNumber());
        }
    }

    /**
     * Parsea la columna 'crew' para encontrar únicamente a la persona con el trabajo ('job') de 'Director'.
     * Si la encuentra, la añade o actualiza en la tabla hash de personas.
     */
    private void procesarCrew(String crewData, int movieId, HashTable<Integer, Persona> personasHashTable) {
        if (crewData == null || crewData.isEmpty()) return;

        Matcher crewMatcher = CREW_MEMBER_PATTERN.matcher(crewData);
        while (crewMatcher.find()) {
            String crewMemberString = crewMatcher.group();

            Matcher jobMatcher = JOB_PATTERN.matcher(crewMemberString);
            // Solo nos interesa si el trabajo es 'Director'
            if (jobMatcher.find() && "Director".equals(jobMatcher.group(1))) {
                Matcher idMatcher = ID_PATTERN.matcher(crewMemberString);
                Matcher nameMatcher = NAME_PATTERN.matcher(crewMemberString);

                if (idMatcher.find() && nameMatcher.find()) {
                    int personaId = Integer.parseInt(idMatcher.group(1));
                    String nombre = nameMatcher.group(1);

                    // Reutiliza o crea la persona
                    Persona director = personasHashTable.obtener(personaId);
                    if (director == null) {
                        director = new Persona(personaId, nombre);
                        try {
                            personasHashTable.insertar(personaId, director);
                        } catch (ElementoYaExistenteException e) {
                            System.err.println("Error de concurrencia/lógica al insertar director: " + personaId);
                        }
                    }
                    // Añadimos la película a su lista de películas dirigidas
                    director.getIdsPeliculasDirigidas().add(movieId);
                    break; // Asumimos un solo director por película para simplificar
                }
            }
        }
    }

    /**
     * Parsea la columna 'cast' para encontrar a todos los actores.
     * Para cada uno, lo añade o actualiza en la tabla hash de personas.
     */
    private void procesarCast(String castData, int movieId, HashTable<Integer, Persona> personasHashTable) {
        if (castData == null || castData.isEmpty()) return;

        Matcher castMatcher = CAST_MEMBER_PATTERN.matcher(castData);
        while (castMatcher.find()) {
            String castMemberString = castMatcher.group();

            Matcher idMatcher = ID_PATTERN.matcher(castMemberString);
            Matcher nameMatcher = NAME_PATTERN.matcher(castMemberString);

            if (idMatcher.find() && nameMatcher.find()) {
                int personaId = Integer.parseInt(idMatcher.group(1));
                String nombre = nameMatcher.group(1).replace("\"", ""); // Limpia comillas dobles si existen

                // Reutiliza o crea la persona
                Persona actor = personasHashTable.obtener(personaId);
                if (actor == null) {
                    actor = new Persona(personaId, nombre);
                    try {
                        personasHashTable.insertar(personaId, actor);
                    } catch (ElementoYaExistenteException e) {
                        // No debería ocurrir
                    }
                }
                // Añadimos la película a su lista de películas en las que actuó
                actor.getIdsPeliculasActuo().add(movieId);
            }
        }
    }
}
