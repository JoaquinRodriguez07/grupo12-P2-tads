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

    private static final Pattern patronMember = Pattern.compile("\\{.*?\\}");
    private static final Pattern patronId = Pattern.compile("['\"]id['\"]\\s*:\\s*(\\d+)");
    private static final Pattern patronNombre = Pattern.compile("['\"]name['\"]\\s*:\\s*(?:'((?:\\\\.|[^'])*)'|\"((?:\\\\.|[^\"])*)\")");
    private static final Pattern patronJob = Pattern.compile("['\"]job['\"]\\s*:\\s*(?:'((?:\\\\.|[^'])*)'|\"((?:\\\\.|[^\"])*)\")");
    private static final Pattern patronPersonaje = Pattern.compile("['\"]character['\"]\\s*:");

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
        }
    }

    private void procesarCrew(String crewData, int movieId, HashTable<Integer, Persona> personasHashTable) {
        if (crewData == null || crewData.isEmpty() || crewData.equals("[]")) return;

        Matcher crewMatcher = patronMember.matcher(crewData);
        while (crewMatcher.find()) {
            String crewMemberString = crewMatcher.group();
            Matcher jobMatcher = patronJob.matcher(crewMemberString);

            if (jobMatcher.find()) {
                String job;

                if (jobMatcher.group(1) != null) {
                    job = jobMatcher.group(1);
                } else {
                    job = jobMatcher.group(2);
                }

                if ("Director".equalsIgnoreCase(job)) {
                    Matcher idMatcher = patronId.matcher(crewMemberString);
                    Matcher nameMatcher = patronNombre.matcher(crewMemberString);

                    if (idMatcher.find() && nameMatcher.find()) {
                        int personaId = Integer.parseInt(idMatcher.group(1));
                        String nombre;

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

        Matcher castMatcher = patronMember.matcher(castData);
        while (castMatcher.find()) {
            String castMemberString = castMatcher.group();
            if (!patronPersonaje.matcher(castMemberString).find()) continue; //

            Matcher idMatcher = patronId.matcher(castMemberString);
            Matcher nameMatcher = patronNombre.matcher(castMemberString);

            if (idMatcher.find() && nameMatcher.find()) {
                int personaId = Integer.parseInt(idMatcher.group(1));
                String nombre;
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
