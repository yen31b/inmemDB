package structures.trees;

import structures.nodes.ListaSimpleNode;

public class ArbolAA<AnyType extends Comparable<AnyType>> extends Arbol<AnyType> {
    private ArbolAA.AANode<AnyType> root;
    private ArbolAA.AANode<AnyType> nullNode = new ArbolAA.AANode((Object)null, (ArbolAA.AANode)null, (ArbolAA.AANode)null, (ListaSimpleNode)null);
    private ArbolAA.AANode<AnyType> deletedNode;
    private ArbolAA.AANode<AnyType> lastNode;

    public ArbolAA() {
        this.nullNode.left = this.nullNode.right = this.nullNode;
        this.nullNode.level = 0;
        this.root = this.nullNode;
    }

    public void insert(AnyType x, ListaSimpleNode node) {
        this.root = this.insert(x, this.root, node);
    }

    public void delete(AnyType x) {
        this.deletedNode = this.nullNode;
        this.root = this.remove(x, this.root);
    }

    public AnyType findMin() {
        if (this.isEmpty()) {
            return null;
        } else {
            ArbolAA.AANode ptr;
            for(ptr = this.root; ptr.left != this.nullNode; ptr = ptr.left) {
            }

            return (Comparable)ptr.element;
        }
    }

    public AnyType findMax() {
        if (this.isEmpty()) {
            return null;
        } else {
            ArbolAA.AANode ptr;
            for(ptr = this.root; ptr.right != this.nullNode; ptr = ptr.right) {
            }

            return (Comparable)ptr.element;
        }
    }

    public ListaSimpleNode search(AnyType x) {
        ArbolAA.AANode<AnyType> current = this.root;
        ListaSimpleNode node = this.root.U;
        this.nullNode.element = x;

        while(true) {
            while(x.compareTo((Comparable)current.element) >= 0) {
                if (x.compareTo((Comparable)current.element) <= 0) {
                    if (current != this.nullNode) {
                        return node;
                    }

                    return null;
                }

                current = current.right;
                node = current.right.U;
            }

            current = current.left;
            node = current.left.U;
        }
    }

    public void makeEmpty() {
        this.root = this.nullNode;
    }

    public boolean isEmpty() {
        return this.root == this.nullNode;
    }

    private ArbolAA.AANode<AnyType> insert(AnyType x, ArbolAA.AANode<AnyType> t, ListaSimpleNode node) {
        if (t == this.nullNode) {
            t = new ArbolAA.AANode(x, this.nullNode, this.nullNode, node);
        } else if (x.compareTo((Comparable)t.element) < 0) {
            t.left = this.insert(x, t.left, node);
        } else {
            if (x.compareTo((Comparable)t.element) <= 0) {
                throw new RuntimeException(x.toString());
            }

            t.right = this.insert(x, t.right, node);
        }

        t = skew(t);
        t = split(t);
        return t;
    }

    private ArbolAA.AANode<AnyType> remove(AnyType x, ArbolAA.AANode<AnyType> t) {
        if (t != this.nullNode) {
            this.lastNode = t;
            if (x.compareTo((Comparable)t.element) < 0) {
                t.left = this.remove(x, t.left);
            } else {
                this.deletedNode = t;
                t.right = this.remove(x, t.right);
            }

            if (t == this.lastNode) {
                if (this.deletedNode == this.nullNode || x.compareTo((Comparable)this.deletedNode.element) != 0) {
                    throw new RuntimeException(x.toString());
                }

                this.deletedNode.element = (Comparable)t.element;
                t = t.right;
            } else if (t.left.level < t.level - 1 || t.right.level < t.level - 1) {
                if (t.right.level > --t.level) {
                    t.right.level = t.level;
                }

                t = skew(t);
                t.right = skew(t.right);
                t.right.right = skew(t.right.right);
                t = split(t);
                t.right = split(t.right);
            }
        }

        return t;
    }

    private static <AnyType> ArbolAA.AANode<AnyType> skew(ArbolAA.AANode<AnyType> t) {
        if (t.left.level == t.level) {
            t = rotateWithLeftChild(t);
        }

        return t;
    }

    private static <AnyType> ArbolAA.AANode<AnyType> split(ArbolAA.AANode<AnyType> t) {
        if (t.right.right.level == t.level) {
            t = rotateWithRightChild(t);
            ++t.level;
        }

        return t;
    }

    private static <AnyType> ArbolAA.AANode<AnyType> rotateWithLeftChild(ArbolAA.AANode<AnyType> k2) {
        ArbolAA.AANode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    private static <AnyType> ArbolAA.AANode<AnyType> rotateWithRightChild(ArbolAA.AANode<AnyType> k1) {
        ArbolAA.AANode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private static class AANode<AnyType> {
        AnyType element;
        ArbolAA.AANode<AnyType> left;
        ArbolAA.AANode<AnyType> right;
        int level;
        ListaSimpleNode U;

        AANode(AnyType theElement, ArbolAA.AANode<AnyType> lt, ArbolAA.AANode<AnyType> rt, ListaSimpleNode node) {
            this.element = theElement;
            this.left = lt;
            this.right = rt;
            this.level = 1;
            this.U = node;
        }
    }
}

