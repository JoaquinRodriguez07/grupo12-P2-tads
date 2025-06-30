
package um.edu.uy.tadsAuxiliares.hashtable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import um.edu.uy.excepciones.ElementoNoExistenteException;
import um.edu.uy.excepciones.ElementoYaExistenteException;

import java.util.Iterator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HashCerradaLineal<K extends Comparable<K>, T> implements HashTable<K, T> {

    private Objeto<K, T>[] tabla;
    private int capacidad;
    private int size;
    private double maxFactorCarga = 0.7;

    private static final Objeto TOMBSTONE = new Objeto<>(null, null);

    @SuppressWarnings("unchecked")
    public HashCerradaLineal(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = (Objeto<K, T>[]) new Objeto[capacidad];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return new HashIterator<K,T>(this.tabla, this.capacidad, this.size, TOMBSTONE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<K> keys() {

        return () -> new KeyIterator<K,T>(this.tabla, this.capacidad, this.size, TOMBSTONE);
    }

    @Override
    public void insertar(K clave, T valor) throws ElementoYaExistenteException {
        if ((double) size / capacidad > maxFactorCarga) {
            reestructurar();
        }
        int hashInicial = hash(clave);
        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];
            if (entrada == null || entrada == TOMBSTONE) {
                tabla[indice] = new Objeto<>(clave, valor);
                size++;
                return;
            }
            if (entrada.getClave().equals(clave)) {
                throw new ElementoYaExistenteException("La clave ya existe: " + clave);
            }
        }
        throw new RuntimeException("Tabla llena, no se pudo insertar");
    }

    @Override
    public T obtener(K clave) {
        int hashInicial = hash(clave);
        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];
            if (entrada == null) return null;
            if (entrada == TOMBSTONE) continue;
            if (entrada.getClave().equals(clave)) return entrada.getValor();
        }
        return null;
    }

    @Override
    public void borrar(K clave) {
        int hashInicial = hash(clave);
        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];
            if (entrada == null) return;
            if (entrada == TOMBSTONE) continue;
            if (entrada.getClave().equals(clave)) {
                tabla[indice] = TOMBSTONE;
                size--;
                return;
            }
        }
    }

    @Override
    public void reestructurar() {
//        System.out.println("REESTRUCTURANDO TABLA HASH. Capacidad anterior: " + capacidad + ".");
        int nuevaCapacidad = siguientePrimo(capacidad * 2);
//        System.out.println("Nueva capacidad (primo): " + nuevaCapacidad);
        Objeto<K, T>[] tablaVieja = tabla;
        @SuppressWarnings("unchecked")
        Objeto<K, T>[] nuevaTabla = (Objeto<K, T>[]) new Objeto[nuevaCapacidad];
        tabla = nuevaTabla;
        int viejaCapacidad = capacidad;
        capacidad = nuevaCapacidad;
        size = 0;
        for (int i = 0; i < viejaCapacidad; i++) {
            if (tablaVieja[i] != null && tablaVieja[i] != TOMBSTONE) {
                try {
                    insertar(tablaVieja[i].getClave(), tablaVieja[i].getValor());
                } catch (ElementoYaExistenteException e) {
                    throw new RuntimeException("Error inesperado durante reestructuración: " + e.getMessage(), e);
                }
            }
        }
//        System.out.println("Reestructuración completada. Elementos reinsertados: " + size);
    }

    private int hash(K clave) {
        return (clave.hashCode() & 0x7fffffff) % capacidad;
    }

    @Override
    public boolean pertenece(K clave) {
        int hashInicial = hash(clave);
        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];
            if (entrada == null) return false;
            if (entrada == TOMBSTONE) continue;
            if (entrada.getClave().equals(clave)) return true;
        }
        return false;
    }



    @Override
    public int tamanio() {
        return size;
    }

    @Override
    public void actualizar(K clave, T nuevoValor) throws ElementoNoExistenteException {
        int hashInicial = hash(clave);
        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];
            if (entrada == null) {
                throw new ElementoNoExistenteException("La clave que se proporciono no tiene un valor que actualizar");
            }
            if (entrada == TOMBSTONE) continue;
            if (entrada.getClave().equals(clave)) {
                entrada.setValor(nuevoValor);
                return;
            }
        }
        throw new ElementoNoExistenteException("La clave no existe");
    }

    private int siguientePrimo(int n) {
        if (n % 2 == 0) n++;
        while (!esPrimo(n)) {
            n += 2;
        }
        return n;
    }

    private boolean esPrimo(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Objeto<K, T>> entries() {
        return () -> new EntryIterator<K,T>(this.tabla, this.capacidad, this.size, TOMBSTONE);
    }
}