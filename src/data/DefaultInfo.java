package data;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * .properties: valores por defecto del sistema
 */
public class DefaultInfo {

    //General Software Data
    //Server's login configuration
    private boolean LOGIN=false;
    private int INTENTOS_LOGIN=0;
    private final int PIN = 1029384756;
    //Server's port
    private int  PUERTO=0;
    //WebServer's Configuration
    private String FOLDER = null;
    private String HOME_PAGE = null;
    //Another Configurations
    private int MAX_CONNECTION=0;
    //Package
    private JSONObject PROPERTIES = new JSONObject();

    /**
     * Es el constructor de la clase
     * @throws JSONException
     */
    public DefaultInfo(){
        /*
         * Carga el archivo properties de la configuración del servidor
         */
        Properties properties = new Properties();
        InputStream data = null;
        try {
            data = new FileInputStream("config.txt");
            properties.load(data);
            this.LOGIN = Boolean.parseBoolean(properties.getProperty("LOGIN"));
            this.INTENTOS_LOGIN = Integer.parseInt(properties.getProperty("INTENTOS_LOGIN"));
            this.PUERTO = Integer.parseInt(properties.getProperty("PUERTO"));
            this.FOLDER = properties.getProperty("FOLDER");
            this.HOME_PAGE = properties.getProperty("HOME_PAGE");
            this.MAX_CONNECTION = Integer.parseInt(properties.getProperty("MAX_CONNECTION"));
            //Store in json object
            PROPERTIES.put("LOGIN", this.LOGIN);
            PROPERTIES.put("INTENTOS_LOGIN", this.INTENTOS_LOGIN);
            PROPERTIES.put("PUERTO", this.PUERTO);
            PROPERTIES.put("FOLDER", this.FOLDER);
            PROPERTIES.put("HOME_PAGE", this.HOME_PAGE);
            PROPERTIES.put("MAX_CONNECTION", this.MAX_CONNECTION);
        } catch (IOException e) {
            Tools.error(e);
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (IOException e) {
                    Tools.write_(e.getMessage());
                }
            }
        }
    }
    /**
     * Se obtiene un booleano referente a si se debe iniciar sesión para ejectar el servidor.
     * @return Si es necesario iniciar sesión
     */
    public boolean get_LOGIN(){
        return this.LOGIN;
    }
    /**
     * Se obtiene los intentos que tiene el usuario si pone pines incorrectos antes de poder iniciar sesión
     * @return cantidad de intentos de login
     */
    public int get_INTENTOS_LOGIN(){
        return this.INTENTOS_LOGIN;
    }
    /**
     * Se obtiene el puerto en el cual el servidor va a escuchar
     * @return número de puerto
     */
    public int get_PUERTO(){
        return this.PUERTO;
    }
    /**
     * Se obtiene la carpeta en la cual van a estar los documentos HTML,CSS y Javascript, así como imágenes y demás para la consola
     * @return ruta absoluta
     */
    public String get_FOLDER(){
        return this.FOLDER;
    }
    /**
     * Se obtiene la página inicial cuando se llame al servidor en \
     * @return nombre de un archivo html
     */
    public String get_HOME_PAGE(){
        return this.HOME_PAGE;
    }
    /**
     * Se obtiene la cantidad de usuarios máximos que se pueden conectar
     * @return cantidad máxima de usuarios
     */
    public int get_MAX_CONNECTION(){
        return this.MAX_CONNECTION;
    }
    /**
     * Se obtiene el pin para desbloquear la aplicación
     * @return pin
     */
    public int get_PIN(){
        return this.PIN;
    }
    /**
     * Este método se llama desde la clase Main para mostrar en consola las configuraciones actuales del sistema
     * @return Las configuraciones
     */
    public String get_properties(String pFormato){
        if(pFormato=="JSON"){
            return PROPERTIES.toString();
        }else{
            return XML.toString(PROPERTIES);
        }
    }

}
