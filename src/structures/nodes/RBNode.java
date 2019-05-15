package structures.nodes;

import structures.nodes.ListaSimpleNode;


public class RBNode<T extends Comparable<T>> {
    private static final char RED = 'R';
    private static final char BLACK = 'B';
    private T data;
    private char colour;
    private RBNode<T> rightChild;
    private RBNode<T> leftChild;
    private boolean deleted;
    private ListaSimpleNode U;

    public RBNode(T data, ListaSimpleNode node) {
        this.data = data;
        this.colour = 'R';
        this.rightChild = null;
        this.leftChild = null;
        this.deleted = false;
        this.U = node;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean setColour(char c) {
        if ((c == 'R' || c == 'B') && c != this.colour) {
            this.colour = c;
            return true;
        } else {
            return false;
        }
    }

    public void setLeftChild(RBNode<T> node) {
        this.leftChild = node;
    }

    public void setRightChild(RBNode<T> node) {
        this.rightChild = node;
    }

    public void delete() {
        this.deleted = true;
    }

    public T getData() {
        return this.data;
    }

    public char getColour() {
        return this.colour;
    }

    public RBNode<T> getLeftChild() {
        return this.leftChild;
    }

    public RBNode<T> getRightChild() {
        return this.rightChild;
    }

    public ListaSimpleNode get_Node() {
        return this.U;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void display(int n) {
        String indent = "- ";

        int i;
        for(i = 1; i <= n; ++i) {
            System.out.print(indent);
        }

        System.out.println("ROOT: " + this.data + ", colour: " + this.colour);

        for(i = 1; i <= n; ++i) {
            System.out.print(indent);
        }

        System.out.println("LEFT");
        if (this.leftChild == null) {
            for(i = 1; i <= n + 1; ++i) {
                System.out.print(indent);
            }

            System.out.println("null");
        } else {
            this.leftChild.display(n + 1);
        }

        for(i = 1; i <= n; ++i) {
            System.out.print(indent);
        }

        System.out.println("RIGHT");
        if (this.rightChild == null) {
            for(i = 1; i <= n + 1; ++i) {
                System.out.print(indent);
            }

            System.out.println("null");
        } else {
            this.rightChild.display(n + 1);
        }

    }
}
