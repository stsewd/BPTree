/**
 * Node / Nodo
 */
package edu.ucue.bptree;

/**
 *
 * @author Santos Gallegos
 */
public class Node<K> {
    private boolean leaf; // Hoja
    
    private int nodeSize; // Tamaño actual del nodo
    
    private K[] keys; // Claves
    private Object[] values; // Valores
    
    private Node next; // Nodo siguiente
    private Node prev; // Nodo anterior

    public Node(boolean leaf, Node next, Node prev, int keysNumber) {
        this.leaf = leaf;
        this.nodeSize = 0;
        this.keys = (K[]) new Object[keysNumber];
        this.values = new Object[keysNumber];
        this.next = next;
        this.prev = prev;
    }    
}
