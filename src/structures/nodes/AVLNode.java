package structures.nodes;

import structures.nodes.ListaSimpleNode;

public class AVLNode<E extends Comparable<? super E>> {
    private AVLNode<E> left;
    private AVLNode<E> right;
    private E element;
    private Node<?> auxiliar;
    private ListaSimpleNode U;
    private String Error = "error";

    public AVLNode() {
        this((Comparable)null, (ListaSimpleNode)null);
    }

    public AVLNode(E element, ListaSimpleNode node) {
        this.element = element;
        this.left = this.right = null;
        this.auxiliar = null;
        this.U = node;
    }

    public E getElement() {
        return this.element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public AVLNode<E> getLeft() {
        return this.left;
    }

    public AVLNode<E> getRight() {
        return this.right;
    }

    public void setLeft(E element, ListaSimpleNode node) {
        if (this.left == null) {
            this.left = new AVLNode(element, node);
        } else {
            this.left.setElement(element);
        }

    }

    public void setRight(E element, ListaSimpleNode node) {
        if (this.right == null) {
            this.right = new AVLNode(element, node);
        } else {
            this.right.setElement(element);
        }

    }

    public void setLeftNode(AVLNode<E> temp) {
        this.left = temp;
    }

    public void setRightNode(AVLNode<E> temp) {
        this.right = temp;
    }

    public int getBalance() {
        int leftHeight = this.left == null ? 0 : this.left.height();
        int rightHeight = this.right == null ? 0 : this.right.height();
        return rightHeight - leftHeight;
    }

    private int height() {
        int leftHeight = this.left == null ? 0 : this.left.height();
        int rightHeight = this.right == null ? 0 : this.right.height();
        return 1 + Math.max(leftHeight, rightHeight);
    }

    public String toString() {
        return this.assemble(this, 0);
    }

    private String assemble(AVLNode<E> temp, int offset) {
        String ret = "";

        for(int i = 0; i < offset; ++i) {
            ret = ret + "\t";
        }

        ret = ret + temp.getElement() + "\n";
        if (temp.getLeft() != null) {
            ret = ret + "Left: " + this.assemble(temp.getLeft(), offset + 1);
        }

        if (temp.getRight() != null) {
            ret = ret + "Right: " + this.assemble(temp.getRight(), offset + 1);
        }

        return ret;
    }

    public Node<?> get_Auxiliar() {
        return this.auxiliar;
    }

    public void set_Auxiliar(Node<?> referencia) {
        this.auxiliar = referencia;
    }

    public ListaSimpleNode get_Node() {
        return this.U;
    }
}
