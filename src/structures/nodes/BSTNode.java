package structures.nodes;

public class BSTNode<T> {
    public T data;
    public BSTNode<T> left, right;

    public BSTNode(T data){
        this.data = data;
    }
}
