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
        // TODO code application logic here
        BPTree<String, Integer> tree = new BPTree(3, new ComparatorString());
        tree.add("Z", 2);
        tree.add("W", 2);
        tree.add("R", 2);
        System.out.println(tree);
        
        tree.add("A", 2);
        tree.add("C", 2);
        tree.add("B", 2);
        System.out.println(tree);
        
        tree.add("D", 2);
        System.out.println(tree);
        
        tree.add("X", 2);
        System.out.println(tree);
    }
    
}
