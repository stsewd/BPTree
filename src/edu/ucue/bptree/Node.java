/**
 * Node / Nodo
 */
package edu.ucue.bptree;

/**
 *
 * @author Santos Gallegos
 */
public final class Node<K> {
    private boolean leaf; // Hoja
    
    private int nodeSize; // Tamaño actual del nodo
    
    private K[] keys; // Claves
    private Object[] values; // Valores
    private final int keysNumber; // Número máximo de claves
    //private Node next; // Nodo siguiente
    //private Node prev; // Nodo anterior

    public Node(boolean leaf, Node next, int keysNumber) {
        this.leaf = leaf;
        this.nodeSize = 0;
        this.keysNumber = keysNumber;
        this.keys = (K[]) new Object[this.keysNumber];
        this.values = new Object[this.keysNumber + 1];
        setNext(next);
        //this.next = next;
        //this.prev = prev;
    }
    
    /**
     * Retorna el siguiente nodo hoja (lista de claves).
     * @return 
     */
    public Node next(){
        return (Node) values[keysNumber];
    }
    
    public void setNext(Node node){
        values[keysNumber] = node;
    }
}
