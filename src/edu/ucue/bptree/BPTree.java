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
    private final int keysNum; // Máximo de claves u órden de árbol
    private final int minKeysNum; // Mínimo de claves que puede tener un nodo (excepto root)
    private final Comparator<K> comparator; // Comparador de claves
    /**
     * Crea un nuevo árbol vacío.
     * @param KeysNumber Número máximo de claves.
     * @param comparator Objeto necesario para comparar las claves.
     */
    public BPTree(int KeysNumber, Comparator<K> comparator) {
        this.keysNum = KeysNumber;
        this.comparator = comparator;
        this.root = new Node<>(true, keysNum, comparator);
        this.minKeysNum = (int) Math.ceil(keysNum/2.0);
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
    
    private Node searchLeaf(K key){
        Node leaf = root;
        int i;
        while(!leaf.isLeaf()){
            i = leaf.getNodeSize() - 1;
            while(i >= 0 && comparator.compare(key, (K) leaf.getKey(i)) < 0)
                i--;
            leaf = leaf.getChild(i + 1);
        }
        return leaf;
    }
    
    /**
     * Agrega un valor, con su respectiva clave
     * al árbol, manteniendo todos sus elementos
     * ordenados por clave.
     * @param key
     * @param value 
     */  
    public void add(K key, V value){
        // Buscar en qué hoja pertenece la clave a insertar
        Node leaf = searchLeaf(key);
        
        leaf.insert(key, value);
        
        if(leaf.getNodeSize() <= keysNum)
            return;
       
        Node newLeaf = new Node(true, keysNum, comparator);
        
        // Dividir claves/valores del nodo hoja
        int k = 0;
        int j = 0;
        for(j = minKeysNum; j < keysNum + 1; j++){
            newLeaf.setKey(k, leaf.getKey(j));
            newLeaf.setValue(k, leaf.getValue(j));
            k++;
        }

        leaf.setNodeSize(minKeysNum);
        newLeaf.setNodeSize(keysNum + 1 - minKeysNum);
        
        // Si el padre del nodo es nulo, la hoja es root
        if(leaf.getParent() == null){
            root = new Node(false, keysNum, comparator);

            root.setKey(0, (K) newLeaf.getKey(0));
            root.setNodeSize(1);

            newLeaf.setNext(null);
            newLeaf.setPrev(leaf);
            leaf.setNext(newLeaf);
            leaf.setPrev(null);

            leaf.setParent(root);
            newLeaf.setParent(root);
            root.setChild(0, leaf);
            root.setChild(1, newLeaf);

        }else {
            newLeaf.setNext(leaf.next());
            if(leaf.next() != null)
                leaf.next().setPrev(newLeaf);
            newLeaf.setPrev(leaf);
            leaf.setNext(newLeaf);

            Node parent = leaf.getParent();
            newLeaf.setParent(parent);

            parent.insert(newLeaf.getKey(0), newLeaf);

            if(parent.getNodeSize() > keysNum)
                split(parent);
        }
    }
    
    private void split(Node node) {
        Node newNode = new Node(false, keysNum, comparator);

        // Dividir claves/valores del nodo interno
        int k = 0;
        int j = 0;
        for(j = minKeysNum + 1; j < keysNum + 1; j++){
            newNode.setKey(k, node.getKey(j));
            newNode.setChild(k, node.getChild(j));
            node.getChild(j).setParent(newNode);
            k++;
        }
        newNode.setChild(k, node.getChild(j));
        node.getChild(j).setParent(newNode);

        node.setNodeSize(minKeysNum);
        newNode.setNodeSize(keysNum - minKeysNum);
        
        if(node.getParent() == null){
            root = new Node(false, keysNum, comparator);            
            root.setKey(0, (K) node.getKey(minKeysNum));
            root.setNodeSize(1);
            
            node.setParent(root);
            newNode.setParent(root);
            root.setChild(0, node);
            root.setChild(1, newNode);
        }else {
            Node parent = node.getParent();
            newNode.setParent(parent);

            parent.insert(node.getKey(minKeysNum), newNode);
            if(parent.getNodeSize() > keysNum)
                split(parent);
        }
    }
    
    public void del(K key){
        del(key, root);
    }
    
    private void del(K key, Node node){
        // Comprobar si la clave existe
        if(search(key) == null)
            return;
        // Buscar en qué hoja pertenece la clave a eliminar
        Node leaf = node;
        int i = 0;
        while(!leaf.isLeaf()){
            i = leaf.getNodeSize() - 1;
            while(i >= 0 && comparator.compare(key, (K) leaf.getKey(i)) < 0)
                i--;
            leaf = leaf.getChild(i + 1);
        }
        
        Node parent = leaf.getParent();
        
        leaf.remove(key);
        parent.setKey(i, leaf.getKey(0));
        
        if(leaf.getNodeSize() >= keysNum/2)
            return;
        
        // Pedir prestado de hermano izquierdo
        Node leftLeaf = leaf.prev();
        if(leftLeaf.getParent() == leaf.getParent() && leftLeaf.getNodeSize() > keysNum/2){
            int last = leftLeaf.getNodeSize() - 1;
            leaf.insert(leftLeaf.getKey(last), leftLeaf.getValue(last));
            leftLeaf.setNodeSize(leftLeaf.getNodeSize() - 1);
            
            parent.setKey(i, leaf.getKey(0));
            
            return;
        }
        
        // Pedir prestado de hermano derecho
        Node rightLeaf = leaf.next();
        if(rightLeaf.getParent() == leaf.getParent() && rightLeaf.getNodeSize() > keysNum/2){
            int first = 0;
            leaf.insert(rightLeaf.getKey(first), rightLeaf.getValue(first));
            rightLeaf.remove(rightLeaf.getKey(first));
            
            // Probablemente no necesario
            // (probablemente a excepcion de un arbol de orden 2).
            if(leaf.getNodeSize() == 1){
                parent.remove(key);
                parent.remove(leaf.getKey(first));
                parent.insert(leaf.getKey(first), leaf);
                parent.insert(rightLeaf.getKey(first), rightLeaf);
            }else {
                parent.setKey(i, rightLeaf.getKey(first));
            }
            
            return;
        }
        
        // Merge con vecino izq
        for(int j = 0; j < leaf.getNodeSize(); j++)
            leftLeaf.insert(leaf.getKey(j), leaf.getValue(j));
        parent.remove(leaf.getKey(0));
        leftLeaf.setNodeSize(leftLeaf.getNodeSize() + leaf.getNodeSize());
        
        leftLeaf.setNext(leaf.next());
        leaf.next().setPrev(leftLeaf);
        
        if(parent.getNodeSize() < minKeysNum){}
            //merge()
    }
    
    /*
    private void merge(Node node, int i){
        // Pedir prestado de hermano izquierdo
        Node leftLeaf = leaf.prev();
        if(leftLeaf.getParent() == leaf.getParent() && leftLeaf.getNodeSize() > keysNum/2){
            int last = leftLeaf.getNodeSize() - 1;
            leaf.insert(leftLeaf.getKey(last), leftLeaf.getValue(last));
            leftLeaf.setNodeSize(leftLeaf.getNodeSize() - 1);
            
            parent.setKey(i, leaf.getKey(0));
            
            return;
        }
        
        // Pedir prestado de hermano derecho
        Node rightLeaf = leaf.next();
        if(rightLeaf.getParent() == leaf.getParent() && rightLeaf.getNodeSize() > keysNum/2){
            int first = 0;
            leaf.insert(rightLeaf.getKey(first), rightLeaf.getValue(first));
            rightLeaf.remove(rightLeaf.getKey(first));
            
            // Probablemente no necesario
            // (probablemente a excepcion de un arbol de orden 2).
            if(leaf.getNodeSize() == 1){
                parent.remove(key);
                parent.remove(leaf.getKey(first));
                parent.insert(leaf.getKey(first), leaf);
                parent.insert(rightLeaf.getKey(first), rightLeaf);
            }else {
                parent.setKey(i, rightLeaf.getKey(first));
            }
            
            return;
        }
        
        // Merge con vecino izq
        for(int j = 0; j < leaf.getNodeSize(); j++)
            leftLeaf.insert(leaf.getKey(j), leaf.getValue(j));
        parent.remove(leaf.getKey(0));
        leftLeaf.setNodeSize(leftLeaf.getNodeSize() + leaf.getNodeSize());
        
        leftLeaf.setNext(leaf.next());
        leaf.next().setPrev(leftLeaf);
        
        if(parent.getNodeSize() < minKeysNum)
            merge()
    }
    */
    
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
    
    public void showAll(){
        // Buscar primera hoja
        Node leaf = root;
        while(!leaf.isLeaf())
            leaf = leaf.getChild(0);
        
        while(leaf != null){
            System.out.print(leaf);
            leaf = leaf.next();
        }
    }
}
