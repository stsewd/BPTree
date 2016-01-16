package edu.ucue.bptree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author Santos Gallegos
 */
public class BPTreeMap<K, V> implements Serializable {
    private final File PATH; // Ruta donde se almacenará la tabla de valores.
    private final int OBJ_SIZE; // Tamaño max reservado para cada objeto.
    private final int EXTRA_BYTES = 4; // Bytes extras que contienen el tamaño del objeto.
    private BPTree<K> tree; // Árbol B+, tabla de índices.
    
    private BPTreeMap(int keysNumber, Comparator comparator, String dataPath, String treePath, int objSize) throws IOException, FileNotFoundException, ObjectSizeException {
        tree = BPTree.getTree(keysNumber, comparator, treePath, 1500); // 1500 es el tamño del nodo.
        PATH = new File(dataPath);
        OBJ_SIZE = objSize;
    }
    
    /**
     * Recupera un árbol existente de la ruta dada, si no existe
     * se retorna un nuevo árbol con los parámetros establecidos.
     * @param keysNumber
     * @param comparator
     * @param dataPath
     * @param treePath
     * @param objSize
     * @return 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     * @throws edu.ucue.bptree.ObjectSizeException 
     */
    public static BPTreeMap getTree(int keysNumber, Comparator comparator, String dataPath, String treePath, int objSize)
            throws FileNotFoundException, IOException, ClassNotFoundException, ObjectSizeException
    {
        
        return new BPTreeMap(keysNumber, comparator, dataPath, treePath, objSize);
    }
    
    /**
     * Retorna el número de elementos que contiene
     * el árbol.
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public int size() throws IOException, FileNotFoundException, ClassNotFoundException {
        return tree.values().size();
    }
    
    /**
     * Retorna true si el árbol está vacío.
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public boolean isEmpty() throws IOException, FileNotFoundException, ClassNotFoundException {
        return size() == 0;
    }
    
    /**
     * Retorna true si la clave key se encuentre
     * dentro del árbol.
     * @param key
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public boolean containsKey(K key) throws IOException, FileNotFoundException, ClassNotFoundException {
        return tree.search(key) != null;
    }
    
    /**
     * Agrega un elemento V, y lo ordena de acuerdo a su
     * clave key.
     * @param key
     * @param value 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     * @throws edu.ucue.bptree.ObjectSizeException 
     */
    public void put(K key, V value) throws FileNotFoundException, IOException, ClassNotFoundException, ObjectSizeException {
        RandomAccessFile ram = null;
        byte[] obj;
        byte[] rest;
        long pos = 0;
        
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            obj = serialize(value);
            
            if(obj.length > OBJ_SIZE)
                throw new ObjectSizeException();
            
            pos = ram.length();
            ram.seek(pos);
            ram.writeInt(obj.length);
            ram.write(obj);
            
            // Llenar de bytes
            rest = new byte[OBJ_SIZE - obj.length + EXTRA_BYTES];
            ram.write(rest);
        } finally {
            ram.close();
        }
        
        tree.add(key, pos);
    }
    
    /**
     * Agrega clave y posicion en tabla de indices,
     * la tabla de valores no se ve modificada,
     * usar con cuidado.
     * @param key 
     * @param pos 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     * @throws edu.ucue.bptree.ObjectSizeException 
     */
    public void put(K key, Long pos) throws IOException, FileNotFoundException, ClassNotFoundException, ObjectSizeException{
        tree.add(key, pos);
    }

    /**
     * Retorna una colección con todos los elementos
     * almacenados en el árbol.
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public Collection<V> values() throws IOException, FileNotFoundException, ClassNotFoundException {
        ArrayList values = new ArrayList();
        for(Long pos : tree.values()){
            values.add(getObject(pos));
        }
        return values;
    }

    /**
     * Retorna un objeto dado su clave,
     * retorna null si este no existe.
     * @param key
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public V get(K key) throws IOException, FileNotFoundException, ClassNotFoundException {
        Long pos = tree.search(key);
        if(pos == null)
            return null;
        return getObject(pos);
    }
    
    /**
     * Retorna la posicion donde se encuentra el objeto.
     * @param key
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     */
    public Long getPos(K key) throws IOException, FileNotFoundException, ClassNotFoundException{
        return tree.search(key);
    }

    /**
     * Elimina un elemento del árbol dada su clave
     * (sólo se elimina de la tabla de índices, no
     * de la tabla de valores).
     * @param key 
     */
    public void remove(K key) {
         throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Obtiene un objeto dada su posicion dentro del archivo de
     * acceso aleatorio.
     * @param pos
     * @return 
     */
    private V getObject(long pos) throws FileNotFoundException, IOException, ClassNotFoundException {
        RandomAccessFile ram = null;
        byte[] objByte;
        V obj = null;
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            ram.seek(pos);
            objByte = new byte[ram.readInt()];
            ram.read(objByte);
            
            obj = (V) deserialize(objByte);
        } finally {
            ram.close();
        }
        
        return obj;
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
    
    /**
     * Guarda el árbol en la ruta dada.
     * @param treePath 
     * @throws java.io.FileNotFoundException 
     */
    public void save(String treePath) throws FileNotFoundException, IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(treePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(this);
        } finally {
            fos.close();
        }
    }
    
    public void update(K key, V newValue) throws FileNotFoundException, IOException, ClassNotFoundException, ObjectSizeException {
        RandomAccessFile ram = null;
        byte[] obj;
        byte[] rest;
        long pos = 0;
        
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            obj = serialize(newValue);
            
            if(obj.length > OBJ_SIZE)
                throw new ObjectSizeException();
            
            pos = getPos(key);
            ram.seek(pos);
            ram.writeInt(obj.length);
            ram.write(obj);
            
            // Llenar de bytes
            rest = new byte[OBJ_SIZE - obj.length + EXTRA_BYTES];
            ram.write(rest);
        } finally {
            ram.close();
        }
    }
    
    public void showAll() throws IOException, FileNotFoundException, ClassNotFoundException{
        tree.showAll();
    }
}
