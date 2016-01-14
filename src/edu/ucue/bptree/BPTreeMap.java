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
    private BPTree<K, Long> tree;
    
    private BPTreeMap(int keysNumber, Comparator comparator, String path, int objSize) {
        tree = new BPTree(keysNumber, comparator);
        PATH = new File(path);
        OBJ_SIZE = objSize;
    }
    
    public int size() {
        return values().size();
    }

    public boolean isEmpty() {
        return tree.getRoot().getNodeSize() == 0;
    }

    public boolean containsKey(K key) {
        return tree.search(key) != null;
    }

    public void put(K key, V value) {
        RandomAccessFile ram = null;
        byte[] obj;
        long pos = 0;
        
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            obj = serialize(value);
            pos = ram.length();
            ram.seek(pos);
            
            ram.write(obj);
            
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

    public Collection<V> values() {
        ArrayList values = new ArrayList();
        for(Long pos : tree.values()){
            values.add(getObject(pos));
        }
        return values;
    }

    public V get(K key) {
        long pos = tree.search(key);
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

    public void remove(K key) {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private V getObject(long pos) {
        RandomAccessFile ram = null;
        byte[] objByte = new byte[OBJ_SIZE];
        V obj = null;
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            ram.seek(pos);
            
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

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    
    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
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
    
    public void saveBPTree(String treePath){
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
}
