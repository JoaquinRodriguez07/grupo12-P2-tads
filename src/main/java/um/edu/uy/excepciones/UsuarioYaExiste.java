package um.edu.uy.excepciones;

public class UsuarioYaExiste extends RuntimeException {
  public UsuarioYaExiste(String message) {
    super(message);
  }
}
