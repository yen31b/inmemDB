package gui.server;

import data.DefaultInfo;
import data.Tools;
import logic.ClientWorker;
import logic.Server;

import structures.lists.Array;
import structures.lists.ListaSimple;


public class Main {
    public static DefaultInfo _defaultinfo = new DefaultInfo();
    private static Server _server = null;
    private static Array<String[]> _log = new Array<String[]>();

    /**
     * Main Method
     * @param args
     */
    public static void main(String[] args){
        for(int i=0;i<=70;i++){Tools.write("-");}
        Tools.space();
        Tools.write_("Tecnológico de Costa Rica");
        Tools.write_("Ingeniería en Computadores");
        Tools.write_("Algoritmos y Estructuras de Datos I");
        Tools.write_("Consola de Administración del Servidor");
        Tools.write_(_defaultinfo.get_NOMBRE_PROYECTO()+" "+_defaultinfo.get_VERSION_DEL_PROYECTO());
        for(int i=0;i<=70;i++){Tools.write("-");}
        Tools.space();
        if(_defaultinfo.get_LOGIN()){
            Tools.space();
            int intentos = _defaultinfo.get_INTENTOS_LOGIN();
            while(intentos>0){
                Tools.write("["+Integer.toString(intentos)+" restantes] PIN de acceso >>> ");
                int userpin = Integer.parseInt(Tools.read());
                if (userpin==_defaultinfo.get_PIN()){
                    log("Inicio de Sesión Correcto");
                    welcome();
                    reader();
                }else{
                    log("Inicio de Sesión Fallido");
                    Tools.write_("PIN incorrecto");
                    intentos--;
                }
            }
            Tools.write_("Has superado los intentos, el programa terminará");
            Tools.exit();
        }else{
            log("Login desabilitado, inicio de sesión automático");
            welcome();
            reader();
        }
    }
    /**
     * Mensaje de Bienvenida
     */
    private static void welcome(){
        Tools.space();
        Tools.write_("\t\t\t##########################");
        Tools.write_("\t\t\t####### Bienvenido #######");
        Tools.write_("\t\t\t##########################");
        Tools.space();
    }
    /**
     * Método estático para poder guardar en el log
     * @param pHistory Suceso
     */
    public static void log(String pHistory){
        String tmp[] = {Tools.fecha(),pHistory};
        _log.insertar(tmp);
    }
    /**
     * Método que "lee" los datos recibidos por el usuario
     */
    private static void reader(){
        boolean tmp = true;
        while(tmp){
            Tools.write(">>> ");
            analyzer(Tools.read());
        }
    }
    private static void analyzer(String pCommand){
        if(pCommand.equals("exit")){
            Tools.exit();

        }else if(pCommand.equals("clear")){
            Tools.clearConsole();

        }else if(pCommand.equals("get properties")){
            String formato = null;
            Tools.write("Formato (JSON || XML) >>> ");
            formato = Tools.read();
            if(formato.equals("JSON") || formato.equals("json")){
                Tools.write_(_defaultinfo.get_properties("JSON"));
            }else if(formato.equals("XML") || formato.equals("xml")){
                Tools.write_(_defaultinfo.get_properties("XML"));
            }else if(formato.isEmpty() || formato.equals(" ")){
                Tools.write_("Debe indicar un formato",Tools.COLOR_YELLOW);
            }else{
                Tools.write_(formato+" no es un formato permitido",Tools.COLOR_RED);
            }

        }else if(pCommand.equals("log")){
            for(int i=0;i<_log.get_size();i++){
                String[] tmp= (String[]) _log.get(i);
                Tools.write_("["+tmp[0]+"] "+tmp[1]);
            }

        }else if(pCommand.equals("start")){
            if(_server==null){
                _server = new Server();
                _server.start();
            }else{
                Tools.write_("El servidor principal ya está iniciado",Tools.COLOR_YELLOW);
            }

        }else if(pCommand.equals("stop")){
            _server.stop_server();
        }else if(pCommand.equals("get clientes")){
            Array<ClientWorker> clientes = _server.get_Clientes();
            for(int i = 0; i<clientes.get_size();i++){
                ClientWorker cliente = (ClientWorker) clientes.get(i);
                if(cliente.is_running()){
                    Tools.write_("["+cliente.get_id()+"] Content-Type: "+cliente.get_content_type());
                }
            }

        }else if(pCommand.equals("get schemes")){
            ListaSimple schemes = Server.get_ENGINE().get_schemes();
            for(int i = 0;i < schemes.get_size(); i++){
                ListaSimple scheme = (ListaSimple) schemes.get(i);
                if(scheme!=null){
                    Tools.write_("Esquema #"+(i+1),Tools.COLOR_BLUE);
                    Tools.write_("\tNombre: "+(String) scheme.get(0),Tools.COLOR_CYAN);//Nombre
                    Tools.write_("\tUbicación: "+(String) scheme.get(1),Tools.COLOR_CYAN);//Location
                    Tools.write_("\tShared Secret: "+(String) scheme.get(2),Tools.COLOR_CYAN);//Shared Secret
                    Tools.write_("\tColumnas:",Tools.COLOR_CYAN);
                    ListaSimple columnas = (ListaSimple) scheme.get(3);
                    for(int j=0;j<columnas.get_size();j++){
                        ListaSimple columna = (ListaSimple) columnas.get(j);
                        Tools.write_("\t\tColumna #"+(j+1),Tools.COLOR_GREEN);
                        Tools.write_("\t\t\tNombre: "+(String) columna.get(0),Tools.COLOR_PURPLE);//Nombre
                        if(columna.get(1).equals("join")){
                            ListaSimple join_data = (ListaSimple) columna.get(3);
                            Tools.write("\t\t\tTipo: "+(String) columna.get(1),Tools.COLOR_PURPLE);//Tipo
                            Tools.write(" ("+(String) join_data.get(0) + " -> ",Tools.COLOR_YELLOW);//Esquema donde se une
                            Tools.write((String) join_data.get(1) + " -> ",Tools.COLOR_YELLOW);//Columna relacionada
                            Tools.write_((String) join_data.get(2) + ")",Tools.COLOR_YELLOW);//Columna del dato
                        }else{
                            Tools.write_("\t\t\tTipo: "+(String) columna.get(1),Tools.COLOR_PURPLE);//Tipo
                        }
                        Tools.write_("\t\t\tLargo: "+(String) columna.get(2),Tools.COLOR_PURPLE);//Largo
                    }
                    ListaSimple Indices = (ListaSimple) scheme.get(4);
                    if(Indices.get_size()==0){
                        Tools.write_("\tÍndices: [NO HAY]",Tools.COLOR_CYAN);//Índices
                    }else{
                        Tools.write_("\tÍndices:",Tools.COLOR_CYAN);//Datos
                        Tools.write_("\t\tColumna\tTipo de Árbol",Tools.COLOR_PURPLE);
                        for(int j=0;j<Indices.get_size();j++){
                            ListaSimple Indice = (ListaSimple) Indices.get(j);
                            if(Indice!=null){
                                Tools.write("\t\t");
                                String tmp_columna =(String) Indice.get(0);
                                String tmp_arbol =(String) Indice.get(1);
                                Tools.write_(tmp_columna+"\t"+tmp_arbol);
                            }else{
                                Tools.write_("\t\tIndice #"+(j+1) + " ha sido eliminado",Tools.COLOR_RED);
                            }
                        }
                    }
                    ListaSimple Datos = (ListaSimple) scheme.get(5);
                    if(Datos.get_size()==0){
                        Tools.write_("\tDatos: [NO HAY]",Tools.COLOR_CYAN);//Datos
                    }else{
                        Tools.write_("\tDatos:",Tools.COLOR_CYAN);//Datos
                        Tools.write("\t\t");
                        for(int j=0;j<columnas.get_size();j++){
                            ListaSimple columna = (ListaSimple) columnas.get(j);
                            Tools.write((String) columna.get(0)+"\t\t",Tools.COLOR_PURPLE);//Nombre
                        }
                        Tools.space();
                        for(int j = 0;j<Datos.get_size();j++){
                            ListaSimple item = (ListaSimple) Datos.get(j);
                            Tools.write("\t\t");
                            for(int k = 0;k<item.get_size();k++){
                                Tools.write(((String)item.get(k))+"\t\t");
                            }
                            Tools.space();
                        }
                    }
                }else{
                    Tools.write_("Esquema #"+(i+1) + " ha sido eliminado",Tools.COLOR_RED);
                }
            }
        }else if(pCommand.equals("")){
            Tools.write_("Indique alguna instrucción"+pCommand,Tools.COLOR_YELLOW);
        }else{
            Tools.write_("No podemos entender la instrucción: "+pCommand,Tools.COLOR_RED);
        }
    }
}

