/**
 * B+ Tree / Árbol B+
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
    private final Comparator<K> comparator; // Comparador de claves
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
    
    /**
     * Busca un valor por su clave, si no hay una coincidencia
     * retorna null.
     * @param key
     * @return 
     */
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
            while(i <  node.getNodeSize() && comparator.compare(key, (K) node.getKey(i)) >= 0)
                i++;
            return search(key, node.getChild(i));
        }
        return null;
    }
    
    /**
     * Agrega un valor, con su respectiva clave
     * al árbol, manteniendo todos sus elementos
     * ordenados por clave.
     * @param key
     * @param value 
     */
    public void add(K key, V value){
        add(key, value, root);
    }
    
    private void add(K key, V value, Node node){
        // Buscar en qué hoja pertenece la clave a insertar
        Node leaf = node;
        int i;
        while(!leaf.isLeaf()){
            i = leaf.getNodeSize() - 1;
            while(i >= 0 && comparator.compare(key, (K) leaf.getKey(i)) < 0)
                i--;
            leaf = leaf.getChild(i + 1);
        }
        
        if(leaf.getNodeSize() == keysNumber){
            Node newLeaf = new Node(true, keysNumber, comparator);
            // Si el padre del nodo es nulo (solo hay un nodo hoja/root)
            if(leaf.getParent() == null){
                leaf.insert(key, value);
                
                root = new Node(false, keysNumber, comparator);
                
                // Dividir claves/valores del nodo hoja
                int k = 0;
                for(int j = (keysNumber)/2; j < keysNumber + 1; j++){
                    newLeaf.setKey(k, leaf.getKey(j));
                    newLeaf.setValue(k, leaf.getValue(j));
                    k++;
                }
                
                leaf.setNodeSize((keysNumber)/2);
                newLeaf.setNodeSize(keysNumber + 1 - (keysNumber)/2);
                
                root.setKey(0, (K) newLeaf.getKey(0));
                root.setNodeSize(1);
                
                leaf.setParent(root);
                newLeaf.setParent(root);
                root.setChild(0, leaf);
                root.setChild(1, newLeaf);
                
            }else {
                leaf.insert(key, value);
                // Dividir claves/valores del nodo hoja
                int k = 0;
                for(int j = (keysNumber)/2; j < keysNumber + 1; j++){
                    newLeaf.setKey(k, leaf.getKey(j));
                    newLeaf.setValue(k, leaf.getValue(j));
                    k++;
                }
                
                leaf.setNodeSize((keysNumber)/2);
                newLeaf.setNodeSize(keysNumber + 1 - (keysNumber)/2);
                
                Node parent = leaf.getParent();
                newLeaf.setParent(parent);
                
                parent.insert(newLeaf.getKey(0), newLeaf);
                
                if(parent.getNodeSize() > keysNumber){
                    splitInnerNode(parent);
                }
            }
        }else {
            leaf.insert(key, value);
        }
    }
    
    private void splitInnerNode(Node node) {
        Node newNode = new Node(false, keysNumber, comparator);
        
        if(node.getParent() == null){
            root = new Node(false, keysNumber, comparator);

            // Dividir claves/valores del nodo interno
            int k = 0;
            int j;
            for(j = (keysNumber)/2 + 1; j < keysNumber + 1; j++){
                newNode.setKey(k, node.getKey(j));
                newNode.setChild(k, node.getChild(j));
                node.getChild(j).setParent(newNode);
                k++;
            }
            newNode.setChild(k, node.getChild(j));
            node.getChild(j).setParent(newNode);
            
            node.setNodeSize((keysNumber)/2);
            newNode.setNodeSize(keysNumber - (keysNumber)/2);
            
            root.setKey(0, (K) node.getKey((keysNumber)/2));
            root.setNodeSize(1);
            
            node.setParent(root);
            newNode.setParent(root);
            root.setChild(0, node);
            root.setChild(1, newNode);
            return;
        }
        
        int k = 0;
        int j;
        for(j = (keysNumber)/2 + 1; j < keysNumber + 1; j++){
            newNode.setKey(k, node.getKey(j));
            newNode.setChild(k, node.getChild(j));
            node.getChild(j).setParent(newNode);
            k++;
        }
        newNode.setChild(k, node.getChild(j));
        node.getChild(j).setParent(newNode);
        
        node.setNodeSize((keysNumber)/2);
        newNode.setNodeSize(keysNumber - (keysNumber)/2);
        
        Node parent = node.getParent();
        newNode.setParent(parent);
        
        parent.insert(node.getKey((keysNumber)/2), newNode);
        if(parent.getNodeSize() == keysNumber)
            splitInnerNode(parent);
    }
    
    /**
     * 
     * @return 
     */
    public Node<K, V> getRoot() {
        return root;
    }

    /**
     * @return the comparator
     */
    public Comparator<K> getComparator() {
        return comparator;
    }
    

    @Override
    public String toString() {
        return toString(root);
    }
    
    /**
     * Retorna el árbol recorrido en preorden.
     * @param node
     * @param index
     * @return 
     */
    private String toString(Node node){
        String str = "";
        str += node.toString();
        
        if(node.getParent() != null)
            str += " padre: (" + node.getParent().toString() + ")";
        
        str += "\n";
        
        if(node.isLeaf())
            return str;
        
        for(int i = 0; i < node.getNodeSize() + 1; i++)
            str += toString(node.getChild(i));
        
        return str;
    }
}
