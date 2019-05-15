package logic;

import java.io.IOException;
import gui.cliente.Main;;

public class Listening extends Thread{
    private boolean listening = false;
    public Listening(){
        listening = true;
    }
    private void lectura(){
        try {
            String data = Main._in.readLine();
            if(data==null){
                listening=false;
                System.out.println("Servidor Offline");
            }else{
                System.out.println("Mensaje:");
                System.out.println(data);
                System.out.println("");
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void run(){
        while(listening){
            lectura();
        }
    }
}
