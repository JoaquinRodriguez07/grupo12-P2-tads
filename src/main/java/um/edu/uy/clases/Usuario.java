package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;
import um.edu.uy.excepciones.ElementoNoExistenteException;
import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.arraylist.MiArrayList;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;




import um.edu.uy.tadsAuxiliares.hashtable.HashCerradaLineal;

@Getter
@Setter
public class Usuario implements Comparable<Usuario> {
    private int id;
    private MiLista<Calificacion> calificacionesDelUsuario;
    private HashCerradaLineal<String, Integer> calificacionesPorGenero;

    public Usuario(int id) {
        this.id = id;
        this.calificacionesDelUsuario = new MiArrayList<>();
        this.calificacionesPorGenero = new HashCerradaLineal<>();
    }

    public void agregarCalificacionPorGenero(String genero) {
        Integer contador = this.calificacionesPorGenero.obtener(genero);
        if (contador == null) {
            try{
                this.calificacionesPorGenero.insertar(genero, 1);
            }
            catch (ElementoYaExistenteException e) {
                System.err.println(e.getMessage());
            }
        }else{
            try{
                this.calificacionesPorGenero.actualizar(genero, contador + 1);
            }
            catch (ElementoNoExistenteException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public int compareTo(Usuario o) {
        return Integer.compare(this.id, o.id);
    }
}
