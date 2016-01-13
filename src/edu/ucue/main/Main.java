/**
 * B+ Tree / Árbol B+
 */
package edu.ucue.main;

import edu.ucue.bptree.BPTree;
import edu.ucue.bptree.BPTreeMap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Santos Gallegos
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*********************************************************************/
        /*********************************************************************/
        
        // Uso del árbol B+ con archivos (tabla de valores).
        
        // Objetos de prueba.
        Person p1 = new Person("0000", "Perez000", 25);
        Person p2 = new Person("0001", "Perez001", 33);
        Person p3 = new Person("0002", "Perez002", 33);
        Person p4 = new Person("0003", "Perez003", 33);
        
        // Obtener tamaño del objeto serializado
        // (113 en el caso de los objetos de prueba creados).
        // * Asegurarse que el tamaño de todos los objetos
        // el mismo para todos.
        /* 
        try {
            byte[] b;
            b = serialize(p1);
            System.out.println(b.length);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
        */
        
        // Ruta donde se manejará la tabla de valores.
        String path = "persons.dat";
        
        // Ruta donde se manejara tabla de índices.
        String arbolPath = "arbolPersons.dat";
        
        // Construir map con el tipo de clave y el tipo de objeto a almacenar.
        Map<String, Person> mapaTest = cargarArbol(path, arbolPath);
        
        // Agregamos los objetos al map
        /*
        mapaTest.put(p3.lastName, p3);
        mapaTest.put(p4.lastName, p4);
        mapaTest.put(p1.lastName, p1);
        mapaTest.put(p2.lastName, p2);
        /*/
        
        // Recuperamos
        
        // System.out.println(mapaTest.get(p4.lastName).toString());
        // System.out.println(mapaTest.get(p1.lastName).toString());
        /*
        for(Person p : mapaTest.values())
            System.out.println(p);
        /*/
        
        // Guardar arbol
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arbolPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(mapaTest);
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no existente.");
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo.");
        }
        
        
        /*********************************************************************/
        /*********************************************************************/
        
        // Uso de árbol B+ sin archivos
        /*
        String[] letters = {
            "Z", "W", "R", "A", "C", "B", "D", "E", "F",
            "H", "G", "M", "L", "I", "J", "K", "P", "X",
            "N", "O", "Y", "S", "T", "U", "V"
        };
        
        BPTree<String, Integer> tree = new BPTree(3, new ComparatorString());
        
        System.out.println("Orden de insercion: " + String.join(" ", letters));
        for(int i = 0; i < letters.length; i++)
            tree.add(letters[i], i);
        
        ArrayList<Integer> n = new ArrayList();
        
        for(int i = 0; i < 1000; i++)
            n.add(i);
        Collections.shuffle(n);
        
        BPTree<Integer, Integer> tree = new BPTree(3, new ComparatorInt());
        System.out.println(n);
        for(int i = 0; i < 1000; i++)
            tree.add(n.get(i), i);
        System.out.println(tree);
        tree.showAll();
        System.out.println();
                
        */
        
    }
    
    public static Map cargarArbol(String data, String arbolPath){
        Map mapaTest = null;
        
        File path = new File(arbolPath);
        if(!path.exists())
            return new BPTreeMap(3, new ComparatorString(), data, 113);
        
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            mapaTest = (Map) ois.readObject();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no existente.");
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al recuperar objeto.");
        }
        
        return mapaTest;
    }
    
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}
