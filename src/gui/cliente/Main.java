package gui.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import logic.Listening;

import data.Tools;

public class Main {

    public static Socket _socket=null;
    public static PrintWriter _out;
    public static BufferedReader _in;
    public static boolean _reading = true;
    public static int _port = 8080;
    public static boolean _flag = false;
    private String Error = "error";

    /**
     * Metodo para lectura de lineas
     * @return txt
     */
    private static String read(){
        String txt = "";
        try{
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            txt = entrada.readLine();
        }catch (IOException e) {

        }
        return txt;
    }

    /**
     * Metodo de envio de datos al servidor
     * @param txt
     */

    @SuppressWarnings("unused")
    private static void send(String txt){
        if(_socket.isConnected()){
            _out.println(txt);
            _out.flush();
        }else{
            System.out.println("Servidor desconectado, imposible enviar dato");
        }
    }

    /**
     * Metodo para lectura de entadas
     */

    private static void reader(){
        while(_reading){
            Tools.write(">>>");
            analyzer(read());
        }
    }

    /**
     * Alalizador de comandos provenientes de la consola
     * @param pCommand
     */

    private static void analyzer(String pCommand){
        if(pCommand.equals("start")){
            Listening listening = new Listening();
            listening.start();
        }else if(pCommand.equals("exit")){
            System.exit(0);
        }else if(pCommand.equals("login")){
            //hacer login
        }else if(pCommand.equals("​content-type: json")){
            //hacer llamada JSON
            Tools.write("hola");
            Tools.space();
        }else if(pCommand.equals("​content-type: xml")){
            //hacer llamada XML
        }else if(pCommand.equals("new scheme")){
            //introduccion de esquema
        }else if(pCommand.equals("search scheme")){
            //busqueda de equemas
        }else if(pCommand.equals("delete scheme")){
            //borrado de esquemas
        }else if(pCommand.equals("put data")){
            //insecion de datos
        }else if(pCommand.equals("search data")){
            //busqueda de datos
        }else if(pCommand.equals("delete data")){
            //borrado de datos
        }else if(pCommand.equals("create index")){
            //crear indices
        }else if(pCommand.equals("delet index")){
            //borrado de indices
        }else if(pCommand.equals("register")){
            //registrar
        }else if(pCommand.equals("Control_1")){
            _flag = false;
            Tools.write("false" + "\n");
        }else if(pCommand.equals("Control_2")){
            _flag = true;
            Tools.write("true"+ "\n");
        }
        else{
            System.out.println("Comando desconocido "+pCommand);
        }
    }

    /**
     * Metodo para el Mensaje de Bienvenida
     */

    private static void welcome(){
        for(int i=0;i<=70;i++){
            System.out.print("-");
        }
        Tools.space();
        Tools.write_("\t\t\t##########################");
        Tools.write_("\t\t\t####### Bienvenido #######");
        Tools.write_("\t\t\t##########################");
        Tools.space();
        for(int i=0;i<=70;i++){System.out.print("-");}
        Tools.space();
    }

    /**
     * Metodo para el Mensaje de Comandos
     */

    public static void comandos(){
        Tools.write("start\n");
        Tools.write("login\n");
        Tools.write("select\n");
        Tools.write("new scheme\n");
        Tools.write("search scheme\n");
        Tools.write("delete scheme\n");
        Tools.write("put data\n");
        Tools.write("search data\n");
        Tools.write("delete data\n");
        Tools.write("create index\n");
        Tools.write("delete index\n");
        Tools.write("exit\n");

    }

    /**
     * Metodo main
     * @param args
     */

    public static void main(String[] args){
        try {
            _socket = new Socket("localhost",_port);
            _out = new PrintWriter(_socket.getOutputStream(),true);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        welcome();
        comandos();
        reader();
    }
}
