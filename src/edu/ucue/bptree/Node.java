/**
 * Node / Nodo
 */
package edu.ucue.bptree;

import java.util.Comparator;

/**
 *
 * @author Santos Gallegos
 * @param <K> Clave
 * @param <V> Valor
 */
public final class Node<K, V> {
    private boolean leaf; // Hoja
    
    private int nodeSize; // Tamaño actual del nodo
    private Node parent;
    private Node next;
    
    private final K[] keys; // Claves
    private final Node[] children; // hijos
    private final V[] values; // Valores
    
    private final int keysNumber; // Número máximo de claves
    private final Comparator<K> comparator; // Comparador de claves

    public Node(boolean leaf, int keysNumber, Comparator<K> comparator) {
        this.leaf = leaf;
        this.nodeSize = 0;
        this.keysNumber = keysNumber;
        this.keys = (K[]) new Object[this.keysNumber + 1];
        this.children = new Node[this.keysNumber + 2];
        this.values = (V[]) new Object[this.keysNumber + 1];
        this.comparator = comparator;
    }
    
    /**
     * Retorna el siguiente nodo hoja (lista de claves).
     * @return 
     */
    public Node next(){
        return next;
    }
    
    public void setNext(Node node){
        this.next = node;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }
    
    public V getValue(int index){
        return values[index];
    }

    public void setValue(int index, V value){
        values[index] = value;
    }
    
    public K getKey(int index){
        return keys[index];
    }
    
    public void setKey(int index, K key){
        keys[index] = key;
    }
    
    public Node getChild(int index){
        return children[index];
    }
    
    public void setChild(int index, Node child){
        children[index] = child;
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
   
    public void insert(K key, V value){
        int i = getNodeSize() - 1;
        while(i >= 0 && comparator.compare(key, getKey(i)) < 0){
            keys[i + 1] = keys[i];
            values[i + 1] = values[i];
            i--;
        }
        keys[i + 1] = key;
        values[i + 1] = value;
        nodeSize++;
    }
    
    public void insert(K key, Node child){
        int i = getNodeSize() - 1;
        while(i >= 0 && comparator.compare(key, getKey(i)) < 0){
            keys[i + 1] = keys[i];
            children[i + 2] = children[i + 1];
            children[i + 1] = children[i];
            i--;
        }
        keys[i + 1] = key;
        children[i + 2] = child;
        nodeSize++;
    }

    /**
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < getNodeSize(); i++)
            str += keys[i] + " ";
        return str;
    }
}
