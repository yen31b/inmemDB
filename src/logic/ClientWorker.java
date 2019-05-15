package logic;

import data.Tools;
import gui.server.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
/**
 * Maneja cada cliente conectado a la app
 *
 */
public class ClientWorker extends Thread{
    private Socket _cliente = null;
    private BufferedReader _reader = null;
    private PrintWriter _writer = null;
    private boolean _running = false;
    private int _id = -1;
    private String content_type=null;


    /**
     * Constructor
     * @param pCliente Socket que se aceptó en el servidor
     */
    public ClientWorker(Socket pCliente){
        this._cliente = pCliente;
        this._id = Tools.random(0,10000);
        this._running = true;
        Main.log("[Cliente][ID: "+this._id+"] Cliente Conectado");
    }
    /**
     * Envía datos (String) al cliente del otro lado de la conexión
     * @param data Datos a enviar (Json)
     */
    public void send(String data){
        _writer.println(data);
        _writer.flush();
    }
    /**
     * Método que se ejecuta del Thread
     */
    public void run(){
        String data;
        try{
            _reader = new BufferedReader(new InputStreamReader(_cliente.getInputStream()));
            _writer = new PrintWriter(_cliente.getOutputStream(), true);
            JSONObject tmp = null;
            while(_running){
                data = _reader.readLine();
                if(data==null){
                    close();
                }else if(data.equals("This will never be catched!")){

                }else{
                    String retorno = null;
                    if(data.toLowerCase().replace(" ","").equals("content-type:json")){
                        if(this.content_type==null){
                            this.content_type = "JSON";
                            retorno = "OK";
                        }else{
                            retorno = "The content-type must be setted just once with this command.";
                        }
                    }else if(data.toLowerCase().replace(" ","").equals("content-type:xml")){
                        if(this.content_type==null){
                            this.content_type = "XML";
                            retorno = "OK";
                        }else{
                            retorno = "The content-type must be setted just once with this command.";
                        }
                    }else if(is_json(data)){
                        if(content_type==null){
                            retorno = "The content-type is not defined. Must be setted first";
                        }else{
                            tmp = new JSONObject(data);
                            if(tmp.has("content-type"))
                                this.content_type = tmp.get("content-type").toString().toUpperCase();
                            try{
                                tmp = Server.get_ENGINE().analyze(tmp);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            if(this.content_type.equals("JSON"))
                                retorno = tmp.toString();
                            else
                                retorno = XML.toString(tmp);
                        }

                    }else if(is_xml(data)){
                        if(content_type==null){
                            retorno = "The content-type is not defined. Must be setted first";
                        }else{
                            tmp = XML.toJSONObject(data);
                            if(tmp.has("content-type"))
                                this.content_type = tmp.get("content-type").toString().toUpperCase();
                            tmp = Server.get_ENGINE().analyze(tmp);
                            if(this.content_type.equals("JSON"))
                                retorno = tmp.toString();
                            else
                                retorno = XML.toString(tmp);
                        }
                    }else if(data.equals("kill me")){
                        close();
                        stop_protocols();
                    }else if(data.equals("")){
                        retorno= "";
                    }else{
                        retorno = "Unknown Command: "+data;
                    }

                    send(retorno);
                }
            }
        }catch (IOException e) {
            close();
            Tools.write__("Fallo de Lectura!");
        }finally{
            stop_protocols();
        }

    }
    private void stop_protocols(){
        try {
            this._reader.close();
            this._writer.close();
            this._cliente.close();
        } catch (IOException e) {
            Tools.error(e);
        }
    }
    /**
     *
     * @param pText String que va a ser validado
     * @return Si es un json válido o no
     */
    private boolean is_json(String pText){
        boolean retorno = false;
        try{
            JSONObject json = new JSONObject(pText);
            json.toString();
            retorno = true;
        }catch(JSONException e){
            retorno = false;
        }
        return retorno;
    }

    private boolean is_xml(String pText){
        boolean retorno = false;
        try{
            JSONObject json = XML.toJSONObject(pText);
            if(json.toString().equals("{}"))
                retorno = false;
            else
                retorno = true;
        }catch (JSONException e){
            retorno = false;
        }
        return retorno;
    }

    /**
     * Se cierra la conexión
     */
    private void close(){
        _running = false;
        Main.log("[Cliente][ID: "+this._id+"] Cliente Desconectado");
    }
    /**
     * Mata el cliente
     */
    public void kill(){
        close();
    }
    /**
     *
     * @return True si el Thread está vivo
     */
    public boolean is_running(){
        return this._running;
    }
    /**
     *
     * @return ID del cliente
     */
    public int get_id(){
        return this._id;
    }
    /**
     *
     * @return El content-type del cliente
     */
    public String get_content_type(){
        return this.content_type;
    }
}
