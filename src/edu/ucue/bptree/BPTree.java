/**
 * Tree / Árbol
 */
package edu.ucue.bptree;

import java.util.Comparator;

/**
 *
 * @author Santos Gallegos
 * @param <K>
 */
public class BPTree<K> {
    private Node<K> root; // Nodo raíz
    private final int keysNumber; // Número máximo de claves
    private final Comparator<K> comparator;
    /**
     * Crea un nuevo árbol vacío.
     * @param KeysNumber 
     * @param comparator 
     */
    public BPTree(int KeysNumber, Comparator<K> comparator) {
        this.keysNumber = KeysNumber;
        this.comparator = comparator;
        this.root = new Node(true, keysNumber, comparator);
    }
    
    public void add(K key, Object value){
        Node tmpRoot = root;
        
        // Comprobar si la raíz está llena
        if(tmpRoot.getNodeSize() == keysNumber){
            
        }else {
            insert(key, value, tmpRoot);
        }
    }

    public Node<K> getRoot() {
        return root;
    }

    @Override
    public String toString() {
        String str = "";
        return toString(root, 0);
    }
    
    private String toString(Node node, int indexKey){
        if(indexKey >= node.getNodeSize()){
            return "\n";
        }
        String str = "";
        str += node.getKey(indexKey) + " ";
        
        if(!node.isLeaf())
            str += toString((Node) node.getValue(indexKey), 0);
        str += toString(node, indexKey + 1);
        return str;
    }

    private void insert(K key, Object value, Node node) {
        int i;
        if(node.isLeaf()){
            node.insert(key, value);
        }
    }
}
