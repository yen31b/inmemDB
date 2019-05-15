package structures.trees;

import structures.nodes.ListaSimpleNode;

public class Splay<AnyType extends Comparable<AnyType>> extends Arbol<AnyType> {
    private Splay.BinaryNode<AnyType> newNode = null;
    private Splay.BinaryNode<AnyType> header = new Splay.BinaryNode((Object)null, (ListaSimpleNode)null);
    private Splay.BinaryNode<AnyType> root;
    private Splay.BinaryNode<AnyType> nullNode = new Splay.BinaryNode((Object)null, (ListaSimpleNode)null);

    public Splay() {
        this.nullNode.left = this.nullNode.right = this.nullNode;
        this.root = this.nullNode;
    }

    public void insert(AnyType x, ListaSimpleNode node) {
        if (this.newNode == null) {
            this.newNode = new Splay.BinaryNode((Object)null, node);
        }

        this.newNode.element = x;
        if (this.root == this.nullNode) {
            this.newNode.left = this.newNode.right = this.nullNode;
            this.root = this.newNode;
        } else {
            this.root = this.splay(x, this.root);
            int compareResult = x.compareTo((Comparable)this.root.element);
            if (compareResult < 0) {
                this.newNode.left = this.root.left;
                this.newNode.right = this.root;
                this.root.left = this.nullNode;
                this.root = this.newNode;
            } else {
                if (compareResult <= 0) {
                    return;
                }

                this.newNode.right = this.root.right;
                this.newNode.left = this.root;
                this.root.right = this.nullNode;
                this.root = this.newNode;
            }
        }

        this.newNode = null;
    }

    public void delete(AnyType x) {
        this.root = this.splay(x, this.root);
        if (((Comparable)this.root.element).compareTo(x) == 0) {
            Splay.BinaryNode newTree;
            if (this.root.left == this.nullNode) {
                newTree = this.root.right;
            } else {
                newTree = this.root.left;
                newTree = this.splay(x, newTree);
                newTree.right = this.root.right;
            }

            this.root = newTree;
        }
    }

    public ListaSimpleNode search(AnyType x) {
        ListaSimpleNode retorno = null;
        if (this.isEmpty()) {
            return retorno;
        } else {
            this.root = this.splay(x, this.root);
            if (((Comparable)this.root.element).compareTo(x) == 0) {
                retorno = this.root.U;
            }

            return retorno;
        }
    }

    public void makeEmpty() {
        this.root = this.nullNode;
    }

    public boolean isEmpty() {
        return this.root == this.nullNode;
    }

    private Splay.BinaryNode<AnyType> splay(AnyType x, Splay.BinaryNode<AnyType> t) {
        this.header.left = this.header.right = this.nullNode;
        Splay.BinaryNode rightTreeMin;
        Splay.BinaryNode<AnyType> leftTreeMax = rightTreeMin = this.header;
        this.nullNode.element = x;

        while(true) {
            int compareResult = x.compareTo((Comparable)t.element);
            if (compareResult < 0) {
                if (x.compareTo((Comparable)t.left.element) < 0) {
                    t = rotateWithLeftChild(t);
                }

                if (t.left == this.nullNode) {
                    break;
                }

                rightTreeMin.left = t;
                rightTreeMin = t;
                t = t.left;
            } else {
                if (compareResult <= 0) {
                    break;
                }

                if (x.compareTo((Comparable)t.right.element) > 0) {
                    t = rotateWithRightChild(t);
                }

                if (t.right == this.nullNode) {
                    break;
                }

                leftTreeMax.right = t;
                leftTreeMax = t;
                t = t.right;
            }
        }

        leftTreeMax.right = t.left;
        rightTreeMin.left = t.right;
        t.left = this.header.right;
        t.right = this.header.left;
        return t;
    }

    private static <AnyType> Splay.BinaryNode<AnyType> rotateWithLeftChild(Splay.BinaryNode<AnyType> k2) {
        Splay.BinaryNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    private static <AnyType> Splay.BinaryNode<AnyType> rotateWithRightChild(Splay.BinaryNode<AnyType> k1) {
        Splay.BinaryNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private static class BinaryNode<AnyType> {
        AnyType element;
        Splay.BinaryNode<AnyType> left;
        Splay.BinaryNode<AnyType> right;
        ListaSimpleNode U;

        BinaryNode(AnyType theElement, ListaSimpleNode node) {
            this(theElement, (Splay.BinaryNode)null, (Splay.BinaryNode)null, node);
        }

        BinaryNode(AnyType theElement, Splay.BinaryNode<AnyType> lt, Splay.BinaryNode<AnyType> rt, ListaSimpleNode node) {
            this.element = theElement;
            this.left = lt;
            this.right = rt;
            this.U = node;
        }
    }
}

