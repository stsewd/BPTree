package edu.ucue.bptree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private final File PATH;
    private final int OBJ_SIZE;
    private final int EXTRA_BYTES = 4; // Bytes extras que contienen el tamaño del objeto
    private BPTree<K, Long> tree;
    
    private BPTreeMap(int keysNumber, Comparator comparator, String path, int objSize) {
        tree = new BPTree(keysNumber, comparator);
        PATH = new File(path);
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
     */
    public static BPTreeMap getBPTree(int keysNumber, Comparator comparator, String dataPath, String treePath, int objSize){
        BPTreeMap tree = null;
        
        File path = new File(treePath);
        if(!path.exists())
            return new BPTreeMap(keysNumber, comparator, dataPath, objSize);
        
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            tree = (BPTreeMap) ois.readObject();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no existente.");
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al recuperar objeto.");
        }
        
        return tree;
    }
    
    /**
     * Retorna el número de elementos que contiene
     * el árbol.
     * @return 
     */
    public int size() {
        return tree.values().size();
    }
    
    /**
     * Retorna true si el árbol está vacío.
     * @return 
     */
    public boolean isEmpty() {
        return size() == 0;
    }
    
    /**
     * Retorna true si la clave key se encuentre
     * dentro del árbol.
     * @param key
     * @return 
     */
    public boolean containsKey(K key) {
        return tree.search(key) != null;
    }
    
    /**
     * Agrega un elemento V, y lo ordena de acuerdo a su
     * clave key.
     * @param key
     * @param value 
     */
    public void put(K key, V value) {
        RandomAccessFile ram = null;
        byte[] obj;
        byte[] rest;
        long pos = 0;
        
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            obj = serialize(value);
            
            if(obj.length > OBJ_SIZE)
                return; // Reemplazar por lanzar excepcion
            
            pos = ram.length();
            ram.seek(pos);
            ram.writeInt(obj.length);
            ram.write(obj);
            
            // Llenar de bytes
            rest = new byte[OBJ_SIZE - obj.length + EXTRA_BYTES];
            ram.write(rest);
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo de datos no encontrado.");
        } catch (IOException ex) {
            System.out.println("Error al escribir en archivo de datos.");
        } finally {
            try {
                ram.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar archivo de datos.");
            }
        }
        
        tree.add(key, pos);
    }
    
    /**
     * Agrega clave y posicion en tabla de indices,
     * la tabla de valores no se ve modificada.
     * @param key 
     * @param pos 
     */
    public void put(K key, Long pos){
        tree.add(key, pos);
    }

    /**
     * Retorna una colección con todos los elementos
     * almacenados en el árbol.
     * @return 
     */
    public Collection<V> values() {
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
     */
    public V get(K key) {
        Long pos = tree.search(key);
        if(pos == null)
            return null;
        return getObject(pos);
    }
    
    /**
     * Retorna la posicion donde se encuentra el objeto.
     * @param key
     * @return 
     */
    public Long getPos(K key){
        return tree.search(key);
    }

    /**
     * Elimina un elemento del árbol dada su clave
     * (sólo se elimina de la tabla de índices, no
     * de la tabla de valores).
     * @param key 
     */
    public void remove(K key) {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Obtiene un objeto dada su posicion dentro del archivo de
     * acceso aleatorio.
     * @param pos
     * @return 
     */
    private V getObject(long pos) {
        RandomAccessFile ram = null;
        byte[] objByte;
        V obj = null;
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            ram.seek(pos);
            objByte = new byte[ram.readInt()];
            ram.read(objByte);
            
            obj = (V) deserialize(objByte);
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo de datos no encontrado.");
        } catch (IOException ex) {
            System.out.println("Error al leer archivo de datos.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al leer el objeto.");
        } finally {
            try {
                ram.close();
            } catch (IOException ex) {
                System.out.println("Erro al cerrar el archivo de datos.");
            }
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
     */
    public void save(String treePath){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(treePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(this);
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no existente.");
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo.");
        }
    }
    
    public void update(K key, V newValue){
        RandomAccessFile ram = null;
        byte[] obj;
        byte[] rest;
        long pos = 0;
        
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            obj = serialize(newValue);
            
            if(obj.length > OBJ_SIZE)
                return; // Reemplazar por lanzar excepcion
            
            pos = getPos(key);
            ram.seek(pos);
            ram.writeInt(obj.length);
            ram.write(obj);
            
            // Llenar de bytes
            rest = new byte[OBJ_SIZE - obj.length + EXTRA_BYTES];
            ram.write(rest);
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo de datos no encontrado.");
        } catch (IOException ex) {
            System.out.println("Error al escribir en archivo de datos.");
        } finally {
            try {
                ram.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar archivo de datos.");
            }
        }
    }
    
    public void showAll(){
        tree.showAll();
    }
}
