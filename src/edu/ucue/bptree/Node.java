/**
 * Node / Nodo
 */
package edu.ucue.bptree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author Santos Gallegos
 * @param <K> Clave
 */
public final class Node<K> implements Serializable {
    private final boolean leaf; // Hoja
    
    private int nodeSize; // Tamaño actual del nodo
    private Long parent; // Posicion dentro del archivo donde esta el padre
    private Long next; 
    private Long prev;
    
    private Long pos; // Posicion del nodo dentro del archivo.
    private final K[] keys; // Claves
    private final Long[] children; // hijos (valores en caso que sea hoja)
    // private final Long[] values; // Valores
    
    private final int keysNumber; // Número máximo de claves
    private final Comparator<K> comparator; // Comparador de claves

    public Node(boolean leaf, int keysNumber, Comparator<K> comparator) {
        this.leaf = leaf;
        this.nodeSize = 0;
        this.keysNumber = keysNumber;
        this.keys = (K[]) new Object[this.keysNumber + 1];
        this.comparator = comparator;
        this.parent = null;
        if(leaf){
            // this.values = new Long[this.keysNumber + 1];
            // this.children = null;
            this.children = new Long[this.keysNumber + 1];
        }else {
            // this.values = null;
            this.children = new Long[this.keysNumber + 2];
        }
    }

    /**
     * Cambia la posicion del nodo.
     * @param pos 
     */
    public void setPos(Long pos) {
        this.pos = pos;
    }
    
    /**
     * 
     * @return posicion del nodo
     */
    public Long getPos() {
        return pos;
    }
    
    /**
     * 
     * @return siguiente nodo hoja/interno.
     */
    public Long next(){
        return next;
    }
    
    /**
     * 
     * @return anterior nodo hoja/interno.
     */
    public Long prev(){
        return prev;
    }
    
    /**
     * Set the next
     * @param node 
     */
    public void setNext(Long node){
        this.next = node;
    }

    /**
     * Set the prev
     * @param prev 
     */
    public void setPrev(Long prev) {
        this.prev = prev;
    }

    /**
     * Cambia el tamaño actual del nodo (número de claves).
     * @param nodeSize 
     */
    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }
    
    public Long getValue(int index){
        // return values[index];
        return children[index];
    }

    public void setValue(int index, Long value){
        // values[index] = value;
        children[index] = value;
    }
    
    public K getKey(int index){
        return keys[index];
    }
    
    public void setKey(int index, K key){
        keys[index] = key;
    }
    
    public Long getChild(int index){
        return children[index];
    }
    
    public void setChild(int index, Long child){
        children[index] = child;
    }

    /**
     * 
     * @return Tamaño actual del nodo (número de claves).
     */
    public int getNodeSize() {
        return nodeSize;
    }

    public boolean isLeaf() {
        return leaf;
    }
    
    /**
     * Inserta una clave con su respectivo valor.
     * @param key
     * @param value 
     */
    public void insertValue(K key, Long value){
        int i = getNodeSize() - 1;
        while(i >= 0 && comparator.compare(key, getKey(i)) < 0){
            keys[i + 1] = keys[i];
            // values[i + 1] = values[i];
            children[i + 1] = children[i];
            i--;
        }
        keys[i + 1] = key;
        // values[i + 1] = value;
        children[i + 1] = value;
        nodeSize++;
    }
    
    /**
     * Inserta una clave junto a su hijo (ubicado a la derecha de la clave).
     * @param key
     * @param child 
     */
    public void insertChild(K key, Long child){
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
     * Elimina una clave; si el nodo es hoja, se elimina su valor;
     * si el nodo no es hoja (interno), se elimina su hijo ubicado
     * a la derecha del nodo.
     * @param key 
     */
    public void remove(K key){
        int i;
        for(i = 0; i < getNodeSize(); i++){
            if(comparator.compare(keys[i], key) == 0)
                break;
        }
        
        if(i >= getNodeSize())
            return;
        
        for(int j = i + 1; j < getNodeSize(); j++){
            keys[j - 1] = keys[j];
            if(leaf){
                // values[j -1] = values[j];
                children[j -1] = children[j];
            }else {
                children[j] = children[j + 1];
            }
        }
        nodeSize--;
    }

    /**
     * @return the parent
     */
    public Long getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Long parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < getNodeSize(); i++)
            str += keys[i] + " ";
        return str;
    }

    /**
     * Retorna una coleccion con todos los valores del nodo en orden.
     * @return 
     */
    public Collection<Long> values() {
        ArrayList<Long> v = new ArrayList<>();
        for(int i = 0; i < nodeSize; i++)
            // v.add(values[i]);
            v.add(children[i]);
        return v;
    }
}
