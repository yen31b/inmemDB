package structures.trees;

import structures.nodes.RBNode;
import structures.nodes.ListaSimpleNode;
import java.io.PrintWriter;

@SuppressWarnings("unchecked")
public class ArbolRN<T extends Comparable<T>> extends Arbol<T> {
    private String Error = "error";
    private static final char BLACK = 'B';
    private static final char RED = 'R';
    private RBNode<T> root = null;

    public ArbolRN() {
    }

    public RBNode<T> getRoot() {
        return this.root;
    }

    public void insert(T data, ListaSimpleNode node) throws IllegalArgumentException {
        try {
            this.root = this.put(this.root, data, node);
            this.root.setColour('B');
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    private RBNode<T> put(RBNode<T> node, T data, ListaSimpleNode Node) throws IllegalArgumentException {
        if (node == null) {
            RBNode<T> newNode = new RBNode(data, Node);
            return newNode;
        } else {
            int cmp = data.compareTo(node.getData());
            if (cmp < 0) {
                node.setLeftChild(this.put(node.getLeftChild(), data, Node));
            } else {
                if (cmp <= 0) {
                    throw new IllegalArgumentException("Data already exists in tree: " + data.toString());
                }

                node.setRightChild(this.put(node.getRightChild(), data, Node));
            }

            if (this.isRed(node.getLeftChild()) && this.isRed(node.getLeftChild().getLeftChild())) {
                node.setColour('R');
                node.getLeftChild().setColour('B');
                node = this.rightRotation(node);
            }

            if (this.isRed(node.getRightChild()) && this.isRed(node.getRightChild().getRightChild())) {
                node.setColour('R');
                node.getRightChild().setColour('B');
                node = this.leftRotation(node);
            }

            if (this.isRed(node.getLeftChild()) && this.isRed(node.getLeftChild().getRightChild())) {
                node.setColour('R');
                node.getLeftChild().getRightChild().setColour('B');
                node.setLeftChild(this.leftRotation(node.getLeftChild()));
                node = this.rightRotation(node);
            }

            if (this.isRed(node.getRightChild()) && this.isRed(node.getRightChild().getLeftChild())) {
                node.setColour('R');
                node.getRightChild().getLeftChild().setColour('B');
                node.setRightChild(this.rightRotation(node.getRightChild()));
                node = this.leftRotation(node);
            }

            this.colourFlip(node);
            return node;
        }
    }

    public ListaSimpleNode search(T data) {
        ListaSimpleNode retorno = this.root.get_Node();
        RBNode current = this.root;

        do {
            if (data.compareTo((T) current.getData()) == 0) {
                return retorno;
            }

            if (data.compareTo((T) current.getData()) < 0) {
                current = current.getLeftChild();
                retorno = current.getLeftChild().get_Node();
            } else {
                current = current.getRightChild();
                retorno = current.getRightChild().get_Node();
            }
        } while(current != null && !current.isDeleted());

        return null;
    }

    public void delete(T data) {
        ListaSimpleNode node = this.search(data);
        if (node != null) {
            node.delete();
        }
    }

    public boolean isRed(RBNode<T> node) {
        if (node == null) {
            return false;
        } else {
            return node.getColour() == 'R';
        }
    }

    private void colourFlip(RBNode<T> parent) {
        if (parent.getRightChild() != null && parent.getLeftChild() != null) {
            if (!this.isRed(parent) && this.isRed(parent.getRightChild()) && this.isRed(parent.getLeftChild())) {
                if (parent != this.root) {
                    parent.setColour('R');
                }

                parent.getRightChild().setColour('B');
                parent.getLeftChild().setColour('B');
            }

        }
    }

    private RBNode<T> rightRotation(RBNode<T> grandparent) {
        RBNode<T> parent = grandparent.getLeftChild();
        RBNode<T> rightChildOfParent = parent.getRightChild();
        parent.setRightChild(grandparent);
        grandparent.setLeftChild(rightChildOfParent);
        return parent;
    }

    private RBNode<T> leftRotation(RBNode<T> grandparent) {
        RBNode<T> parent = grandparent.getRightChild();
        RBNode<T> leftChildOfParent = parent.getLeftChild();
        parent.setLeftChild(grandparent);
        grandparent.setRightChild(leftChildOfParent);
        return parent;
    }

    public void displayElements(PrintWriter p) {
        this.displaySubtreeInOrder(this.root, p);
    }

    private void displaySubtreeInOrder(RBNode<T> current, PrintWriter p) {
        if (current != null) {
            this.displaySubtreeInOrder(current.getLeftChild(), p);
            p.println("Data is " + current.getData() + "Node colour: " + current.getColour());
            this.displaySubtreeInOrder(current.getRightChild(), p);
        }

    }

    public void printStructure() {
        if (this.root == null) {
            System.out.println("null");
        } else {
            System.out.println("*****************************************");
            this.root.display(0);
            System.out.println("*****************************************");
        }

        System.out.println();
    }
}
