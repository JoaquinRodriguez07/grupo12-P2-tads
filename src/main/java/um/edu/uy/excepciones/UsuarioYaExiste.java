package um.edu.uy.excepciones;

public class UsuarioYaExiste extends Exception {
    public UsuarioYaExiste(String message) {
        super(message);
    }
}
