
package um.edu.uy.tadsAuxiliares.hashtable;

import um.edu.uy.excepciones.ElementoNoExistenteException;
import um.edu.uy.excepciones.ElementoYaExistenteException;

public interface HashTable<K extends Comparable<K>, T> extends Iterable<T> {

    Iterable<K> keys();

    void insertar(K clave, T valor) throws ElementoYaExistenteException;
    boolean pertenece(K clave);
    void borrar(K clave);
    void reestructurar();
    T obtener(K clave);
    int tamanio();

    void actualizar(K clave, T nuevoValor) throws ElementoNoExistenteException;



    Iterable<Objeto<K, T>> entries();
}