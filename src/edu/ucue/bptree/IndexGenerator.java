/**
 * Interfaz que permite definir qué campo(s) usar
 * como clave para los índices secundarios.
 */
package edu.ucue.bptree;

import java.util.Comparator;

/**
 * Clase para especificar que valor tomar de indice de un objeto dado,
 * util para especificar claves secundarias del arbol B+.
 * @author Santos Gallegos
 * @param <E> Tipo de objeto
 * @param <V> Tipo de dato del objeto que va a ser indexado.
 */
public interface IndexGenerator<E, V> {
    public V getKey(E obj);
    public Comparator<V> getComparator();
}
