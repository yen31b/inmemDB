package logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import gui.server.Main;
import data.Tools;
import structures.lists.Array;

/**
 * Servidor principal
 *
 */

public class Server extends Thread {
    //Global
    ServerSocket _servidor = null;
    //Server's Details
    private int _puerto = Main._defaultinfo.get_PUERTO();;
    private boolean _isOnline = false;
    private final int _max_connections = Main._defaultinfo.get_MAX_CONNECTION();
    private ExecutorService _ExecutorService = Executors.newFixedThreadPool(_max_connections);
    //Clients connected
    private Array<ClientWorker> _clientes = new Array<ClientWorker>();
    //BD Motor
    private static Engine _engine = new Engine();


    /**
     * Constructor
     */
    public Server(){
        try {
            _servidor = new ServerSocket(this._puerto);
            this._isOnline = true;
            Main.log("Servidor en linea");
            Tools.write_("Servidor principal en linea y escuchando en: "+this._servidor.getLocalSocketAddress(),Tools.COLOR_GREEN);
        }catch (IOException e){
            Tools.error(e);
        }
    }
    /**
     * Método que acepta nuevos clientes
     */
    public void run(){
        try {
            while(this._isOnline){
                Socket clientSocket = _servidor.accept();
                ClientWorker cliente = new ClientWorker(clientSocket);
                _clientes.insertar(cliente);
                _ExecutorService.submit(cliente);
            }
        } catch (IOException e) {
            Tools.error(e);
        }
    }
    /**
     * Detiene el servidor
     */
    public void stop_server(){
        try {
            this._isOnline=false;
            _servidor.close();
        } catch (IOException e) {
            Tools.error(e);
        }
    }
    /**
     *
     * @return Los clientes conectados
     */
    public Array<ClientWorker> get_Clientes(){
        return this._clientes;
    }
    public static Engine get_ENGINE(){
        return _engine;
    }
}
