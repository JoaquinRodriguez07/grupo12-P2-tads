package um.edu.uy.clases;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private int id;

    public Usuario(int id) {
        this.id = id;
    }
}
