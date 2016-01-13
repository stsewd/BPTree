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
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Santos Gallegos
 */
public class BPTreeMap<K, V> implements Map<K, V>, Serializable {
    private final File PATH;
    private final int OBJ_SIZE;
    private BPTree<K, Long> tree;
    
    public BPTreeMap(int keysNumber, Comparator comparator, String path, int objSize) {
        tree = new BPTree(keysNumber, comparator);
        PATH = new File(path);
        OBJ_SIZE = objSize;
    }
    
    @Override
    public int size() {
        return values().size();
    }

    @Override
    public boolean isEmpty() {
        return tree.getRoot().getNodeSize() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return tree.search((K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Object put(Object key, Object value) {
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
        
        tree.add((K) key, pos);
        return value;
    }

    @Override
    public void putAll(Map m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection values() {
        ArrayList values = new ArrayList();
        for(Long pos : tree.values()){
            values.add(getObject(pos));
        }
        return values;
    }

    @Override
    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(Object key) {
        long pos = tree.search((K) key);
        return getObject(pos);
    }

    @Override
    public V remove(Object key) {
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

}
