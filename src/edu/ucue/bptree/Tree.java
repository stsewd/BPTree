/**
 * Tree / Árbol
 */
package edu.ucue.bptree;

/**
 *
 * @author Santos Gallegos
 */
public class Tree<K> {
    private Node<K> root; // Nodo raíz
    private final int keysNumber; // Número máximo de claves

    /**
     * Crea un nuevo árbol vacío.
     * @param KeysNumber 
     */
    public Tree(int KeysNumber) {
        this.keysNumber = KeysNumber;
        this.root = new Node(true, null, keysNumber);
    }

    @Override
    public String toString() {
        String str = "";
        toString(str, root, 0);
        return str;
    }
    
    private void toString(String str, Node node, int indexKey){
        if(indexKey >= node.getNodeSize()){
            str += "\n";
            return;
        }
        
        str += node.getKey(indexKey) + " ";
        
        if(!node.isLeaf())
            toString(str, (Node) node.getValue(indexKey), 0);
        toString(str, node, indexKey + 1);
    }
    
}
