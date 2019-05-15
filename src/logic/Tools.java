package logic;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Tools {
    private static final String COLOR_RESET = "\u001B[0m";
    public static final String COLOR_BLACK = "\u001B[30m";
    public static final String COLOR_RED = "\u001B[31m";
    public static final String COLOR_GREEN = "\u001B[32m";
    public static final String COLOR_YELLOW = "\u001B[33m";
    public static final String COLOR_BLUE = "\u001B[34m";
    public static final String COLOR_PURPLE = "\u001B[35m";
    public static final String COLOR_CYAN = "\u001B[36m";
    public static final String COLOR_WHITE = "\u001B[37m";

    /**
     * crea una nueva linea vacía en consola
     */
    public static void space(){
        System.out.println();
    }
    /**
     * Imprime un texto en consola, sin hacer salto de linea
     * @param ptext Texto a imprimir en consola
     */
    public static void write(String ptext){
        System.out.print(ptext);
    }
    /**
     * Imprime un texto en consola, sin hacer salto de linea, con color
     * @param ptext Texto a imprimir en consola
     */
    public static void write(String ptext,String pColor){
        System.out.print(pColor+ptext+COLOR_RESET);
    }
    /**
     * Imprime un texto en consola, haciendo salto de linea
     * @param ptext Texto a imprimir en consola
     */
    public static void write_(String ptext){
        System.out.println(ptext);
    }
    /**
     * Imprime un texto en consola, haciendo salto de linea, con color
     * @param ptext Texto a imprimir en consola
     */
    public static void write_(String ptext,String pColor){
        System.out.println(pColor+ptext+COLOR_RESET);
    }
    /**
     * Imprime un texto en consola, haciendo salto de linea antes y despues del texto
     * @param ptext Texto a imprimir en consola
     */
    public static void write__(String ptext){
        space();
        System.out.println(ptext);
    }
    /**
     * Imprime un texto en consola, haciendo salto de linea antes y despues del texto, con color
     * @param ptext Texto a imprimir en consola
     */
    public static void write__(String ptext,String pColor){
        space();
        System.out.println(pColor+ptext+COLOR_RESET);
    }
    /**
     * Termina la aplicación
     */
    public static void exit(){
        System.exit(0);
    }
    /**
     * Lee un buffer de datos, correspondiente a los comandos de la consola
     */
    public static String read(){
        String txt = "";
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            txt = entrada.readLine();
        }
        catch (IOException e) {
            write_(e.getMessage());
        }
        return txt;
    }
    /**
     * Genera una fecha actual basado en el calendaria gregoriano
     * @return Fecha actual
     */
    public static String fecha(){
        String data = null;
        Calendar fecha = new GregorianCalendar();
        int ano = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);
        data = ano+"/"+(mes+1)+"/"+dia+" "+hora+":"+minuto+":"+segundo;
        return data;
    }
    /**
     * Limpia la consola
     */
    public  static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
	    /*try{
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	    for(int clear = 0; clear < 100; clear++) {
	        write_("\b");
	    }*/
    }
    /**
     * Genera un número aleatorio entero
     * @param start Inicio del intervalo donde se escogerá el número
     * @param end Fin del intervalo donde se escogerá el número
     * @return Número aleatorio
     */
    public static int random(int start, int end){
        Random random = new Random();
        double pre_val = random.nextDouble();
        int val = (int) (Math.abs(pre_val)*(end-start)+start);
        return val;
    }
    /**
     * Manejador de Excepciones
     * @param pException Excepcion
     */
    public static void error(IOException pException){
        //Main.log("Error: "+pException.getMessage());
        pException.printStackTrace();
    }
    /**
     * Manejador de Excepciones
     * @param pException Excepcion
     */
    public static void error(InterruptedException pException){
        //Main.log("Error: "+pException.getMessage());
        pException.printStackTrace();
    }
    /**
     * Manejador de Excepciones
     * @param pException Excepcion
     */
    public static void error(Exception pException){
        //Main.log("Error: "+pException.getMessage());
        pException.printStackTrace();
    }
    /**
     * Valida si un String es un número
     * @param s String a validar
     * @return True or False
     */
    public static boolean isInteger(String s){
        return isInteger(s,10);
    }

    /**
     * Verifica si un String  es Integer
     * @param s String a validar
     * @param radix Radio
     * @return True or False
     */
    private static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    /**
     * Make a beep sound
     */
    public static void beep(){
        Toolkit.getDefaultToolkit().beep();
    }
    /**
     * Retorna el tiempo en nanosegundos actual del sistema
     * @return long del tiempo actual
     */
    public static long get_nanotime(){
        return System.nanoTime();
    }
    /**
     * Retorna la diferencia (final-inicial) de dos long en segundos
     * @param pTime Tiempo inicial de la comparación
     * @return La diferencia
     */
    public static double get_difference_nanotime(long pTime){
        long diferencia = Tools.get_nanotime()-pTime;
        return (double) diferencia/1000000.0;
    }
}