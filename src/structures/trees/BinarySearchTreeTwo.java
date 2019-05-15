package structures.trees;

import structures.nodes.BSTNode;

public class BinarySearchTreeTwo<T extends Comparable<T>>{
    public BSTNode<T> root;
    private String Error = "error";

    /**
     * Adds a node to the binary search tree
     * @param data data of type T that implements the Comparable interface
     */
    public void insert(T data){
        root = insert(root, data);
    }

    private BSTNode<T> insert(BSTNode<T> root, T data)
    {
        // instantiate a new Node with data as data if the current root has not been previously instantiated
        if (root == null)
        {
            return new BSTNode<>(data);
        }
        // if the value of the data being searched for is less than the value of the current root node, then
        // traverse to the left node of the current root, setting the current left node to whatever gets returned
        // from the insert method
        else if (data.compareTo(root.data) < 0)
        {
            root.left = insert(root.left, data);
        }
        // if the value of the data being searched for is less than the value of the current root node, then
        // traverse to the right node of the current root, setting the current right node to whatever gets returned
        // from the insert method
        else if (data.compareTo(root.data) > 0)
        {
            root.right = insert(root.right, data);
        }

        return root;
    }
}
