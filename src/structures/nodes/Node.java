package structures.nodes;

public class Node {
    private int _ID = -1;
    private Node _Next = null;
    private Object _Data = null;
    private boolean _State = false;

    public Node(Object pData, int pID) {
        this._Data = pData;
        this._ID = pID;
        this._State = true;
    }

    public Object get_Data() {
        return this._Data;
    }

    public void set_Data(Object pObject) {
        this._Data = pObject;
    }

    public Node get_Next() {
        return this._Next;
    }

    public void set_Next(Node pNode) {
        this._Next = pNode;
    }

    public int get_ID() {
        return this._ID;
    }

    public void delete() {
        this._State = false;
    }

    public boolean get_state() {
        return this._State;
    }
}