package structures.nodes;

public class ListaSimpleNode {
    private int _ID = -1;
    private ListaSimpleNode _Next = null;
    private Object _Data = null;
    private boolean _State = false;
    private String Error = "error";

    public ListaSimpleNode(Object pData, int pID) {
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

    public ListaSimpleNode get_Next() {
        return this._Next;
    }

    public void set_Next(ListaSimpleNode pNode) {
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