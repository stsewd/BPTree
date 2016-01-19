/**
 * B+ Tree / Árbol B+
 */
package edu.ucue.main;

import edu.ucue.bptree.BPTree;
import edu.ucue.bptree.BPTreeMap;
import edu.ucue.bptree.IndexGenerator;
import edu.ucue.bptree.ObjectSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        
        /********************************************************************
         * Uso del árbol B+ con archivos (tabla de índices y valores).
        *********************************************************************/
        
        // Objetos de prueba.
        Person p1 = new Person("0003", "Perez000", 25);
        Person p2 = new Person("0002", "Perez001", 33);
        Person p3 = new Person("0001", "Perez002", 33);
        Person p4 = new Person("0000", "Perez003", 33);
        
        // Obtener tamaño del objeto serializado
        // (113 en el caso de los objetos de prueba creados).
        // * Asegurarse que el tamaño de todos los objetos
        // el mismo para todos.
        /*
        try {
            byte[] b;
            b = serialize(p1);
            System.out.println(b.length);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
        // Ruta donde se manejará la tabla de valores.
        String dataPath = "data/persons.dat";
        
        // Ruta donde se manejara tabla de índices.
        String treePath = "data/persons_lastname_index.dat";
        String treePathSec = "data/persons_name_index.dat";
        
        // Construir map con el tipo de clave y el tipo de objeto a almacenar.
        BPTreeMap<String, Person> bpTreeMap = null;
        try {
            bpTreeMap = BPTreeMap.getTree(3, new ComparatorString(), dataPath, treePath, 300);
            
            // Agregar tabla de indice secundaria
            bpTreeMap.addSecIndex(treePathSec, new NombreGenerator());
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        // Agregamos los objetos al arbol
        try {
            bpTreeMap.put(p3.lastName, p3);
            bpTreeMap.put(p4.lastName, p4);
            bpTreeMap.put(p1.lastName, p1);
            bpTreeMap.put(p2.lastName, p2);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        // Recuperamos
        try {
            System.out.println("De tabla de indice primaria.");
            for(Person p : bpTreeMap.values())
                System.out.println(p);
            
            System.out.println("De tabla de indice sec.");
            for(Person p : bpTreeMap.valuesOf(0))
                System.out.println(p);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        /*********************************************************************
         *  Uso de árbol B+ solo con archivo de indice.
        *********************************************************************/
        
        // Prueba con claves de tipo String
        
        /*
        BPTree<String> tree = null;
        
        try {
            tree = BPTree.getTree(3, new ComparatorString(), treePath, 1500);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
         
        String[] letters = {
            "Z", "W", "R", "A", "C", "B", "D", "E", "F",
            "H", "G", "M", "L", "I", "J", "K", "P","X",
            "N", "O", "Y", "S", "T", "U", "V"
        };
        

        System.out.println("Orden de insercion: " + String.join(" ", letters));
        for(int i = 0; i < letters.length; i++){
            try {
                tree.add(letters[i], 0L);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        System.out.println(tree);
        
        try {
            tree.showAll();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
        // Prueba con claves de tipo entero.
        
        /*
        ArrayList<Integer> n = new ArrayList();
        for(int i = 0; i < 10; i++)
            n.add(i);
        Collections.shuffle(n);
        */
        
        /*
        int[] n = new int[]{5, 3, 8, 0, 2, 6, 7, 9, 4, 1};
        
        BPTree<Integer> tree = null;
        
        try {
            tree = BPTree.getTree(3, new ComparatorInt(), treePath, 1500);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
        /*
        try {
            tree.add(3, 0L);
            tree.add(5, 0L);
            tree.add(8, 0L);
            System.out.println(tree);
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        }
        */
        
        /*
        //System.out.println(n);
        System.out.println("5, 3, 8, 0, 2, 6, 7, 9, 4, 1");
        try {
            for(int i = 0; i < n.length; i++)
                tree.add(n[i], 0L);
            System.out.println(tree);
            tree.showAll();
            System.out.println();
            
            tree.del(8);
            System.out.println(tree);
            tree.showAll();
            System.out.println();
            
            tree.del(9);
            System.out.println(tree);
            tree.showAll();
            System.out.println();
            
            tree.del(5);
            System.out.println(tree);
            tree.showAll();
            System.out.println();
            
            tree.del(2);
            System.out.println(tree);
            tree.showAll();
            System.out.println();
            
            tree.del(1);
            System.out.println(tree);
            tree.showAll();
            System.out.println();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        */
        
        /*
        try{
            tree.del(3);
            tree.del(4);
            System.out.println(tree);
            tree.showAll();
        System.out.println();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
    }
    
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}
