package structures.lists;


import structures.nodes.Node;

public class Array<T> {
    private Node _head = null;
    private int _largo = 0;
    private String Error = "error";
    public Array() {
    }

    public int get_size() {
        return this._largo;
    }

    private void set_size(int pVal) {
        this._largo += pVal;
    }

    public void insertar(T pObject) {
        if (this._head == null) {
            this._head = new Node(pObject, this.get_size());
        } else {
            Node tmp;
            for(tmp = this._head; tmp.get_Next() != null; tmp = tmp.get_Next()) {
            }

            tmp.set_Next(new Node(pObject, this.get_size()));
        }

        this.set_size(1);
    }

    public Object get(int pIndex) {
        Object returned = null;
        Node tmp = this._head;
        int contador = 0;
        if (pIndex > this.get_size() - 1) {
            returned = -1;
        } else {
            for(; contador <= pIndex && tmp != null; tmp = tmp.get_Next()) {
                if (tmp.get_ID() == pIndex && tmp.get_state()) {
                    returned = tmp.get_Data();
                    contador = pIndex;
                } else {
                    ++contador;
                }
            }
        }

        return returned;
    }

    public boolean set(int pIndex, T pObject) {
        Boolean returned = null;
        Node tmp = this._head;
        int contador = 0;
        if (pIndex > this.get_size() - 1) {
            returned = false;
        } else {
            while(contador <= pIndex) {
                if (tmp.get_ID() == pIndex) {
                    returned = true;
                    tmp.set_Data(pObject);
                    contador = pIndex;
                } else {
                    ++contador;
                }
            }
        }

        return returned;
    }

    public void delete(int pIndex) {
        Node tmp = this._head;
        int contador = 0;
        if (pIndex <= this.get_size() - 1) {
            for(; contador <= pIndex && tmp != null; tmp = tmp.get_Next()) {
                if (tmp.get_ID() == pIndex) {
                    tmp.delete();
                    contador = pIndex;
                } else {
                    ++contador;
                }
            }
        }

    }
}
