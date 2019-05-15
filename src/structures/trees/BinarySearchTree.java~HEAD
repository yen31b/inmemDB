package structures.trees;

import structures.nodes.ListaSimpleNode;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<T>> extends Arbol<T> {
    private BinarySearchTree<T>.Node<T> root = null;
    private Comparator<T> comparator;

    public BinarySearchTree() {
        this.comparator = null;
    }

    public BinarySearchTree(Comparator<T> comp) {
        this.comparator = comp;
    }

    private int compare(T x, T y) {
        return this.comparator == null ? x.compareTo(y) : this.comparator.compare(x, y);
    }

    public void insert(T data, ListaSimpleNode Nodo) {
        this.root = this.insert(this.root, data, Nodo);
    }

    private BinarySearchTree<T>.Node<T> insert(BinarySearchTree<T>.Node<T> p, T toInsert, ListaSimpleNode Nodo) {
        if (p == null) {
            return new BinarySearchTree.Node(toInsert, Nodo);
        } else if (this.compare(toInsert, (Comparable)p.data) == 0) {
            return p;
        } else {
            if (this.compare(toInsert, (Comparable)p.data) < 0) {
                p.left = this.insert(p.left, toInsert, Nodo);
            } else {
                p.right = this.insert(p.right, toInsert, Nodo);
            }

            return p;
        }
    }

    public ListaSimpleNode search(T toSearch) {
        return this.search(this.root, toSearch);
    }

    private ListaSimpleNode search(BinarySearchTree<T>.Node<T> p, T toSearch) {
        if (p == null) {
            return null;
        } else if (this.compare(toSearch, (Comparable)p.data) == 0) {
            return p.get_Node();
        } else {
            return this.compare(toSearch, (Comparable)p.data) < 0 ? this.search(p.left, toSearch) : this.search(p.right, toSearch);
        }
    }

    public void delete(T toDelete) {
        this.root = this.delete(this.root, toDelete);
    }

    private BinarySearchTree<T>.Node<T> delete(BinarySearchTree<T>.Node<T> p, T toDelete) {
        if (p == null) {
            throw new RuntimeException("cannot delete.");
        } else {
            if (this.compare(toDelete, (Comparable)p.data) < 0) {
                p.left = this.delete(p.left, toDelete);
            } else if (this.compare(toDelete, (Comparable)p.data) > 0) {
                p.right = this.delete(p.right, toDelete);
            } else {
                if (p.left == null) {
                    return p.right;
                }

                if (p.right == null) {
                    return p.left;
                }

                p.data = this.retrieveData(p.left);
                p.left = this.delete(p.left, (Comparable)p.data);
            }

            return p;
        }
    }

    private T retrieveData(BinarySearchTree<T>.Node<T> p) {
        while(p.right != null) {
            p = p.right;
        }

        return (Comparable)p.data;
    }

    public Iterator<T> iterator() {
        return new BinarySearchTree.MyIterator();
    }

    private class MyIterator implements Iterator<T> {
        Stack<BinarySearchTree<T>.Node<T>> stk = new Stack();

        public MyIterator() {
            if (BinarySearchTree.this.root != null) {
                this.stk.push(BinarySearchTree.this.root);
            }

        }

        public boolean hasNext() {
            return !this.stk.isEmpty();
        }

        public T next() {
            BinarySearchTree<T>.Node<T> cur = (BinarySearchTree.Node)this.stk.peek();
            if (cur.left != null) {
                this.stk.push(cur.left);
            } else {
                BinarySearchTree.Node tmp;
                for(tmp = (BinarySearchTree.Node)this.stk.pop(); tmp.right == null; tmp = (BinarySearchTree.Node)this.stk.pop()) {
                    if (this.stk.isEmpty()) {
                        return (Comparable)cur.data;
                    }
                }

                this.stk.push(tmp.right);
            }

            return (Comparable)cur.data;
        }

        public void remove() {
        }
    }

    private class Node<T> {
        private T data;
        private ListaSimpleNode U;
        private BinarySearchTree<T>.Node<T> left;
        private BinarySearchTree<T>.Node<T> right;

        public Node(T data, BinarySearchTree<T>.Node<T> l, BinarySearchTree<T>.Node<T> r, ListaSimpleNode Nodo) {
            this.left = l;
            this.right = r;
            this.data = data;
            this.U = Nodo;
        }

        public Node(T data, ListaSimpleNode Nodo) {
            this(data, (BinarySearchTree.Node)null, (BinarySearchTree.Node)null, Nodo);
        }

        public ListaSimpleNode get_Node() {
            return this.U;
        }

        public String toString() {
            return this.data.toString();
        }
    }
}
