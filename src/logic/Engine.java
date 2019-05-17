package logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import structures.lists.ListaSimple;

import structures.nodes.ListaSimpleNode;
import structures.trees.Arbol;
import structures.trees.BinarySearchTree;
import structures.trees.ArbolB;
import structures.trees.ArbolRN;
import structures.trees.AVLTree;
import structures.trees.Splay;
import structures.trees.ArbolAA;

import data.Tools;
import gui.server.Main;

/**
 * Motor de la Base de Datos
 */

@SuppressWarnings("unchecked")
public class Engine {
    private String Error = "error";
    private ListaSimple _schemes = new ListaSimple();
    private ListaSimple _tipos_de_datos = new ListaSimple();

    /**
     * Constructor de la Clase
     */
    public Engine(){
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
    /**
     * Método que analiza el objeto JSON que se recibe, para proceder con las diferentes operaciones de la base de datos
     * @param pJSON Dato recibido del cliente
     * @return El estado final de la operacion solicitada por el cliente
     */
    public JSONObject analyze(JSONObject pJSON){
        long tmp_time1 = Tools.get_nanotime();
        JSONObject retorno = new JSONObject();
        if(pJSON.has("command")){
            if(pJSON.get("command").toString().replace(" ", "").equals("")){
                retorno.put("status", "error");
                retorno.put("code", "The command can not be empty");
            }else{
                String comando = pJSON.get("command").toString();
                if(comando.equals("create_scheme")){
                    if(pJSON.has("name") && pJSON.has("location") && pJSON.has("shared_secret") &&
                            pJSON.has("columns")){
                        retorno = create_scheme(pJSON);
                    }else{
                        retorno.put("status", "error");
                        retorno.put("code", "The command __create_scheme__ does not has the all necessary arguments.");
                    }
                }else if(comando.equals("delete_scheme")){
                    if(pJSON.has("name") && pJSON.has("location") && pJSON.has("shared_secret")){
                        retorno = delete_scheme(pJSON);
                    }else{
                        retorno.put("status", "error");
                        retorno.put("code", "The command __delete_scheme__ does not has the all necessary arguments.");
                    }
                }else if(comando.equals("get_schemes")){
                    retorno = get_schemes_JSON();
                }else if(comando.equals("insert")){
                    if(pJSON.has("scheme") && pJSON.has("location") && pJSON.has("shared_secret") && pJSON.has("values")){
                        retorno = insert(pJSON);
                    }else{
                        retorno.put("status","error");
                        retorno.put("code", "The command __insert__ does not has the all necessary arguments.");
                    }
                }else if(comando.equals("make_index")){
                    if(pJSON.has("scheme") && pJSON.has("location") && pJSON.has("shared_secret") && pJSON.has("arguments")){
                        retorno = make_index(pJSON);
                    }else{
                        retorno.put("status","error");
                        retorno.put("code", "The command __make_index__ does not has the all necessary arguments.");
                    }
                }else if(comando.equals("delete_index")){
                    if(pJSON.has("scheme") && pJSON.has("location") && pJSON.has("shared_secret") && pJSON.has("arguments")){
                        retorno = delete_index(pJSON);
                    }else{
                        retorno.put("status","error");
                        retorno.put("code", "The command __delete_index__ does not has the all necessary arguments.");
                    }
                }else if(comando.equals("search")){
                    if(pJSON.has("scheme") && pJSON.has("location") && pJSON.has("shared_secret") && pJSON.has("arguments")){
                        retorno = search(pJSON);
                    }else{
                        retorno.put("status","error");
                        retorno.put("code", "The command __search__ does not has the all necessary arguments.");
                    }
                }else{
                    retorno.put("status", "error");
                    retorno.put("code", "The command _"+comando+"_ is not defined");
                }
            }

        }else{
            retorno.put("status", "error");
            retorno.put("code", "The __command__ key is not defined.");
        }
        return retorno.put("time", Tools.get_difference_nanotime(tmp_time1));
    }

    /**
     * Verifica si existe un indice sobre una columna en un determinado esquema
     * @param pScheme Esquema a buscar
     * @param pShared_secret Shared Secret del esquema a donde se realizará la búsqueda
     * @param pColumn Columna que se desea buscar
     * @param pTree Tipo de arbol que se tiene
     * @return Si se encontró el indice ó no
     */
    private boolean column_has_index(String pScheme,String pLocation,String pShared_secret,String pColumn,String pTree){
        boolean retorno =false;
        for(int i = 0;i<_schemes.get_size();i++){
            ListaSimple scheme = (ListaSimple) _schemes.get(i);
            if(scheme!=null){
                if(scheme.get(0).equals(pScheme)&& scheme.get(1).equals(pLocation) && scheme.get(2).equals(pShared_secret)){
                    ListaSimple indices = (ListaSimple) scheme.get(4);
                    for(int j=0;j<indices.get_size();j++){
                        ListaSimple indice = (ListaSimple) indices.get(j);
                        if(indice.get(0).equals(pColumn) && indice.get(1).equals(pTree)){
                            retorno = true;
                            break;
                        }
                    }
                }
            }
        }
        return retorno;
    }
    /**
     * Obtiene el dato de un campo al hacer un join
     * @param pScheme Esquema a buscar
     * @param pColumnRelacionada Columna donde se busca
     * @param pColumnDato Columna donde se tiene el dato
     * @param pPattern Patron a buscar
     * @return El texto del dato
     */
    private String search_auxiliar(String pScheme,String pColumnRelacionada,String pColumnDato,String pPattern){
        String retorno = null;
        for(int i = 0;i<_schemes.get_size();i++){
            ListaSimple scheme = (ListaSimple) _schemes.get(i);
            if(scheme!=null){
                if(scheme.get(0).equals(pScheme)){
                    ListaSimple Columnas = (ListaSimple) scheme.get(3);
                    ListaSimple Datos = (ListaSimple) scheme.get(5);
                    int indice_columna_relacionada = get_indice_columna(Columnas,pColumnRelacionada);
                    int indice_columna_dato = get_indice_columna(Columnas,pColumnDato);
                    for(int j=0;j<Datos.get_size();j++){
                        ListaSimple Dato = (ListaSimple) Datos.get(j);
                        String tmp_dato_columna_relacionada = (String) Dato.get(indice_columna_relacionada);
                        if(tmp_dato_columna_relacionada.equals(pPattern)){
                            retorno = (String) Dato.get(indice_columna_dato);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return retorno;
    }
    /**
     * Realiza una busqueda en la base de datos del servidor
     * @param pJSON JSON puro del cliente con los parámetros
     * @return Resultados de la búsqueda
     */
    private JSONObject search(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String scheme_name = pJSON.getString("scheme");
        String scheme_location = pJSON.getString("location");
        String scheme_shared_secret = pJSON.getString("shared_secret");
        if(!exists_scheme(scheme_name,scheme_location)){
            retorno.put("status","error");
            retorno.put("code", "The scheme's name given not exists");
        }else{
            for(int i = 0;i<get_schemes().get_size();i++){
                ListaSimple scheme = (ListaSimple) get_schemes().get(i);
                if(scheme!=null){
                    if(scheme.get(0).equals(scheme_name) && scheme.get(1).equals(scheme_location)){
                        if(scheme.get(2).equals(scheme_shared_secret)){
                            ListaSimple Indices = (ListaSimple) scheme.get(4);
                            ListaSimple Datos = (ListaSimple) scheme.get(5);
                            JSONObject argumentos = pJSON.getJSONObject("arguments");
                            if(argumentos.has("column") && argumentos.has("pattern") && argumentos.has("index")){
                                String argument_column = argumentos.getString("column");
                                String argument_pattern = argumentos.getString("pattern");
                                JSONObject index = argumentos.getJSONObject("index");
                                if(!scheme_has_column(scheme_name,scheme_location,scheme_shared_secret,argument_column)){
                                    retorno.put("status","error");
                                    retorno.put("code", "There is not a column called " + argument_column +" in the scheme selected");
                                }else{
                                    if(!index.has("tree")){
                                        retorno.put("status","error");
                                        retorno.put("code", "The command __search__ does not has the all necessary arguments.");
                                    }else{
                                        String argument_tree = index.getString("tree");
                                        argument_tree="null";
                                        if(argument_tree.equals("null")){//Búsqueda lineal
                                            JSONArray resultados = new JSONArray();
                                            ListaSimple Columnas = (ListaSimple) scheme.get(3);
                                            if(Datos.get_size()!=0){
                                                for(int j = 0;j<Datos.get_size();j++){
                                                    ListaSimple Dato = (ListaSimple) Datos.get(j);
                                                    String valor_dato = (String) Dato.get(get_indice_columna(Columnas,argument_column));
                                                    if(valor_dato.equals(argument_pattern)){
                                                        JSONObject tmp = new JSONObject();
                                                        for(int k = 0;k<Columnas.get_size();k++){
                                                            ListaSimple columna = (ListaSimple) Columnas.get(k);
                                                            String tmp_nombre_columna = (String) columna.get(0);
                                                            String tmp_tipo_columna = (String) columna.get(1);
                                                            String dato_final = (String) Dato.get(k);
                                                            if(tmp_tipo_columna.equals("join")){
                                                                ListaSimple join_data = (ListaSimple) columna.get(3);
                                                                String tmp_schema_to_join = (String) join_data.get(0);
                                                                String tmp_columna_relacionada = (String) join_data.get(1);
                                                                String tmp_columna_dato = (String) join_data.get(2);
                                                                tmp.put(tmp_nombre_columna, search_auxiliar(tmp_schema_to_join,
                                                                        tmp_columna_relacionada,tmp_columna_dato,dato_final));
                                                            }else{
                                                                tmp.put(tmp_nombre_columna,dato_final );
                                                            }
                                                        }
                                                        resultados.put(tmp);
                                                    }
                                                }
                                            }
                                            retorno.put("results", resultados);
                                        }else{//Búsqueda por indices
                                            if(Indices.get_size()==0){
                                                retorno.put("status","error");
                                                retorno.put("code", "There is not index defined on the scheme given ");
                                            }else{//Se busca por Indices
                                                if(!column_has_index(scheme_name,scheme_location,
                                                        scheme_shared_secret,argument_column,argument_tree)){
                                                    retorno.put("status","error");
                                                    retorno.put("code", "There is not index defined on the column "+
                                                            argument_column + " of tree type "+argument_tree);
                                                }else{
                                                    JSONArray resultados = new JSONArray();
                                                    if(Datos.get_size()!=0){

                                                    }
                                                    retorno.put("results", resultados);
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                retorno.put("status","error");
                                retorno.put("code", "The command search does not has the all necessary arguments.");
                            }
                        }else{
                            retorno.put("status","error");
                            retorno.put("code", "Unauthorized access to search in the scheme");
                        }
                        break;
                    }

                }
            }
        }
        return retorno;
    }
    /**
     * Elimina un indice en un esquema especifico
     * @param pJSON
     * @return
     */
    private JSONObject delete_index(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String scheme_name = pJSON.getString("scheme");
        String scheme_location = pJSON.getString("location");
        String scheme_shared_secret = pJSON.getString("shared_secret");
        if(!exists_scheme(scheme_name,scheme_location)){
            retorno.put("status","error");
            retorno.put("code", "The scheme's name given not exists");
        }else{
            for(int i = 0;i<get_schemes().get_size();i++){
                ListaSimple scheme = (ListaSimple) get_schemes().get(i);
                if(scheme.get(0).equals(scheme_name) && scheme.get(1).equals(scheme_location)){
                    if(scheme.get(2).equals(scheme_shared_secret)){
                        ListaSimple indices = (ListaSimple) scheme.get(4);
                        if(indices.get_size()==0){
                            retorno.put("status","error");
                            retorno.put("code", "There is not defined index on this scheme");
                        }else{
                            JSONObject argumentos = pJSON.getJSONObject("arguments");
                            if(!(argumentos.has("column") && argumentos.has("tree"))){
                                retorno.put("status","error");
                                retorno.put("code", "The command delete_index does not has the all necessary arguments.");
                            }else{
                                String argument_column = argumentos.getString("column");
                                String argument_tree = argumentos.getString("tree");
                                if(!scheme_has_column(scheme_name,scheme_location,scheme_shared_secret,argument_column)){
                                    retorno.put("status","error");
                                    retorno.put("code", "There is not a column called " + argument_column +" in the scheme selected");
                                }else{
                                    boolean exists = false;
                                    for(int j = 0;j<indices.get_size();j++){
                                        ListaSimple indice = (ListaSimple) indices.get(j);
                                        String tmp_name = (String) indice.get(0);
                                        String tmp_tree = (String) indice.get(1);
                                        if(tmp_name.equals(argument_column) && tmp_tree.equals(argument_tree)){
                                            exists=true;
                                            indices.delete(j);
                                            break;
                                        }
                                    }
                                    if(exists){
                                        retorno.put("status","done");
                                    }else{
                                        retorno.put("status","error");
                                        retorno.put("code", "There is not an index "+argument_tree+" defined over a given column ("+argument_column+")");
                                    }
                                }
                            }
                        }
                    }else{
                        retorno.put("status","error");
                        retorno.put("code", "Unauthorized access to delete in the scheme");
                    }
                    break;
                }
            }
        }
        return retorno;
    }
    /**
     * Retorna el tipo de columna (int,float, string,...,join)
     * @param pScheme Esquema al que pertenece la columna
     * @param pLocation Ubicación del esquema
     * @param pShared_secret Shared Secret del esquema
     * @param pColumn Columna del esquema del que se deseea saber el tipo de dato
     * @return El tipo de columna
     */
    private String type_of_column(String pScheme, String pLocation, String pShared_secret, String pColumn){
        String retorno = null;
        for(int i = 0;i<_schemes.get_size();i++){
            ListaSimple scheme = (ListaSimple) _schemes.get(i);
            if(scheme.get(0).equals(pScheme) && scheme.get(1).equals(pLocation) &&
                    scheme.get(2).equals(pShared_secret)){
                ListaSimple columnas = (ListaSimple) scheme.get(3);
                for(int j = 0; j<columnas.get_size();j++){
                    ListaSimple columna = (ListaSimple) columnas.get(j);
                    if(columna.get(0).equals(pColumn)){
                        retorno = (String) columna.get(1);
                        break;
                    }
                }
                break;
            }
        }
        return retorno;
    }
    /**
     * Genera el índice en memoria (Todas las validaciones ya están hechas)
     * @param argument_tree Tipo de arbol que será el indice
     * @param argument_column Columna del esquema sobre el cual se establece el indice
     * @param Columnas Columnas del esquema (Referencia)
     * @param Datos Datos del esquqma (Referencia)
     * @param Indices Indices del esquema (Referencia)
     */
    private void make_index_auxiliar(String argument_tree,String argument_column,ListaSimple Columnas,ListaSimple Datos,ListaSimple Indices){
        Arbol<String> tree = null;
        if(argument_tree.toUpperCase().equals("B")){
            tree = new ArbolB<String>();
        }else if(argument_tree.toUpperCase().equals("B+")){
            tree = new ArbolB<String>();
        }else if(argument_tree.toUpperCase().equals("ABB")){
            tree = new BinarySearchTree<String>();
        }else if(argument_tree.toUpperCase().equals("RN")){
            tree = new ArbolRN<String>();
        }else if(argument_tree.toUpperCase().equals("AVL")){
            tree = new AVLTree<String>();
        }else if(argument_tree.toUpperCase().equals("SPLAY")){
            tree = new Splay<String>();
        }else if(argument_tree.toUpperCase().equals("AA")){
            tree = new ArbolAA<String>();
        }else{
            tree = new BinarySearchTree<String>();
        }
        if(Datos.get_size()!=0){//Generar indice insertando los datos existentes en el árbol
            int indice_columna = 0;
            for(int j = 0;j<Columnas.get_size();j++){
                ListaSimple columna = (ListaSimple) Columnas.get(j);
                if(columna.get(0).equals(argument_column)){
                    indice_columna=j;
                    break;
                }
            }
            for(int j = 0;j<Datos.get_size();j++){
                ListaSimpleNode NodoDato = Datos.get_Node(j);
                ListaSimple dato = (ListaSimple) NodoDato.get_Data();
                String string_dato = (String) dato.get(indice_columna);
                tree.insert(string_dato, NodoDato);
            }

        }
        ListaSimple Indice = new ListaSimple();
        Indice.insertar(argument_column);//[0] Nombre de la columna del Indice
        Indice.insertar(argument_tree); //[1] Tipo de arbol del Indice
        Indice.insertar(tree);// [2] Arbol en sí donde se almacenan los datos
        Indices.insertar(Indice);
    }
    /**
     * Crea un indice sobre una columna en un esquema dado.
     * @param pJSON JSON puro que se recibe del usuario
     * @return Estado final de la creacion del indice (Éxitosa ó no, y sus posibles codigos de error)
     */
    private JSONObject make_index(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String scheme_name = pJSON.getString("scheme");
        String scheme_location = pJSON.getString("location");
        String scheme_shared_secret = pJSON.getString("shared_secret");
        if(!exists_scheme(scheme_name,scheme_location)){
            retorno.put("status","error");
            retorno.put("code", "The scheme's name given not exists");
        }else{
            for(int i = 0;i<get_schemes().get_size();i++){
                ListaSimple scheme = (ListaSimple) get_schemes().get(i);
                if(scheme.get(0).equals(scheme_name) && scheme.get(1).equals(scheme_location)){
                    if(scheme.get(2).equals(scheme_shared_secret)){
                        JSONObject argumentos = pJSON.getJSONObject("arguments");
                        String argument_column = argumentos.getString("column");
                        String argument_tree = argumentos.getString("tree");
                        String argument_type_column = type_of_column(scheme_name, scheme_location, scheme_shared_secret,argument_column);
                        if(!this.scheme_has_column(scheme_name,scheme_location,
                                scheme_shared_secret, argument_column)){
                            retorno.put("status","error");
                            retorno.put("code", "There is not a column called " + argument_column);

                        }else if(argument_type_column.equals("image") ||
                                argument_type_column.equals("binary") ||
                                argument_type_column.equals("video") ||
                                argument_type_column.equals("join")){
                            retorno.put("status","error");
                            retorno.put("code", "Is not posible to make a index over a column defined as "+argument_type_column);
                        }else{
                            ListaSimple Columnas = (ListaSimple) scheme.get(3);
                            ListaSimple Indices = (ListaSimple) scheme.get(4);
                            ListaSimple Datos = (ListaSimple) scheme.get(5);
                            if(Indices.get_size()==0){
                                this.make_index_auxiliar(argument_tree, argument_column, Columnas, Datos,Indices);
                                retorno.put("status","done");
                            }else{
                                boolean exists = false;
                                for(int j = 0;j<Indices.get_size();j++){
                                    ListaSimple Indice = (ListaSimple) Indices.get(j);
                                    if(Indice.get(0).equals(argument_column) &&
                                            Indice.get(1).equals(argument_tree.toUpperCase())){//Se verifica si ya existe un indice definido sobre una columna con el mismo tipo de arbol
                                        exists = true;
                                        retorno.put("status","error");
                                        retorno.put("code", "The column already has a index with the same tree type");
                                        break;
                                    }
                                }
                                if(!exists){
                                    this.make_index_auxiliar(argument_tree, argument_column, Columnas, Datos,Indices);
                                    retorno.put("status","done");
                                }
                            }
                        }
                    }else{
                        retorno.put("status","error");
                        retorno.put("code", "Unauthorized access to insert in the scheme");
                    }
                    break;
                }
            }
        }
        return retorno;
    }
    /**
     * Retorna el indice de una columna especifica
     * @param pColumnas Lista de columnas de un esquema
     * @param pColumna Nombre de la columna a buscar
     * @return El indice de la columna (Un entero positivo o igual a 0, si no se encuentra la columna un -1)
     */
    private int get_indice_columna(ListaSimple pColumnas, String pColumna){
        int retorno = -1;
        for(int i = 0;i<pColumnas.get_size();i++){
            ListaSimple columna = (ListaSimple) pColumnas.get(i);
            String nombre_columna = (String) columna.get(0);
            if(nombre_columna.equals(pColumna)){
                retorno = i;
                break;
            }
        }
        return retorno;
    }
    /**
     * Inserta una nueva fila en un esquema determinado
     * @param pJSON JSON puro que se recibe del usuario
     * @return Estado final de la inserción (Éxitosa ó no, y sus posibles codigos de error)
     */
    private JSONObject insert(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String scheme_name = pJSON.getString("scheme");
        String scheme_location = pJSON.getString("location");
        String scheme_shared_secret = pJSON.getString("shared_secret");
        JSONObject values = pJSON.getJSONObject("values");
        if(!exists_scheme(scheme_name,scheme_location)){
            retorno.put("status","error");
            retorno.put("code", "The scheme's name given not exists");
        }else{
            for(int i = 0;i<get_schemes().get_size();i++){
                ListaSimple scheme = (ListaSimple) get_schemes().get(i);
                if(scheme!=null){
                    boolean tmp_state = true;
                    if(scheme.get(0).equals(scheme_name) && scheme.get(1).equals(scheme_location)){
                        if(scheme.get(2).equals(scheme_shared_secret)){
                            ListaSimple columnas = (ListaSimple) scheme.get(3);
                            ListaSimple indices = (ListaSimple) scheme.get(4);
                            ListaSimple a_insertar = new ListaSimple();
                            for(int j = 0;j<columnas.get_size();j++){
                                ListaSimple columna = (ListaSimple) columnas.get(j);
                                String nombre_columna = (String) columna.get(0);
                                //String tipo_columna = (String) columna.get(1);
                                String largo_columna = (String) columna.get(2);
                                if(values.has(nombre_columna)){
                                    String valor_tmp = (String) values.get(nombre_columna);
                                    if(valor_tmp.length()<=Integer.parseInt(largo_columna)){
                                        a_insertar.insertar(valor_tmp);//Guardo la referencia del Nodo que se inserta en la lista
                                    }else{
                                        tmp_state = false;
                                        retorno.put("status","error");
                                        retorno.put("code", "The data value of column '"+
                                                nombre_columna+"' is greater than the value defined ("+
                                                largo_columna +") in the schema on the column");
                                        break;
                                    }
                                }else{
                                    a_insertar.insertar(null);
                                }
                            }
                            if(tmp_state){//Si paso todas las verificaciones se insertan los datos y se actualizan los indices
                                ListaSimple datos = (ListaSimple) scheme.get(5);
                                ListaSimpleNode Nodo = datos.insertar(a_insertar);
                                for(int j = 0;j<indices.get_size();j++){
                                    ListaSimple indice = (ListaSimple) indices.get(j);
                                    String tmp_columna = (String) indice.get(0);
                                    String tmp_dato = (String) a_insertar.get(get_indice_columna(columnas,tmp_columna));
                                    Arbol<String> tree = (Arbol<String>) indice.get(2);
                                    tree.insert(tmp_dato, Nodo);
                                }
                                retorno.put("status","done");
                            }
                        }else{
                            retorno.put("status","error");
                            retorno.put("code", "Unauthorized access to insert in the scheme");
                        }
                        break;
                    }
                }
            }
        }
        return retorno;
    }

    /**
     * Retorna en formato JSON los esquemas almacenados en la BD (nombre, location, columns[nombre,tipo,largo],indices[columna,tipo])
     * @return JSON Shemes
     */
    private JSONObject get_schemes_JSON(){
        JSONObject retorno = new JSONObject();
        JSONArray lista_esquemas = new JSONArray();
        for(int i = 0; i<get_schemes().get_size();i++){
            ListaSimple scheme = (ListaSimple) get_schemes().get(i);
            if(scheme!=null){
                JSONObject tmp_ = new JSONObject();
                tmp_.put("name", scheme.get(0).toString());
                tmp_.put("location", scheme.get(1).toString());
                JSONArray columnas = new JSONArray();
                ListaSimple columns = (ListaSimple) scheme.get(3);
                for(int j = 0; j<columns.get_size();j++){
                    ListaSimple column = (ListaSimple) columns.get(j);
                    JSONObject tmp = new JSONObject();
                    String col_name = (String) column.get(0);
                    String col_type = (String) column.get(1);
                    String col_length = (String) column.get(2);
                    tmp.put("name", col_name);
                    tmp.put("type", col_type);
                    tmp.put("length", col_length);
                    columnas.put(tmp);
                }
                JSONArray indices = new JSONArray();
                ListaSimple indexes = (ListaSimple) scheme.get(4);
                for(int j = 0;j<indexes.get_size();j++){
                    ListaSimple index = (ListaSimple) indexes.get(j);
                    if(index!=null){
                        JSONObject tmp = new JSONObject();
                        String index_column = (String) index.get(0);
                        String index_tree = (String) index.get(1);
                        tmp.put("column", index_column);
                        tmp.put("tree", index_tree);
                        indices.put(tmp);
                    }
                }
                tmp_.put("columns", columnas);
                tmp_.put("index", indices);
                lista_esquemas.put(tmp_);
            }
        }
        retorno.put("status", "done");
        retorno.put("schemes", lista_esquemas);
        return retorno;
    }
    /**
     * Elimina un esquema de la base de datos
     * @param pJSON JSON puro que se recibe del usuario
     * @return Estado final de la eliminación (Éxitosa ó no, y sus posibles codigos de error)
     */
    private JSONObject delete_scheme(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String name = pJSON.get("name").toString();
        String location = pJSON.get("location").toString();
        String shared_secret = pJSON.get("shared_secret").toString();
        if(!exists_scheme(name,location)){
            retorno.put("status", "error");
            retorno.put("code", "There is not schemes to delete with the name given");
        }else{
            for(int i = 0; i<get_schemes().get_size();i++){
                ListaSimple scheme = (ListaSimple) _schemes.get(i);
                if(scheme.get(0).equals(name) && scheme.get(1).equals(location)){
                    if(scheme.get(2).equals(shared_secret)){
                        _schemes.delete(i);
                        retorno.put("status", "done");
                    }else{
                        retorno.put("status", "error");
                        retorno.put("code", "Shared secret incorrect");
                    }
                    break;
                }
            }
        }
        return retorno;
    }
    /**
     * Crea un nuevo esquema
     * @param pJSON JSON puro que se recibe del usuario
     * @return Estado final de la cracion (Éxitosa ó no, y sus posibles codigos de error)
     */
    private JSONObject create_scheme(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String name = pJSON.get("name").toString();
        String location = pJSON.get("location").toString();
        String shared_secret = pJSON.get("shared_secret").toString();
        JSONArray columns = new JSONArray(pJSON.get("columns").toString());
        if(name.replace(" ", "").equals("")){
            retorno.put("status", "error");
            retorno.put("code", "The name can not be empty");
        }else if(location.replace(" ", "").equals("")){
            retorno.put("status", "error");
            retorno.put("code", "The location can not be empty");
        }else if(exists_scheme(name,location)){
            retorno.put("status", "error");
            retorno.put("code", "The scheme already exists in the location given");
        }else if(shared_secret.replace(" ","").equals("")){
            retorno.put("status", "error");
            retorno.put("code", "The shared_secret can not be empty");
        }else if(columns.length()==0){
            retorno.put("status", "error");
            retorno.put("code", "The scheme should have at least one column");
        }else{
            boolean state = true;
            ListaSimple tmp = new ListaSimple();
            ListaSimple tmp2 = new ListaSimple();
            ListaSimple columns_names = new ListaSimple();
            for(int i = 0;i<columns.length();i++){
                JSONObject tmp3 = (JSONObject) columns.get(i);
                ListaSimple tmp4 = new ListaSimple();
                String column_name = tmp3.get("name").toString();
                String column_type = tmp3.get("type").toString();
                String column_length = tmp3.get("length").toString();
                if(column_name.replace(" ","").equals("")){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The name of any column can not be empty");
                }else if(exists_in_list(column_name,columns_names)){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "Can not be two or more columns with the same name");
                }else if(column_type.replace(" ","").equals("")){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The type's name of any column can not be empty");
                }else if(!exists_in_list(column_type.toLowerCase(),_tipos_de_datos)){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The type of any column can not be "+column_type);
                }else if(column_type.equals("join") && _schemes.get_size()==0){
                    state=false;
                    retorno.put("status", "error");//OK
                    retorno.put("code", "There is not another schemes to join");
                }else if(column_type.equals("join") && !tmp3.has("join")){///
                    state=false;
                    retorno.put("status", "error");//OK
                    retorno.put("code", "No exists information for make a join");
                }else if(tmp3.has("join") && !is_correct_join(tmp3.get("join").toString())){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The join information given is not correct");
                }else if(!Tools.isInteger(column_length)){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The column's length must be a number");
                }else if(Integer.parseInt(column_length)<=0){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The column's length must be higher than 0");
                }else{
                    tmp4.insertar(column_name);//[0] Nombre de la Columna
                    tmp4.insertar(column_type.toLowerCase()); //[1] Tipo de Columna
                    tmp4.insertar(column_length); //[2] Largo de los datos de la columna
                    if(tmp3.has("join")){
                        ListaSimple tmp5 = new ListaSimple();
                        JSONObject tmp6 = new JSONObject(tmp3.get("join").toString());
                        tmp5.insertar(tmp6.get("scheme").toString());
                        tmp5.insertar(tmp6.get("busqueda").toString());
                        tmp5.insertar(tmp6.get("dato").toString());
                        tmp4.insertar(tmp5);
                    }
                    tmp2.insertar(tmp4);
                    columns_names.insertar(column_name);
                }
                if(!state){
                    break;
                }
            }
            if(state){
                tmp.insertar(name);//[0]
                tmp.insertar(location);//[1]
                tmp.insertar(shared_secret);//[2]
                tmp.insertar(tmp2);//Columnas[3]
                tmp.insertar(new ListaSimple());//Índices[4]
                tmp.insertar(new ListaSimple());//Datos[5]
                _schemes.insertar(tmp);
                retorno.put("status", "done");
                Tools.beep();
            }
        }
        return retorno;
    }
    /**
     * Verifica si existe una columna en un esquema
     * @param pScheme Esquema a buscar
     * @param pShared_secret Shared Secret del esquema a donde se realizará la bússqueda
     * @param pColumn Columna que se desea buscar
     * @return Si se encontró la columna ó no
     */
    private boolean scheme_has_column(String pScheme,String pLocation,String pShared_secret,String pColumn){
        boolean retorno =false;
        for(int i = 0;i<_schemes.get_size();i++){
            ListaSimple scheme = (ListaSimple) _schemes.get(i);
            if(scheme!=null){
                if(scheme.get(0).equals(pScheme)&& scheme.get(1).equals(pLocation) && scheme.get(2).equals(pShared_secret)){
                    ListaSimple columnas = (ListaSimple) scheme.get(3);
                    for(int j=0;j<columnas.get_size();j++){
                        ListaSimple columna = (ListaSimple) columnas.get(j);
                        if(columna.get(0).equals(pColumn)){
                            retorno = true;
                            break;
                        }
                    }
                }
            }
        }
        return retorno;
    }
    /**
     * Valida si la informacion de un join es correcta
     * @param pJSON Informacion del join a validar
     * @return true or false
     */
    private boolean is_correct_join(String pJSON){
        boolean retorno = false;
        try{
            JSONObject json = new JSONObject(pJSON);
            if(!(json.has("scheme") &&
                    json.has("busqueda") &&
                    json.has("dato") &&
                    json.has("location") &&
                    json.has("shared_secret"))){
                retorno = false;
            }else if(!exists_scheme(json.get("scheme").toString(),json.get("location").toString())){
                retorno = false;
            }else if(!scheme_has_column(json.get("scheme").toString(),
                    json.get("location").toString(),
                    json.get("shared_secret").toString(),
                    json.get("busqueda").toString())){
                retorno = false;
            }else if(!scheme_has_column(json.get("scheme").toString(),
                    json.get("location").toString(),
                    json.get("shared_secret").toString(),
                    json.get("dato").toString())){
                retorno = false;
            }else{
                retorno = true;
            }
        }catch(JSONException e){
            retorno = false;
            Tools.write_("5");
        }

        return retorno;
    }
    /**
     * Valida si un texto está en una lista
     * @param pKey Valor a buscar
     * @param pList Lista en donde buscar
     * @return True or False
     */
    private boolean exists_in_list(String pKey, ListaSimple pList){
        boolean retorno = false;
        for(int i=0;i<pList.get_size();i++){
            String tmp = (String) pList.get(i);
            if(tmp.equals(pKey)){
                retorno = true;
                break;
            }
        }
        return retorno;
    }
    /**
     * Método que verifica si existe un esquema en la base de datos.
     * @param pName Nombre del esquema
     * @return Valor booleano si existe o no.
     */
    private boolean exists_scheme(String pName, String pLocation){
        boolean retorno = false;
        for(int i = 0;i<_schemes.get_size();i++){
            ListaSimple scheme = (ListaSimple) _schemes.get(i);
            if(scheme!=null){
                if(scheme.get(0).equals(pName) && scheme.get(1).equals(pLocation)){
                    retorno=true;
                    break;
                }
            }
        }
        return retorno;
    }
    /**
     *
     * @return Los esquemas que existen en la base de datos
     */
    public ListaSimple get_schemes(){
        return this._schemes;
    }
}
