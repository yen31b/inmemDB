package structures.lists;

import structures.nodes.ListaSimpleNode;

public class ListaSimple {
    private ListaSimpleNode _head = null;
    private int _largo = 0;

    public ListaSimple() {
    }

    public int get_size() {
        return this._largo;
    }

    private void set_size(int pVal) {
        this._largo += pVal;
    }

    public ListaSimpleNode insertar(Object pObject) {
        ListaSimpleNode retorno = null;
        if (this._head == null) {
            this._head = retorno = new ListaSimpleNode(pObject, this.get_size());
        } else {
            ListaSimpleNode tmp;
            for(tmp = this._head; tmp.get_Next() != null; tmp = tmp.get_Next()) {
            }

            retorno = new ListaSimpleNode(pObject, this.get_size());
            tmp.set_Next(retorno);
        }

        this.set_size(1);
        return retorno;
    }

    public ListaSimpleNode get_Node(int pIndex) {
        ListaSimpleNode returned = null;
        ListaSimpleNode tmp = this._head;
        int contador = 0;
        if (pIndex > this.get_size() - 1) {
            returned = null;
        } else {
            for(; contador <= pIndex && tmp != null; tmp = tmp.get_Next()) {
                if (tmp.get_ID() == pIndex && tmp.get_state()) {
                    returned = tmp;
                    contador = pIndex;
                } else {
                    ++contador;
                }
            }
        }

        return returned;
    }

    public Object get(int pIndex) {
        Object returned = null;
        ListaSimpleNode tmp = this._head;
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

    public void set(int pIndex, Object pObject) {
        ListaSimpleNode tmp = this._head;

        for(int contador = 0; contador <= pIndex; tmp = tmp.get_Next()) {
            if (contador == pIndex) {
                tmp.set_Data(pObject);
                break;
            }

            ++contador;
        }

    }

    public void delete(int pIndex) {
        ListaSimpleNode tmp = this._head;
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
