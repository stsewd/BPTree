/**
 * B+ Tree / Árbol B+
 */
package edu.ucue.bptree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Árbol B+.
 * @author Santos Gallegos
 * @param <K> Clave
 */
public class BPTree<K> implements Serializable {
    private Long root; // Nodo raíz
    private final int keysNum; // Máximo de claves u órden de árbol
    private final int minKeysNum; // Mínimo de claves que puede tener un nodo (excepto root)
    private final Comparator<K> comparator; // Comparador de claves
    private final File PATH; // Ruta donde se almacenara la tabla de indices
    private final int OBJ_SIZE; // Tamaño max reservado para cada nodo.
    private final int EXTRA_BYTES = 4; // Bytes extras que contienen el tamaño del nodo.
    
    /**
     * Crea un nuevo árbol vacío.
     * @param keysNumber Número máximo de claves.
     * @param comparator Objeto necesario para comparar las claves.
     * @param path
     * @param objSize
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    private BPTree(int keysNumber, Comparator<K> comparator, String path, int objSize, Long root) throws FileNotFoundException, IOException {
        
        this.keysNum = keysNumber;
        this.comparator = comparator;
        this.minKeysNum = (int) Math.ceil(keysNum/2.0);
        this.OBJ_SIZE = objSize;
        this.PATH = new File(path);
        
        setRoot(root);
    }
    
    public static BPTree getBPTree(int keysNumber, Comparator comparator, String path, int objSize) throws FileNotFoundException, IOException {
        BPTree tree = null;
        
        File treePath = new File(path);
        if(!treePath.exists()){
            tree = new BPTree(keysNumber, comparator, path, objSize, 8L);
            tree.saveNode(new Node(true, keysNumber, comparator));
            return tree;
        }
        
        RandomAccessFile raf = null;
        
        try{
            raf = new RandomAccessFile(treePath, "rw");
            raf.seek(0);
            Long root = raf.readLong();
            tree = new BPTree(keysNumber, comparator, path, objSize, root);
        } finally {
            raf.close();
        }

        return tree;
    }
        

    private void setRoot(Long root) throws FileNotFoundException, IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(PATH, "rw");
            raf.seek(0);
            raf.writeLong(root);
        } finally {
            raf.close();
        }
        this.root = root;
    }
    
    /**
     * Busca un valor por su clave, si no hay una coincidencia
     * retorna null.
     * @param key
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public Long search(K key) throws IOException, FileNotFoundException, ClassNotFoundException{
        return search(key, root);
    }
    
    private Long search(K key, Long nodePos) throws IOException, FileNotFoundException, ClassNotFoundException{
        
        Node node = getNode(nodePos);
        
        if(node.isLeaf()){
            for(int i = 0; i < node.getNodeSize(); i++){
                if(comparator.compare(key, (K) node.getKey(i)) == 0)
                    return node.getValue(i);
            }
        }else {
            int i = 0;
            while(i < node.getNodeSize() && comparator.compare(key, (K) node.getKey(i)) >= 0)
                i++;
            return search(key, node.getChild(i));
        }
        return null;
    }
    
    /**
     * Retorna la hoja en donde debería ser insertada
     * una clave, o donde podría estar esta.
     * @param key
     * @return 
     */
    private Long searchLeaf(K key) throws IOException, FileNotFoundException, ClassNotFoundException{
        
        Node leaf = getNode(root);
        int i;
        while(!leaf.isLeaf()){
            i = leaf.getNodeSize() - 1;
            while(i >= 0 && comparator.compare(key, (K) leaf.getKey(i)) < 0)
                i--;
            leaf = getNode(leaf.getChild(i + 1));
        }
        return leaf.getPos();
    }
    
    /**
     * Agrega un valor, con su respectiva clave
     * al árbol, manteniendo todos sus elementos
     * ordenados por clave.
     * @param key
     * @param value 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */  
    public void add(K key, Long value) throws IOException, FileNotFoundException, ClassNotFoundException{
        // Buscar en qué hoja pertenece la clave a insertar
        Node leaf = getNode(searchLeaf(key));
        
        leaf.insertValue(key, value);
        
        updateNode(leaf);
        
        if(leaf.getNodeSize() <= keysNum)
            return;
       
        Node newLeaf = new Node(true, keysNum, comparator);
        saveNode(newLeaf);
        
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
        
        updateNode(leaf);
        updateNode(newLeaf);
        
        // Si el padre del nodo es nulo, la hoja es root
        if(leaf.getParent() == null){
            
            Node newRoot = new Node(false, keysNum, comparator);
            
            newRoot.setKey(0, (K) newLeaf.getKey(0));
            newRoot.setNodeSize(1);
            
            saveNode(newRoot);
            
            setRoot(newRoot.getPos());
            
            newLeaf.setNext(null);
            newLeaf.setPrev(leaf.getPos());
            leaf.setNext(newLeaf.getPos());
            leaf.setPrev(null);

            leaf.setParent(newRoot.getPos());
            newLeaf.setParent(newRoot.getPos());
            
            updateNode(leaf);
            updateNode(newLeaf);
            
            newRoot.setChild(0, leaf.getPos());
            newRoot.setChild(1, newLeaf.getPos());
            
            updateNode(newRoot);
            
        }else {
            
            newLeaf.setNext(leaf.next());
            if(leaf.next() != null){
                Node leafNext = getNode(leaf.next()); 
                leafNext.setPrev(newLeaf.getPos());
                updateNode(leafNext);
            }
            newLeaf.setPrev(leaf.getPos());
            leaf.setNext(newLeaf.getPos());
            
            updateNode(leaf);
            updateNode(newLeaf);
            
            
            Node parent = getNode(leaf.getParent());
            newLeaf.setParent(parent.getPos());
            
            updateNode(newLeaf);

            parent.insertChild(newLeaf.getKey(0), newLeaf.getPos());

            updateNode(parent);
            
            if(parent.getNodeSize() > keysNum)
                split(parent.getPos());
        }
    }
    
    /**
     * Divide un nodo de manera recursiva hasta que todos
     * los nodos tengan el numero de claves adecuado.
     * @param node 
     */
    private void split(Long nodePos) throws IOException, FileNotFoundException, ClassNotFoundException {
        Node node = getNode(nodePos);
        
        Node newNode = new Node(false, keysNum, comparator);
        saveNode(newNode);
        
        // Dividir claves/valores del nodo interno
        int k = 0;
        int j = 0;
        for(j = minKeysNum + 1; j < keysNum + 1; j++){
            
            Node child = getNode(node.getChild(j));
            child.setParent(newNode.getPos());
            updateNode(child);
            
            newNode.setKey(k, node.getKey(j));
            newNode.setChild(k, child.getPos());
            updateNode(newNode);

            k++;
        }
        Node child = getNode(node.getChild(j));
        child.setParent(newNode.getPos());
        updateNode(child);
        
        newNode.setChild(k, child.getPos());
        updateNode(newNode);
        
        node.setNodeSize(minKeysNum);
        newNode.setNodeSize(keysNum - minKeysNum);
        
        updateNode(node);
        updateNode(newNode);
        
        // Si el nodo es raiz
        if(node.getParent() == null){
            Node newRoot = new Node(false, keysNum, comparator);
            
            newRoot.setKey(0, (K) node.getKey(minKeysNum));
            newRoot.setNodeSize(1);
            
            saveNode(newRoot);
            
            setRoot(newRoot.getPos());
            
            node.setParent(newRoot.getPos());
            newNode.setParent(newRoot.getPos());
            
            updateNode(node);
            updateNode(newNode);
            
            newRoot.setChild(0, node.getPos());
            newRoot.setChild(1, newNode.getPos());
            
            updateNode(newRoot);

        }else {
            
            Node parent = getNode(node.getParent());
            newNode.setParent(parent.getPos());
            
            updateNode(newNode);
            
            parent.insertChild(node.getKey(minKeysNum), newNode.getPos());
            
            updateNode(parent);
            
            if(parent.getNodeSize() > keysNum)
                split(parent.getPos());
        }
    }
    
    public void del(K key){
        // del(key, root);
    }
    
    /*
    private void del(K key, Long node){
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
            leaf.insertValue(leftLeaf.getKey(last), leftLeaf.getValue(last));
            leftLeaf.setNodeSize(leftLeaf.getNodeSize() - 1);
            
            parent.setKey(i, leaf.getKey(0));
            
            return;
        }
        
        // Pedir prestado de hermano derecho
        Node rightLeaf = leaf.next();
        if(rightLeaf.getParent() == leaf.getParent() && rightLeaf.getNodeSize() > keysNum/2){
            int first = 0;
            leaf.insertValue(rightLeaf.getKey(first), rightLeaf.getValue(first));
            rightLeaf.remove(rightLeaf.getKey(first));
            
            // Probablemente no necesario
            // (probablemente a excepcion de un arbol de orden 2).
            if(leaf.getNodeSize() == 1){
                parent.remove(key);
                parent.remove(leaf.getKey(first));
                parent.insertChild(leaf.getKey(first), leaf);
                parent.insertChild(rightLeaf.getKey(first), rightLeaf);
            }else {
                parent.setKey(i, rightLeaf.getKey(first));
            }
            
            return;
        }
        
        // Merge con vecino izq
        for(int j = 0; j < leaf.getNodeSize(); j++)
            leftLeaf.insertValue(leaf.getKey(j), leaf.getValue(j));
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
            leaf.insertValue(leftLeaf.getKey(last), leftLeaf.getValue(last));
            leftLeaf.setNodeSize(leftLeaf.getNodeSize() - 1);
            
            parent.setKey(i, leaf.getKey(0));
            
            return;
        }
        
        // Pedir prestado de hermano derecho
        Node rightLeaf = leaf.next();
        if(rightLeaf.getParent() == leaf.getParent() && rightLeaf.getNodeSize() > keysNum/2){
            int first = 0;
            leaf.insertValue(rightLeaf.getKey(first), rightLeaf.getValue(first));
            rightLeaf.remove(rightLeaf.getKey(first));
            
            // Probablemente no necesario
            // (probablemente a excepcion de un arbol de orden 2).
            if(leaf.getNodeSize() == 1){
                parent.remove(key);
                parent.remove(leaf.getKey(first));
                parent.insertValue(leaf.getKey(first), leaf);
                parent.insertValue(rightLeaf.getKey(first), rightLeaf);
            }else {
                parent.setKey(i, rightLeaf.getKey(first));
            }
            
            return;
        }
        
        // Merge con vecino izq
        for(int j = 0; j < leaf.getNodeSize(); j++)
            leftLeaf.insertValue(leaf.getKey(j), leaf.getValue(j));
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
    public Long getRoot() {
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
        try {
            return toString(root);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
    /**
     * Retorna el árbol recorrido en preorden.
     * @param node
     * @param index
     * @return 
     */
    private String toString(Long nodePos) throws IOException, FileNotFoundException, ClassNotFoundException{
        Node node = getNode(nodePos);
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
    
    public void showAll() throws IOException, FileNotFoundException, ClassNotFoundException{
        // Buscar primera hoja
        Node leaf = getNode(root);
        while(!leaf.isLeaf())
            leaf = getNode(leaf.getChild(0));
        
        while(leaf != null){
            System.out.print(leaf);
            leaf = getNode(leaf.next());
        }
    }
    
    public Collection<Long> values() throws IOException, FileNotFoundException, ClassNotFoundException{
        ArrayList<Long> values = new ArrayList();
        
        // Buscar primera hoja.
        Node leaf = getNode(root);
        while(!leaf.isLeaf())
            leaf = getNode(leaf.getChild(0));
        
        // Recorrer todos los elementos del arbol de forma ordenada.
        while(leaf != null){
            values.addAll(leaf.values());
            leaf = getNode(leaf.next());
        }
        return values;
    }
    
    /**
     * Retorna una cadena de bytes que representan la serializacion del objeto.
     * @param obj
     * @return
     * @throws IOException 
     */
    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    
    /**
     * Dada una cadena de bytes, retorna el objeto que representa.
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
    private Long saveNode(Node node) throws IOException{
        byte[] obj;
        byte[] rest;
        long pos = 0;
        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(PATH, "rw");
            pos = raf.length();
            
            node.setPos(pos);
            
            obj = serialize(node);
            
            if(obj.length > OBJ_SIZE){
                System.out.println("Limite.");
                return null; // Reemplazar por lanzar excepcion
            }
            
            raf.seek(pos);
            raf.writeInt(obj.length);
            raf.write(obj);
            
            // Llenar de bytes
            rest = new byte[OBJ_SIZE - obj.length + EXTRA_BYTES];
            raf.write(rest);
        } finally {
            raf.close();
        }
        return pos;
    }
    
    private void updateNode(Node newNode, Long pos) throws FileNotFoundException, IOException{
        byte[] obj;
        byte[] rest;
        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(PATH, "rw");
            
            obj = serialize(newNode);
            
            if(obj.length > OBJ_SIZE){
                System.out.println("Muy grande!");
                return; // Reemplazar por lanzar excepcion
            }
            raf.seek(pos);
            raf.writeInt(obj.length);
            raf.write(obj);
            
            // Llenar de bytes
            rest = new byte[OBJ_SIZE - obj.length + EXTRA_BYTES];
            raf.write(rest);
            
        } finally {
            raf.close();
        }
    }
    
    private void updateNode(Node node) throws IOException{
        updateNode(node, node.getPos());
    }
    
    private Node getNode(Long pos) throws FileNotFoundException, IOException, ClassNotFoundException {
        if(pos == null)
            return null;
        RandomAccessFile raf = null;
        byte[] objByte;
        Node obj = null;
        try {
            raf = new RandomAccessFile(PATH, "rw");
            
            raf.seek(pos);
            objByte = new byte[raf.readInt()];
            raf.read(objByte);
            
            obj = (Node) deserialize(objByte);
        } finally {
            raf.close();
        }
        return obj;
    }
}
