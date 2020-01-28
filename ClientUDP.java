import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.regex.*;


public class ClientUDP implements Runnable{

  private int port;

  public ClientUDP(int p){

    this.port = p;
  }

  public int getPort(){
    return this.port;
  }

  public void setPort(int p){
    this.port = p;
  }

  public void run(){

    try{
      DatagramSocket dso=new DatagramSocket(this.getPort());
      byte[]data=new byte[100];
      DatagramPacket paquet=new DatagramPacket(data,data.length);
      while(true){
        dso.receive(paquet);
        String st=new String(paquet.getData(),0,paquet.getLength());
        st= st.substring(0, st.length()-3);
        String [] mesTab = st.split(" ");
        int entier = mesTab[1].length()+5;
        String messageEntier = st.substring(entier, st.length());
        System.out.println("Message generale de la part de "+mesTab[1]+" :"+messageEntier);

      }
    }catch(Exception e){
  //    System.out.println(e);
  //    e.printStackTrace();
    }
  }
}
