package um.edu.uy.tadsAuxiliares.hashtable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Objeto<K extends Comparable<K>, T> implements Comparable<Objeto<K, T>> {

    private K clave;
    private T valor;

    @Override
    public int compareTo(Objeto<K, T> otro) {
        return this.clave.compareTo(otro.getClave());
    }
}
