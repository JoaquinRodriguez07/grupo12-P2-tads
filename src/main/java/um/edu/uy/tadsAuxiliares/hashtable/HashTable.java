package um.edu.uy.tadsAuxiliares.hashtable;

import um.edu.uy.excepciones.ElementoYaExistenteException;
import um.edu.uy.tadsAuxiliares.arraylist.MiLista;

public interface HashTable<K,T> {

    public void insertar(K clave, T valor) throws ElementoYaExistenteException;
    public boolean pertenece(K clave);
    public void borrar(K clave);
    public void reestructurar();

    T obtener(K clave);

    MiLista<T> getValores();

    int tamanio();
}
