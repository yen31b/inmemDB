package structures.trees;

import structures.nodes.AVLNode;
import structures.nodes.ListaSimpleNode;

public class AVLTree<E extends Comparable<E>> extends Arbol<E> {
    private AVLNode<E> rootAbove = new AVLNode();

    public AVLTree() {
    }

    public void rotate(AVLNode<E> rotateBase, AVLNode<E> rootAbove) {
        int balance = rotateBase.getBalance();
        Math.abs(balance);
        AVLNode<E> child = balance < 0 ? rotateBase.getLeft() : rotateBase.getRight();
        if (child != null) {
            int childBalance = child.getBalance();
            AVLNode<E> grandChild = null;
            if (balance < -1 && childBalance < 0) {
                if (rootAbove != this.rootAbove && rootAbove.getRight() == rotateBase) {
                    rootAbove.setRightNode(child);
                } else {
                    rootAbove.setLeftNode(child);
                }

                grandChild = child.getRight();
                child.setRightNode(rotateBase);
                rotateBase.setLeftNode(grandChild);
            } else if (balance > 1 && childBalance > 0) {
                if (rootAbove != this.rootAbove && rootAbove.getRight() == rotateBase) {
                    rootAbove.setRightNode(child);
                } else {
                    rootAbove.setLeftNode(child);
                }

                grandChild = child.getLeft();
                child.setLeftNode(rotateBase);
                rotateBase.setRightNode(grandChild);
            } else if (balance < -1 && childBalance > 0) {
                grandChild = child.getRight();
                rotateBase.setLeftNode(grandChild);
                child.setRightNode(grandChild.getLeft());
                grandChild.setLeftNode(child);
                this.rotate(rotateBase, rootAbove);
            } else if (balance > 1 && childBalance < 0) {
                grandChild = child.getLeft();
                rotateBase.setRightNode(grandChild);
                child.setLeftNode(grandChild.getRight());
                grandChild.setRightNode(child);
                this.rotate(rotateBase, rootAbove);
            }
        }
    }

    public void insert(E element, ListaSimpleNode Node) {
        this.insert(element, this.rootAbove.getLeft(), Node);
    }

    private void insert(E element, AVLNode<E> temp, ListaSimpleNode Node) {
        if (this.rootAbove.getLeft() == null) {
            this.rootAbove.setLeftNode(new AVLNode(element, Node));
        } else {
            int compare = element.compareTo(temp.getElement());
            if (compare <= 0) {
                if (temp.getLeft() == null) {
                    temp.setLeft(element, Node);
                    return;
                }

                this.insert(element, temp.getLeft(), Node);
            } else {
                if (temp.getRight() == null) {
                    temp.setRight(element, Node);
                    return;
                }

                this.insert(element, temp.getRight(), Node);
            }

            if (temp == this.rootAbove.getLeft()) {
                this.rotate(this.rootAbove.getLeft(), this.rootAbove);
            }

            if (temp.getLeft() != null) {
                this.rotate(temp.getLeft(), temp);
            }

            if (temp.getRight() != null) {
                this.rotate(temp.getRight(), temp);
            }

        }
    }

    public void delete(E element) {
        this.remove(element, this.rootAbove);
    }

    private void remove(E element, AVLNode<E> temp) {
        if (temp != null) {
            int compare = 0;
            if (temp != this.rootAbove) {
                compare = element.compareTo(temp.getElement());
            }

            boolean direction = compare > 0 && temp != this.rootAbove;
            AVLNode<E> child = direction ? temp.getRight() : temp.getLeft();
            if (child != null) {
                if (temp == this.rootAbove && child.getBalance() == 0 && child.getElement().equals(element)) {
                    AVLNode<E> newRoot = child.getLeft();
                    if (newRoot == null) {
                        this.rootAbove.setLeftNode((AVLNode)null);
                    } else {
                        this.enactRemoval(temp, child, false);
                    }
                } else {
                    if (element.compareTo(child.getElement()) == 0) {
                        this.enactRemoval(temp, child, direction);
                    } else {
                        this.remove(element, child);
                    }

                }
            }
        }
    }

    private void enactRemoval(AVLNode<E> parent, AVLNode<E> remove, boolean direction) {
        AVLNode<E> temp = null;
        AVLNode<E> left = remove.getLeft();
        AVLNode<E> right = remove.getRight();
        if (left != null || right != null) {
            temp = this.findSuccessor(remove);
        }

        if (direction && parent != this.rootAbove) {
            parent.setRightNode(temp);
        } else {
            parent.setLeftNode(temp);
        }

        if (temp != null) {
            if (temp != left) {
                temp.setLeftNode(remove.getLeft());
            }

            if (temp != right) {
                temp.setRightNode(remove.getRight());
            }
        }

        remove.setLeftNode((AVLNode)null);
        remove.setRightNode((AVLNode)null);
    }

    private AVLNode<E> findSuccessor(AVLNode<E> root) {
        AVLNode<E> parent = null;
        boolean direction = root.getBalance() > 0;
        parent = root;
        AVLNode<E> temp = direction ? root.getRight() : root.getLeft();
        if (temp == null) {
            return temp;
        } else {
            while(temp.getRight() != null && !direction || temp.getLeft() != null && direction) {
                parent = temp;
                temp = direction ? temp.getLeft() : temp.getRight();
            }

            if (temp == parent.getLeft()) {
                parent.setLeftNode(temp.getRight());
                temp.setRightNode((AVLNode)null);
            } else {
                parent.setRightNode(temp.getLeft());
                temp.setLeftNode((AVLNode)null);
            }

            return temp;
        }
    }

    public ListaSimpleNode search(E element) {
        int balance;
        for(AVLNode temp = this.rootAbove.getLeft(); temp != null; temp = balance < 0 ? temp.getLeft() : temp.getRight()) {
            if (temp.getElement().equals(element)) {
                return temp.get_Node();
            }

            balance = element.compareTo(temp.getElement());
        }

        return null;
    }
}
