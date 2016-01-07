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
        BPTree<Integer> tree = new BPTree(2, new ComparatorInt());
        System.out.println(tree.toString());
        tree.add(2, 2);
        System.out.println(tree.toString());
        tree.add(3, 3);
        System.out.println(tree.toString());
    }
    
}
