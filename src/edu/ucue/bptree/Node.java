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
    private Node next;
    
    private final K[] keys; // Claves
    private final V[] values; // Valores
    private final int keysNumber; // Número máximo de claves
    private final Comparator<K> comparator;

    public Node(boolean leaf, int keysNumber, Comparator<K> comparator) {
        this.leaf = leaf;
        this.nodeSize = 0;
        this.keysNumber = keysNumber;
        this.keys = (K[]) new Object[this.keysNumber];
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
    
    public Object getValue(int index){
        return values[index];
    }

    public Object[] getValues() {
        return values;
    }
    
    public K getKey(int index){
        return keys[index];
    }

    public K[] getKeys() {
        return keys;
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
}
