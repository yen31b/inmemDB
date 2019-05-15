package structures.nodes;

public class BSTNode<T> {
    public T data;
    public BSTNode<T> left, right;
    private String Error = "error";

    public BSTNode(T data){
        this.data = data;
    }
}
