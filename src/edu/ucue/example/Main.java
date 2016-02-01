/**
 * Ejemplo de uso del árbol B+.
 */
package edu.ucue.example;

import edu.ucue.bptree.BPTree;
import edu.ucue.bptree.BPTreeMap;
import edu.ucue.bptree.IndexGenerator;
import edu.ucue.bptree.ObjectSizeException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

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
         * Uso del árbol B+ con archivos (tabla de índices primaria/secundaria
         * y tabla de valores).
        *********************************************************************/
        
        // Objetos de prueba.
        Person p1 = new Person("0003", "Perez000", 25);
        Person p2 = new Person("0002", "Perez001", 33);
        Person p3 = new Person("0001", "Perez002", 33);
        Person p4 = new Person("0000", "Perez003", 33);
               
        // Obtener tamaño del objeto serializado
        // (113 en el caso de los objetos de prueba creados).
        // * Si el objeto varía con el tiempo, escoger un tamaño
        // límite superior al obtenido.
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
        
        // Ruta donde se manejara la tabla de indice secundario.
        String treePathSec = "data/persons_name_index.dat";
        
        File dir = new File("data");
        dir.mkdir();
               
        // Construir map con el tipo de clave y el tipo de objeto a almacenar.
        /*
        BPTreeMap<String, Person> bpTreeMap = null;
        try {
            bpTreeMap = BPTreeMap.getTree(3, new ComparatorString(), dataPath, treePath, 300, 1500);
            
            // Agregar tabla de indice secundaria
            bpTreeMap.addSecIndex(treePathSec, new NombreGenerator(), 1500);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
        // Agregamos los objetos al arbol
        /*
        try {
            bpTreeMap.put(p3.lastName, p3);
            bpTreeMap.put(p4.lastName, p4);
            bpTreeMap.put(p1.lastName, p1);
            bpTreeMap.put(p2.lastName, p2);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
        // Recuperamos
        /*
        try {
            System.out.println("De tabla de indice primaria (Ordenados por apellidos).");
            for(Person p : bpTreeMap.values())
                System.out.println(p);
            
            System.out.println("De tabla de indice sec (Ordenados por nombre).");
            for(Person p : bpTreeMap.valuesOf(0))
                System.out.println(p);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
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

        System.out.println("Orden de inserción: " + String.join(" ", letters));
        for(int i = 0; i < letters.length; i++){
            try {
                tree.add(letters[i], 0L); // Agregada una posicion cualquiera sólo por prueba.
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        System.out.println(tree);
        
        try {
            tree.showAll();
            System.out.println();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        */
        
        // Prueba con claves de tipo entero.
        /*
        BPTree<Integer> tree = null;
        
        try {
            tree = BPTree.getTree(3, new ComparatorInt(), treePath, 1500);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        ArrayList<Integer> n = new ArrayList();
        for(int i = 0; i < 100; i++)
            n.add(i);
        Collections.shuffle(n);
        
        try{
            for(int i = 0; i < n.size(); i++){
                tree.add(n.get(i), 0L);
            }
        } catch (Exception ex){
            System.out.println(ex);
        }
        
        System.out.println(tree);

        try{
            tree.showAll();
            System.out.println();
        } catch(Exception ex){
            System.out.println(ex);
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
