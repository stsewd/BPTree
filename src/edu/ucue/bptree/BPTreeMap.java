/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Santos Gallegos
 */
public class BPTreeMap<K, V> implements Map<K, V> {
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
            Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ram.close();
            } catch (IOException ex) {
                Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
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
        tree.del((K) key);
        return null;
    }
    
    private V getObject(long pos){
        RandomAccessFile ram = null;
        byte[] objByte = new byte[OBJ_SIZE];
        V obj = null;
        try {
            ram = new RandomAccessFile(PATH, "rw");
            
            ram.seek(pos);
            
            ram.read(objByte);
            
            obj = (V) deserialize(objByte);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ram.close();
            } catch (IOException ex) {
                Logger.getLogger(BPTreeMap.class.getName()).log(Level.SEVERE, null, ex);
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
