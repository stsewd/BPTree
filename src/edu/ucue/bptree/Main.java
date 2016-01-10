/**
 * B+ Tree / Árbol B+
 */
package edu.ucue.bptree;

/**
 *
 * @author Santos Gallegos
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Corregir -> Ceil de N/2 no floor de N/2
        String[] letters = {"Z", "W", "R", "A", "C", "B", "D", "E", "F", "H", "G", "M", "L", "I", "J", "K"};
        
        BPTree<String, Integer> tree = new BPTree(3, new ComparatorString());
        
        System.out.println("Orden de insercion: " + String.join(" ", letters));
        for(int i = 0; i < letters.length; i++)
            tree.add(letters[i], i);
        tree.del("J");
        tree.del("R");
        tree.del("Z");
        tree.del("W");
        System.out.println(tree);
        tree.showAll();
        System.out.println();
    }
}
