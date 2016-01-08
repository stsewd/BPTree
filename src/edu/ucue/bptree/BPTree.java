/**
 * Tree / Árbol
 */
package edu.ucue.bptree;

import java.util.Comparator;

/**
 * Árbol B+.
 * @author Santos Gallegos
 * @param <K> Clave
 * @param <V> Valor
 */
public class BPTree<K, V> {
    private Node<K, V> root; // Nodo raíz
    private final int keysNumber; // Número máximo de claves u órden de árbol
    private final Comparator<K> comparator; // Objeto que provee el método para comparar las claves
    /**
     * Crea un nuevo árbol vacío.
     * @param KeysNumber Número máximo de claves.
     * @param comparator Objeto necesario para comparar las claves.
     */
    public BPTree(int KeysNumber, Comparator<K> comparator) {
        this.keysNumber = KeysNumber;
        this.comparator = comparator;
        this.root = new Node<>(true, keysNumber, comparator);
    }
    
    public void add(K key, V value){
        add(key, value, root);
        /*
        Node tmpRoot = root;
        
        // Comprobar si la raíz está llena
        if(root.getNodeSize() == keysNumber){
            //Ubicar nueva clave
            //Dividir nodo izq, medio, der
            //Crear nuevo nodo con clave medio
        }else {
            insert(root, key, value);
        }
        */
    }
    
    private void add(K key, V value, Node node){
        Node leaf = node;
        int i;
        while(!leaf.isLeaf()){
            i = 0;
            while(i <  node.getNodeSize() && comparator.compare(key, (K) node.getKey(i)) >= 0){
                i++;
            }
            leaf = leaf.getChild(i);
        }
        if(leaf.getNodeSize() == keysNumber){
            
        }else {
            leaf.insert(key, value);
        }
    }
    
    public Node<K, V> getRoot() {
        return root;
    }

    private void insert(Node node, K key, V value) {
        int i = node.getNodeSize() - 1;
        if(node.isLeaf()){
            //node.insert(key, value);
            while(i >= 0 && comparator.compare(key, (K) node.getKey(i)) < 0){
                node.setKey(i + 1, node.getKey(i));
                node.setValue(i + 1, node.getValue(i));
                i--;
            }
            node.setKey(i + 1, key);
            node.setValue(i + 1, value);
            node.setNodeSize(node.getNodeSize() + 1);
        }else {
        }
    }
    
    private void split() {
    }
    
    public V search(K key){
        return search(key, root);
    }
    
    private V search(K key, Node node){
        if(node.isLeaf()){
            for(int i = 0; i < node.getNodeSize(); i++){
                if(comparator.compare(key, (K) node.getKey(i)) == 0)
                    return (V) node.getValue(i);
            }
        }else {
            int i = 0;
            while(i <  node.getNodeSize() && comparator.compare(key, (K) node.getKey(i)) >= 0){
                i++;
            }
            return search(key, node.getChild(i));
        }
        return null;
    }

    /**
     * @return the comparator
     */
    public Comparator<K> getComparator() {
        return comparator;
    }
    
    @Override
    public String toString() {
        String str = "";
        return toString(root, 0);
    }
    
    private String toString(Node node, int index){
        if(index >= node.getNodeSize()){
            return "\n";
        }
        String str = "";
        str += node.getKey(index) + " ";
        
        if(!node.isLeaf())
            str += toString((Node) node.getValue(index), 0);
        str += toString(node, index + 1);
        return str;
    }
}
