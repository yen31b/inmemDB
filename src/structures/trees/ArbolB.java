package structures.trees;

import structures.nodes.BNode;
import structures.nodes.ListaSimpleNode;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ArbolB<T extends Comparable<T>> extends Arbol<T> {
    private BNode<T> root;

    public ArbolB() {
        this.root = null;
    }

    public ArbolB(BNode<T> root) {
        this.root = root;
    }

    public void insert(T info, ListaSimpleNode Node) {
        this.insert(info, this.root, (BNode)null, false, Node);
    }

    public ListaSimpleNode search(T info) {
        return this.search(info, this.root);
    }

    public void delete(T info) {
        this.delete(info, this.root);
    }

    public String toString() {
        return this.inOrder();
    }

    public String inOrder() {
        return this.inOrder(this.root);
    }

    public String preOrder() {
        return this.preOrder(this.root);
    }

    public String postOrder() {
        return this.postOrder(this.root);
    }

    public int getHeight() {
        return this.getHeight(this.root);
    }

    private void insert(T info, BNode<T> node, BNode<T> parent, boolean right, ListaSimpleNode Node) {
        if (node == null) {
            if (parent == null) {
                this.root = node = new BNode(info, parent, Node);
            } else if (right) {
                parent.right = node = new BNode(info, parent, Node);
            } else {
                parent.left = node = new BNode(info, parent, Node);
            }

            this.restructInsert(node, false);
        } else if (info.compareTo(node.information) == 0) {
            node.information = info;
        } else if (info.compareTo(node.information) > 0) {
            this.insert(info, node.right, node, true, Node);
        } else {
            this.insert(info, node.left, node, false, Node);
        }

    }

    private ListaSimpleNode search(T info, BNode<T> node) {
        if (node == null) {
            return null;
        } else if (info.compareTo(node.information) == 0) {
            return node.get_Node();
        } else {
            return info.compareTo(node.information) > 0 ? this.search(info, node.right) : this.search(info, node.left);
        }
    }

    private void delete(T info, BNode<T> node) throws NoSuchElementException {
        if (node == null) {
            throw new NoSuchElementException();
        } else {
            if (info.compareTo(node.information) == 0) {
                this.deleteNode(node);
            } else if (info.compareTo(node.information) > 0) {
                this.delete(info, node.right);
            } else {
                this.delete(info, node.left);
            }

        }
    }

    private void deleteNode(BNode<T> node) {
        BNode<T> delNode = null;
        boolean rightNode = false;
        if (node.isLeaf()) {
            if (node.parent == null) {
                this.root = null;
            } else if (node.isRightNode()) {
                node.parent.right = null;
                rightNode = true;
            } else if (node.isLeftNode()) {
                node.parent.left = null;
            }

            delNode = node;
        } else {
            BNode minMaxNode;
            if (node.hasLeftNode()) {
                minMaxNode = node.left;

                for(BNode eNode = node.left; eNode != null; eNode = eNode.right) {
                    minMaxNode = eNode;
                }

                delNode = minMaxNode;
                node.information = (T) minMaxNode.information;
                if (node.left.right != null) {
                    minMaxNode.parent.right = minMaxNode.left;
                    rightNode = true;
                } else {
                    minMaxNode.parent.left = minMaxNode.left;
                }

                if (minMaxNode.left != null) {
                    minMaxNode.left.parent = minMaxNode.parent;
                }
            } else if (node.hasRightNode()) {
                minMaxNode = node.right;
                delNode = minMaxNode;
                rightNode = true;
                node.information = (T) minMaxNode.information;
                node.right = minMaxNode.right;
                if (node.right != null) {
                    node.right.parent = node;
                }

                node.left = minMaxNode.left;
                if (node.left != null) {
                    node.left.parent = node;
                }
            }
        }

        this.restructDelete(delNode.parent, rightNode);
    }

    private int getHeight(BNode<T> node) {

        int height;
        if (node == null) {
            height = -1;
        } else {
            height = 1 + Math.max(this.getHeight(node.left), this.getHeight(node.right));
        }

        return height;
    }

    private String inOrder(BNode<T> node) {
        String result = "";
        if (node != null) {
            result = result + this.inOrder(node.left) + " ";
            result = result + ((Comparable)node.information).toString();
            result = result + this.inOrder(node.right);
        }

        return result;
    }

    private String preOrder(BNode<T> node) {
        String result = "";
        if (node != null) {
            result = result + ((Comparable)node.information).toString() + " ";
            result = result + this.preOrder(node.left);
            result = result + this.preOrder(node.right);
        }

        return result;
    }

    private String postOrder(BNode<T> node) {
        String result = "";
        if (node != null) {
            result = result + this.postOrder(node.left);
            result = result + this.postOrder(node.right);
            result = result + ((Comparable)node.information).toString() + " ";
        }

        return result;
    }

    private void restructInsert(BNode<T> node, boolean wasRight) {
        if (node != this.root) {
            if (node.parent.balance == '_') {
                if (node.isLeftNode()) {
                    node.parent.balance = '/';
                    this.restructInsert(node.parent, false);
                } else {
                    node.parent.balance = '\\';
                    this.restructInsert(node.parent, true);
                }
            } else if (node.parent.balance == '/') {
                if (node.isRightNode()) {
                    node.parent.balance = '_';
                } else if (!wasRight) {
                    this.rotateRight(node.parent);
                } else {
                    this.doubleRotateRight(node.parent);
                }
            } else if (node.parent.balance == '\\') {
                if (node.isLeftNode()) {
                    node.parent.balance = '_';
                } else if (wasRight) {
                    this.rotateLeft(node.parent);
                } else {
                    this.doubleRotateLeft(node.parent);
                }
            }
        }

    }

    private void restructDelete(BNode<T> z, boolean wasRight) {
        boolean isRight = false;
        boolean climb = false;
        if (z != null) {
            BNode<T> parent = z.parent;
            boolean canClimb = parent != null;
            if (canClimb) {
                isRight = z.isRightNode();
            }

            if (z.balance == '_') {
                if (wasRight) {
                    z.balance = '/';
                } else {
                    z.balance = '\\';
                }
            } else if (z.balance == '/') {
                if (wasRight) {
                    if (z.left.balance == '\\') {
                        this.doubleRotateRight(z);
                        climb = true;
                    } else {
                        this.rotateRight(z);
                        if (z.balance == '_') {
                            climb = true;
                        }
                    }
                } else {
                    z.balance = '_';
                    climb = true;
                }
            } else if (wasRight) {
                z.balance = '_';
                climb = true;
            } else if (z.right.balance == '/') {
                this.doubleRotateLeft(z);
                climb = true;
            } else {
                this.rotateLeft(z);
                if (z.balance == '_') {
                    climb = true;
                }
            }

            if (canClimb && climb) {
                this.restructDelete(parent, isRight);
            }

        }
    }

    private void rotateLeft(BNode<T> a) {
        BNode<T> b = a.right;
        if (a.parent == null) {
            this.root = b;
        } else if (a.isLeftNode()) {
            a.parent.left = b;
        } else {
            a.parent.right = b;
        }

        a.right = b.left;
        if (a.right != null) {
            a.right.parent = a;
        }

        b.parent = a.parent;
        a.parent = b;
        b.left = a;
        if (b.balance == '_') {
            a.balance = '\\';
            b.balance = '/';
        } else {
            a.balance = '_';
            b.balance = '_';
        }

    }

    private void rotateRight(BNode<T> a) {
        BNode<T> b = a.left;
        if (a.parent == null) {
            this.root = b;
        } else if (a.isLeftNode()) {
            a.parent.left = b;
        } else {
            a.parent.right = b;
        }

        a.left = b.right;
        if (a.left != null) {
            a.left.parent = a;
        }

        b.parent = a.parent;
        a.parent = b;
        b.right = a;
        if (b.balance == '_') {
            a.balance = '/';
            b.balance = '\\';
        } else {
            a.balance = '_';
            b.balance = '_';
        }

    }

    private void doubleRotateLeft(BNode<T> a) {
        BNode<T> b = a.right;
        BNode<T> c = b.left;
        if (a.parent == null) {
            this.root = c;
        } else if (a.isLeftNode()) {
            a.parent.left = c;
        } else {
            a.parent.right = c;
        }

        c.parent = a.parent;
        a.right = c.left;
        if (a.right != null) {
            a.right.parent = a;
        }

        b.left = c.right;
        if (b.left != null) {
            b.left.parent = b;
        }

        c.left = a;
        c.right = b;
        a.parent = c;
        b.parent = c;
        if (c.balance == '/') {
            a.balance = '_';
            b.balance = '\\';
        } else if (c.balance == '\\') {
            a.balance = '/';
            b.balance = '_';
        } else {
            a.balance = '_';
            b.balance = '_';
        }

        c.balance = '_';
    }

    private void doubleRotateRight(BNode<T> a) {
        BNode<T> b = a.left;
        BNode<T> c = b.right;
        if (a.parent == null) {
            this.root = c;
        } else if (a.isLeftNode()) {
            a.parent.left = c;
        } else {
            a.parent.right = c;
        }

        c.parent = a.parent;
        a.left = c.right;
        if (a.left != null) {
            a.left.parent = a;
        }

        b.right = c.left;
        if (b.right != null) {
            b.right.parent = b;
        }

        c.right = a;
        c.left = b;
        a.parent = c;
        b.parent = c;
        if (c.balance == '/') {
            b.balance = '_';
            a.balance = '\\';
        } else if (c.balance == '\\') {
            b.balance = '/';
            a.balance = '_';
        } else {
            b.balance = '_';
            a.balance = '_';
        }

        c.balance = '_';
    }
}
