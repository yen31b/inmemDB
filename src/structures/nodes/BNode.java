package structures.nodes;

import structures.nodes.ListaSimpleNode;

public class BNode<T> {
    public T information;
    public BNode<T> parent = null;
    public BNode<T> left = null;
    public BNode<T> right = null;
    public ListaSimpleNode U = null;
    public char balance;

    public BNode(T information, BNode parent, ListaSimpleNode node) {
        this.information = information;
        this.parent = parent;
        this.left = null;
        this.right = null;
        this.balance = '_';
        this.U = node;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public boolean isNode() {
        return !this.isLeaf();
    }

    public boolean hasLeftNode() {
        return this.left != null;
    }

    public boolean hasRightNode() {
        return this.right != null;
    }

    public boolean isLeftNode() {
        return this.parent.left == this;
    }

    public boolean isRightNode() {
        return this.parent.right == this;
    }

    public ListaSimpleNode get_Node() {
        return this.U;
    }
}

