/**
 * Tree / Árbol
 */
package edu.ucue.bptree;

/**
 *
 * @author Santos Gallegos
 */
public class Tree<K> {
    private Node<K> root; // Nodo raíz
    private final int keysNumber; // Número máximo de claves

    /**
     * Crea un nuevo árbol vacío.
     * @param KeysNumber 
     */
    public Tree(int KeysNumber) {
        this.keysNumber = KeysNumber;
        this.root = new Node(true, null, keysNumber);
    }
}
