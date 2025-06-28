package um.edu.uy.cargadoresDeData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import um.edu.uy.clases.Persona;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargadorDeCredits {

    // Patrones robustecidos para manejar las inconsistencias del CSV
    private static final Pattern PATRON_MEMBER = Pattern.compile("\\{.*?\\}");
    private static final Pattern PATRON_ID = Pattern.compile("['\"]id['\"]\\s*:\\s*(\\d+)");
    private static final Pattern PATRON_NOMBRE = Pattern.compile("['\"]name['\"]\\s*:\\s*(?:'((?:\\\\.|[^'])*)'|\"((?:\\\\.|[^\"])*)\")");
    private static final Pattern PATRON_JOB = Pattern.compile("['\"]job['\"]\\s*:\\s*(?:'((?:\\\\.|[^'])*)'|\"((?:\\\\.|[^\"])*)\")");
    private static final Pattern PATRON_CHARACTER = Pattern.compile("['\"]character['\"]\\s*:");

    public void cargarCredits(String rutaArchivo, HashTable<Integer, Persona> personasHashTable, HashTable<Integer, MiLista<Persona>> actoresPorPelicula) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        try (Reader in = new FileReader(rutaArchivo, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(in, format)) {
            for (CSVRecord record : parser) {
                procesadoPorFila(record, personasHashTable, actoresPorPelicula);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de créditos: " + e.getMessage()); // tiro esto aca pq no me lo lee avces asi me entero
        }
    }

    private void procesadoPorFila(CSVRecord record, HashTable<Integer, Persona> personasHashTable, HashTable<Integer, MiLista<Persona>> actoresPorPelicula) {
        try {
            int movieId = Integer.parseInt(record.get("id"));
            String castData = record.get("cast");
            String crewData = record.get("crew");

            procesarCrew(crewData, movieId, personasHashTable);
            procesarCast(castData, movieId, personasHashTable, actoresPorPelicula);
        } catch (IllegalArgumentException ignored) { //tiro esto aca porque ese catch me rompe todito
            // Se ignoran filas con formato incorrecto
        }
    }

    private void procesarCrew(String crewData, int movieId, HashTable<Integer, Persona> personasHashTable) {
        if (crewData == null || crewData.isEmpty() || crewData.equals("[]")) return;

        Matcher crewMatcher = PATRON_MEMBER.matcher(crewData);
        while (crewMatcher.find()) {
            String crewMemberString = crewMatcher.group();
            Matcher jobMatcher = PATRON_JOB.matcher(crewMemberString);

            if (jobMatcher.find()) {
                String job;

                if (jobMatcher.group(1) != null) {
                    job = jobMatcher.group(1);
                } else {
                    job = jobMatcher.group(2);
                }

                if ("Director".equalsIgnoreCase(job)) {
                    Matcher idMatcher = PATRON_ID.matcher(crewMemberString);
                    Matcher nameMatcher = PATRON_NOMBRE.matcher(crewMemberString);

                    if (idMatcher.find() && nameMatcher.find()) {
                        int personaId = Integer.parseInt(idMatcher.group(1));
                        String nombre;
                        // Bloque if-else para obtener el valor de 'name'
                        if (nameMatcher.group(1) != null) {
                            nombre = nameMatcher.group(1);
                        } else {
                            nombre = nameMatcher.group(2);
                        }

                        if (personaId <= 0 || nombre == null || nombre.trim().isEmpty() || nombre.equalsIgnoreCase("None")) continue;

                        Persona director = personasHashTable.obtener(personaId);
                        if (director == null) {
                            director = new Persona(personaId, nombre);
                            try {
                                personasHashTable.insertar(personaId, director);
                            } catch (ElementoYaExistenteException ignored) {}
                        }
                        director.getIdsPeliculasDirigidas().add(movieId);
                    }
                }
            }
        }
    }

    private void procesarCast(String castData, int movieId, HashTable<Integer, Persona> personasHashTable, HashTable<Integer, MiLista<Persona>> actoresPorPelicula) {
        if (castData == null || castData.isEmpty() || castData.equals("[]")) return;

        Matcher castMatcher = PATRON_MEMBER.matcher(castData);
        while (castMatcher.find()) {
            String castMemberString = castMatcher.group();
            if (!PATRON_CHARACTER.matcher(castMemberString).find()) continue;

            Matcher idMatcher = PATRON_ID.matcher(castMemberString);
            Matcher nameMatcher = PATRON_NOMBRE.matcher(castMemberString);

            if (idMatcher.find() && nameMatcher.find()) {
                int personaId = Integer.parseInt(idMatcher.group(1));
                String nombre;
                // Bloque if-else para obtener el valor de 'name'
                if (nameMatcher.group(1) != null) {
                    nombre = nameMatcher.group(1);
                } else {
                    nombre = nameMatcher.group(2);
                }

                if (personaId <= 0 || nombre == null || nombre.trim().isEmpty() || nombre.equalsIgnoreCase("None")) continue;

                Persona actor = personasHashTable.obtener(personaId);
                if (actor == null) {
                    actor = new Persona(personaId, nombre);
                    try {
                        personasHashTable.insertar(personaId, actor);
                    } catch (ElementoYaExistenteException ignored) {}
                }
                actor.getIdsPeliculasActuo().add(movieId);

                // Lógica para poblar el índice de actores por película
                MiLista<Persona> listaActores = actoresPorPelicula.obtener(movieId);
                if (listaActores == null) {
                    listaActores = new MiArrayList<>();
                    try {
                        actoresPorPelicula.insertar(movieId, listaActores);
                    } catch (ElementoYaExistenteException ignored) {}
                }
                listaActores.add(actor);
            }
        }
    }
}
