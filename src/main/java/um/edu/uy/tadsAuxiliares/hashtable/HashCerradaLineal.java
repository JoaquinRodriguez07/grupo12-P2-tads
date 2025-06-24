package um.edu.uy.tadsAuxiliares.hashtable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import um.edu.uy.excepciones.ElementoYaExistenteException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HashCerradaLineal<K extends Comparable<K>, T> implements HashTable<K, T> {

    private Objeto<K, T>[] tabla;
    private int capacidad;
    private int size;
    private double maxFactorCarga = 0.7;

    @SuppressWarnings("unchecked")
    public HashCerradaLineal(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = (Objeto<K, T>[]) new Objeto[capacidad];
        this.size = 0;
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

            if (entrada == null) {
                tabla[indice] = new Objeto<>(clave, valor);
                size++;
                return;
            } else if (entrada.getClave().equals(clave)) {
                throw new ElementoYaExistenteException("La clave ya existe: " + clave);
            }
            // Continua con la búsqueda lineal si hay colisión
        }

        throw new RuntimeException("Tabla llena, no se pudo insertar");
    }

    @Override
    public boolean pertenece(K clave) {
        int hashInicial = hash(clave);

        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];

            if (entrada == null) return false;
            if (entrada.getClave().equals(clave)) return true;
        }

        return false;
    }

    @Override
    public void borrar(K clave) {
        int hashInicial = hash(clave);

        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];

            if (entrada == null) return;

            if (entrada.getClave().equals(clave)) {
                tabla[indice] = null;
                size--;
                return;
            }
        }
    }

    @Override
    public void reestructurar() {
        int nuevaCapacidad = siguientePrimo(capacidad * 2);
        Objeto<K, T>[] tablaVieja = tabla;

        @SuppressWarnings("unchecked")
        Objeto<K, T>[] nuevaTabla = (Objeto<K, T>[]) new Objeto[nuevaCapacidad];

        tabla = nuevaTabla;
        int viejaCapacidad = capacidad;
        capacidad = nuevaCapacidad;
        size = 0;

        for (int i = 0; i < viejaCapacidad; i++) {
            if (tablaVieja[i] != null) {
                try {
                    insertar(tablaVieja[i].getClave(), tablaVieja[i].getValor());
                } catch (ElementoYaExistenteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public T obtener(K clave) {
        int hashInicial = hash(clave);

        for (int i = 0; i < capacidad; i++) {
            int indice = (hashInicial + i) % capacidad;
            Objeto<K, T> entrada = tabla[indice];

            if (entrada == null) return null;
            if (entrada.getClave().equals(clave)) return entrada.getValor();
        }

        return null;
    }

    @Override
    public int tamanio() {
        return size;
    }



    // Funciones auxiliares

    private int siguientePrimo(int n) {
        while (!esPrimo(n)) {
            n++;
        }
        return n;
    }

    private boolean esPrimo(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int hash(K clave) {
        return Math.abs(clave.hashCode()) % capacidad;
    }
}