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

    public Node(boolean leaf, Node next, int keysNumber) {
        this.leaf = leaf;
        this.nodeSize = 0;
        this.keysNumber = keysNumber;
        this.keys = (K[]) new Object[this.keysNumber];
        this.values = new Object[this.keysNumber + 1];
        setNext(next);
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
    
    public Object getValue(int index){
        return values[index];
    }
    
    public K getKey(int index){
        return keys[index];
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
