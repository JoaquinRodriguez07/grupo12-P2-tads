package um.edu.uy.cargadoresDeData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import um.edu.uy.clases.Calificaciones;
import um.edu.uy.clases.Peliculas;
import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;

//import java.time.Date;


public class CargadorDeCalificaciones {


    HashCerradaLineal<Integer, Peliculas>  calificacionesHash = new HashCerradaLineal<>(142200); //cant de calificaciones / factor de carga (0.7) // esto se debe arreglar apra que no sea fijo hay que buscar como

    CSVFormat format = CSVFormat.DEFAULT.builder() // Usa el formato CSV por defecto (comas como separadores)
            .setHeader()             // Indica que la primera línea del CSV es un encabezado.
            .setSkipHeaderRecord(true) // Le dice al parser que no te devuelva la línea del encabezado como un registro de datos.
            .setIgnoreHeaderCase(true) //  Permite que los nombres de las columnas  no le importe si esta en mayus o minus ( Por ejemplo, "ID" y "id" se tratarán como la misma columna)
            .setTrim(true)           // Elimina espacios en blanco iniciales/finales de cada campo leído.
            .build();                // Construye la configuración final del formato.


    private void procesadoDeLaFila(CSVRecord record, HashCerradaLineal<Integer, Peliculas> calificacionesHash) {   // Procesa un único registro (fila) del CSV y lo intenta insertar en la tabla hash. El record es la Fila.

        try {
            String id = record.get(0); // Obtiene el valor de la columna "id" usando el mapeo del encabezado.
            if (id == null || id.trim().isEmpty()) {
                System.err.println("Advertencia: Registro con ID vacío o nulo. Se ignora registro: " + record.toString());
                return;
            }

            int userId = Integer.parseInt(record.get(1));
            // Extracción de otros Campos
            int idPelicula = Integer.parseInt(record.get(1));
            int rating = Integer.parseInt(record.get(3));
            String timeStamp = record.get(4);

            //pasar timestamp a fecha
//            Date fecha1 = new Date(timeStamp);
//            Calificaciones calificacion = new Calificaciones(userId, idPelicula, rating,);// Con todos los datos extraídos y procesados, se crea una nueva instancia de calificacion.
            //me fijo a que fecha pertence la review

//            Peliculas peliculaCalificacion = moviesHashTable.obtener(idPelicula);





        } catch (IllegalArgumentException e) {
            // 6Manejo de Errores de Columnas Faltantes
            System.err.println("ERROR: Columna esperada no encontrada en registro. Fila: " + record.getRecordNumber() + ", Datos: " + record.toString() + ". Error: " + e.getMessage());
        }
    }
}
