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
        tree.add("A", 2);
        tree.add("C", 2);
        tree.add("B", 2);
        tree.add("D", 2);
        tree.add("E", 2);
        tree.add("F", 2);
        System.out.println("Z W R A C B D E F *********************");
        System.out.println(tree);
        tree.add("H", 2);
        System.out.println("H *********************");
        System.out.println(tree);
        // aquí empieza el problema
        tree.add("G", 2);
        System.out.println("G *********************");
        System.out.println(tree);
        tree.add("M", 2);
        System.out.println("M *********************");
        System.out.println(tree);
    }
    
}
