package um.edu.uy.tadsAuxiliares.hashtable;

import um.edu.uy.excepciones.ElementoYaExistenteException;
public interface HashTable<K,T> {

    public void insertar(K clave, T valor) throws ElementoYaExistenteException;
    public boolean pertenece(K clave);
    public void borrar(K clave);
    public void reestructurar();

}
