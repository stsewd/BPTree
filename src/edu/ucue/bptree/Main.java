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
        BPTree<String, Integer> tree = new BPTree(4, new ComparatorString());
        System.out.println(tree.toString());
        tree.add("Z", 1);
        System.out.println(tree.toString());
        tree.add("B", 2);
        System.out.println(tree.toString());
        tree.add("D", 2);
        System.out.println(tree.toString());
        tree.add("A", 2);
        System.out.println(tree.toString());
    }
    
}
