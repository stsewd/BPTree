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
        System.out.println("Z W R *********************");
        System.out.println(tree);
        tree.add("A", 2);
        System.out.println("A *********************");
        System.out.println(tree);
        tree.add("C", 2);
        tree.add("B", 2);
        System.out.println("C B *********************");
        System.out.println(tree);
        tree.add("D", 2);
        System.out.println("D *********************");
        System.out.println(tree);
        tree.add("E", 2);
        System.out.println("E *********************");
        System.out.println(tree);
        tree.add("F", 2);
        System.out.println("F *********************");
        System.out.println(tree);
        // aquí empieza el problema
        tree.add("H", 2);
        System.out.println("H *********************");
        System.out.println(tree);
        //tree.add("G", 2);
        //tree.add("M", 2);
        
    }
    
}
