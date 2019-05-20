package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * API del server para utilizar en la aplicacion grafica
 *
 */
public class InmemDB {
    private Socket socket=null;
    private PrintWriter out;
    private BufferedReader in;
    private String _protocol = null;

    /**
     * Constructor del API
     * @param pIP IP del servidor
     * @param pPuerto Puerto del servidor
     * @param pProtocol Protocolo de Comunicacion (XML o JSON)
     */
    public InmemDB(String pIP, int pPuerto, String pProtocol){
        try {
            socket = new Socket(pIP,pPuerto);
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            _protocol = pProtocol;
            send("content-type:"+pProtocol);
            System.out.println(read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Crea un nuevo esquema en la base de datos
     * @param pNombre Nombre del esquema
     * @param pUbicacion Ubicación del esquema
     * @param pShared_Secret Clave del esquema
     * @param columnas Columnas (JSONArray) Ej. [{"name":"id","type":"int","length":100},{"name":"file","type":"binary","length":5000}]
     * @return
     */
    public String create_scheme(String pNombre, String pUbicacion,String pShared_Secret,JSONArray columnas){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "create_scheme");
        tmp.put("name", pNombre);
        tmp.put("location", pUbicacion);
        tmp.put("shared_secret", pShared_Secret);
        tmp.put("columns", columnas);
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }
    /**
     * Borra un esquema en el servidor
     * @param pNombre Nombre del esquema
     * @param pUbicacion Ubicacion del esquema
     * @param pShared_Secret Clave del esquema
     * @return
     */
    public String delete_scheme(String pNombre, String pUbicacion,String pShared_Secret){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "delete_scheme");
        tmp.put("name", pNombre);
        tmp.put("location", pUbicacion);
        tmp.put("shared_secret", pShared_Secret);
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }
    /**
     * Consulta los esquemas (y su estructura) al servidor
     * @return
     */
    public String get_schemes(){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "get_schemes");
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }
    /**
     * Cambia el protocolo de comunicacion
     * @param pProtocol XML || JSON
     */
    public void set_content_type(String pProtocol){
        _protocol = pProtocol;
    }
    /**
     * Inserta en un esquema de la base de datos
     * @param pNombre Nombre del esquema
     * @param pUbicacion Ubicación del esquema
     * @param pShared_Secret Clave del esquema
     * @param values (JSONObject) columnas Ej. {"id":"1","nombre":"Pepe","apellido":"Rojas"}
     * @return
     */
    public String insert(String pNombre, String pUbicacion,String pShared_Secret,JSONObject values){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "insert");
        tmp.put("scheme", pNombre);
        tmp.put("location", pUbicacion);
        tmp.put("shared_secret", pShared_Secret);
        tmp.put("values", values);
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }
    /**
     * Crea un indice de un esquema
     * @param pNombre Nombre del esquema
     * @param pUbicacion Ubicación del esquema
     * @param pShared_Secret Clave del esquema
     * @param arguments (JSONObject) columnas Ej. {"column":"nombre","tree":"ABB"}
     * column: nombre de la columna a la cual se le está haciendo el indice
     * tree: tipo de arbol del indice
     * @return String que indica si fue creado o no el indice
     */
    public String make_index(String pNombre, String pUbicacion,String pShared_Secret,JSONObject arguments){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "make_index");
        tmp.put("name", pNombre);
        tmp.put("location", pUbicacion);
        tmp.put("shared_secret", pShared_Secret);
        tmp.put("arguments", arguments);
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }
    /**
     * Borra un indice de un esquema
     * @param pNombre Nombre del esquema
     * @param pUbicacion Ubicación del esquema
     * @param pShared_Secret Clave del esquema
     * @param arguments (JSONObject) columnas Ej. {"column":"nombre","tree":"ABB"}
     * column: nombre de la columna a la cual se le está eliminando el indice
     * tree: tipo de arbol del indice
     * @return
     */
    public String delete_index(String pNombre, String pUbicacion,String pShared_Secret,JSONObject arguments){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "delete_index");
        tmp.put("name", pNombre);
        tmp.put("location", pUbicacion);
        tmp.put("shared_secret", pShared_Secret);
        tmp.put("arguments", arguments);
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }

    /**
     * Busca en un esquema
     * @param pNombre Nombre del esquema
     * @param pUbicacion Ubicación del esquema
     * @param pShared_Secret Clave del esquema
     * @param arguments (JSONObject) columnas Ej. {"column":"nombre","pattern":"Pepe","index":{"tree":""}}
     * column: nombre de la columna sobre la cual se va a buscar
     * patter: Valor a buscar en la estructura
     * tree: tipo de arbol del indice que se va a utilizar para buscar
     * tree: B,B+,ABB,RN,AVL,SPLAY,AA <-- Debe escribirse de esa manera
     * tree: null si no se desea buscar con indices (Busqueda lineal)
     * @return
     */
    public String search(String pNombre, String pUbicacion,String pShared_Secret,JSONObject arguments){
        String retorno = null;
        JSONObject tmp = new JSONObject();
        tmp.put("command", "search");
        tmp.put("name", pNombre);
        tmp.put("location", pUbicacion);
        tmp.put("shared_secret", pShared_Secret);
        tmp.put("arguments", arguments);
        tmp.put("content-type", _protocol);
        send(tmp.toString());
        retorno = read();
        return retorno;
    }


    //{"command":"search","scheme":"USERS","location":"cr.ac.tec.ce1103.databases","shared_secret":"12345","arguments":{"column":"id","pattern":"3",index:{"tree":"null"}}}
    /**
     * Envia mensaje por el Socket
     * @param txt Texto a enviar
     */
    private void send(String txt){
        if(socket.isConnected()){
            out.println(txt);
            out.flush();
        }else{
            System.out.println("Servidor desconectado, imposible enviar dato");
        }
    }
    /**
     * Escucha por nuevos datos
     */
    private String read(){
        String retorno = null;
        try {
            String data = in.readLine();
            if(data==null){
                //_connected =false;
            }else{
                retorno=  data;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return retorno;
    }
    public static void main(String[] args){
        InmemDB db = new InmemDB("localhost",8080,"json");
        System.out.println(db.get_schemes());
    }

}