package um.edu.uy.cargadoresDeData; 

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import um.edu.uy.clases.Peliculas;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;
import um.edu.uy.tadsAuxiliares.hashtable.HashTable;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher; //Esto me funca para hacer regular expression que me sirven para buscar solo lo que necesito en vez de buscar segun posicion
import java.util.regex.Pattern; //Esto me funca para hacer regular expression que me sirven para buscar solo lo que necesito en vez de buscar segun posicion

public class CargadorDeMovies { 

    private static final Pattern nombreColeccion = Pattern.compile("'name':\\s*'([^']*)'"); //lo puse como final pq no me va a cambar nunca y creo que es buena practica :)

    
    public HashTable<Integer, Peliculas> cargadorMoviesAHash(String rutaArchivo) {

        HashCerradaLineal<Integer, Peliculas> moviesHashTable = new HashCerradaLineal<>(64907); // Usar un primo cercano a 45433/0.7
        CSVFormat format = CSVFormat.DEFAULT.builder() // Usa el formato CSV por defecto (comas como separadores)
                .setHeader()             // Indica que la primera línea del CSV es un encabezado.
                .setSkipHeaderRecord(true) // Le dice al parser que no te devuelva la línea del encabezado como un registro de datos.
                .setIgnoreHeaderCase(true) //  Permite que los nombres de las columnas  no le importe si esta en mayus o minus ( Por ejemplo, "ID" y "id" se tratarán como la misma columna)
                .setTrim(true)           // Elimina espacios en blanco iniciales/finales de cada campo leído.
                .build();                // Construye la configuración final del formato.

        try (Reader in = new FileReader(rutaArchivo);
             CSVParser parser = new CSVParser(in, format)) {
            for (CSVRecord record : parser) {
                procesadoDeLaFila(record, moviesHashTable);
            }
        } catch (IOException e) {
            System.out.println("Error de I/O al leer el archivo CSV: " + rutaArchivo + " - " + e.getMessage());
            e.printStackTrace();
        }
        return moviesHashTable;
    }


    private void procesadoDeLaFila(CSVRecord record, HashCerradaLineal<Integer, Peliculas> moviesHashTable) {   // Procesa un único registro (fila) del CSV y lo intenta insertar en la tabla hash. El record es la Fila.
        try {
            String idPreParse = record.get("id"); // Obtiene el valor de la columna "id" usando el mapeo del encabezado.
            if (idPreParse == null || idPreParse.trim().isEmpty()) {
                System.out.println("Advertencia: Registro con ID vacío o nulo. Se ignora registro: " + record.toString());
                return;
            }

            // Extracción de otros Campos
            String titulo = record.get("title");
            String idiomaOriginal = record.get("original_language");
            int idParseada = Integer.parseInt(record.get("id"));
            // 3. Parseo de Campos Complejos con Métodos Auxiliares

            // Llamamos a métodos privados dedicados para manejar la lógica de los JSON
            String coleccion = parseCollection(record.get("belongs_to_collection"));
            double ingresos = parseRevenue(record.get("revenue"), idPreParse); // Pasamos el ID para un mejor mensaje de error si falla

            // Con todos los datos extraídos y procesados, se crea una nueva instancia de Peliculas.
            Peliculas pelicula = new Peliculas(idParseada, titulo, idiomaOriginal, coleccion, ingresos);

            // Intento de Inserción en la Tabla Hash
            try {
                moviesHashTable.insertar(pelicula.getId(), pelicula); // Intentamos insertar la película.
            } catch (ElementoYaExistenteException e) {
                // Si la película ya existe (ID duplicado), se captura la excepción de la tabla hash.
                System.out.println(" Película con ID " + idPreParse + " ya existe. NO SE AGREGA!");
            }

        } catch (IllegalArgumentException e) {
            // 6Manejo de Errores de Columnas Faltantes
            // Esto indica un problema con la estructura del archivo.
            System.out.println("ERROR: Columna esperada no encontrada en registro. Fila: " + record.getRecordNumber() + ", Datos: " + record.toString() + ". Error: " + e.getMessage());
        }
    }


    private String parseCollection(String jsonSucio) {
        // Si la cadena es nula, vacía o "False", no hay nombre de colección.
        if (jsonSucio == null || jsonSucio.trim().isEmpty() || jsonSucio.trim().equals("False")) {
            return "";
        }
        //aca es donde entran las regular expression (el java.regex)
        Matcher matcher = nombreColeccion.matcher(jsonSucio);  // Esta línea crea un objeto 'Matcher' que intenta encontrar el patrón de la regex dentro de 'collectionRaw'.

        if (matcher.find()) {
            return matcher.group(1); // 'matcher.group(1)' devuelve el texto que fue capturado por el primer grupo de paréntesis en la regex.
            // En la regex "'name':\\s*'([^']*)'", el grupo 1 es '([^']*)'.
            // Esto significa:
            // - Busca literalmente algo que diga : 'name':
            // - Luego, puede haber cualquier cantidad de espacios en blanco (\\s*)
            // - Luego, busca una comilla simple abierta (')
            // -  '([^']*)' captura cualquier carácter que NO sea una comilla simple (^)
            //   cero o más veces (*) hasta que encuentra la siguiente comilla simple (').
            //   Esto es precisamente el nombre de la colección.
            // Ejemplo: Si collectionRaw es "{'id': 1, 'name': 'Toy Story Collection', ...}"
            // matcher.group(1) devolvería: "Toy Story Collection"
        }
        return ""; // Si el patrón no se encuentra, devuelve una cadena vacía.
    }

    private double parseRevenue(String revenueStr, String movieId) {
        // Si la cadena no es nula ni vacía, intenta parsearla.
        if (revenueStr != null && !revenueStr.trim().isEmpty()) {
            try {
                return Double.parseDouble(revenueStr.trim()); // Convierte la cadena a un número double.
            } catch (NumberFormatException e) {
                // Si la cadena no puede convertirse a double, imprime una advertencia.
                System.out.println("Advertencia: No se pudo parsear 'revenue' para ID " + movieId + ". Valor: '" + revenueStr + "'. Estableciendo a 0.0");
            }
        }
        return 0.0; // Si la cadena es nula, vacía o no parseable, devuelve 0.0.
    }
}