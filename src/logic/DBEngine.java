package logic;

import structures.lists.ListaSimple;

public class DBEngine {

    private static ListaSimple _schemes = new ListaSimple();
    private ListaSimple _tipos_de_datos = new ListaSimple();
    private String Error = "error";
    public DBEngine(){
        _tipos_de_datos.insertar("int");
        _tipos_de_datos.insertar("string");
        _tipos_de_datos.insertar("float");
        _tipos_de_datos.insertar("long");
        _tipos_de_datos.insertar("double");
        _tipos_de_datos.insertar("image");
        _tipos_de_datos.insertar("binary");
        _tipos_de_datos.insertar("video");
        _tipos_de_datos.insertar("join");
    }

    public static ListaSimple get_schemes() {
        return _schemes;
    }

    public void set_schemes(ListaSimple _schemes) {
        this._schemes = _schemes;
    }

    public ListaSimple get_tipos_de_datos() {
        return _tipos_de_datos;
    }

    public void set_tipos_de_datos(ListaSimple _tipos_de_datos) {
        this._tipos_de_datos = _tipos_de_datos;
    }
}
