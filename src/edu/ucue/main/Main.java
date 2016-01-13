/**
 * B+ Tree / Árbol B+
 */
package edu.ucue.main;

import edu.ucue.bptree.BPTree;
import java.util.ArrayList;
import java.util.Collection;
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
        */
        
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
    }
}
