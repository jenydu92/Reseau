import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class MultiDiffusion implements Runnable{

  private int numP;
  private String add;

  public MultiDiffusion(String add,int num){
    this.numP = num;
    this.add = add;

  }


public int getNumP(){
  return this.numP;
}

public void setNumP(int n){
  this.numP = n;
}

public String getAdd(){
  return this.add;
}

public void setAdd(String a){
  this.add = a;
}


  public void run(){

    try{
      MulticastSocket mso=new MulticastSocket(this.numP);

      mso.joinGroup(InetAddress.getByName(this.add));

      byte[]data=new byte[100];
      DatagramPacket paquet=new DatagramPacket(data,data.length);

      while(true){

        mso.receive(paquet);
        String st=new String(paquet.getData(),0,paquet.getLength());
        st= st.substring(0, st.length()-3);
        String [] mesTab = st.split(" ");
        int entier = mesTab[1].length()+5;
        String messageEntier = st.substring(entier, st.length());
        System.out.println("Message generale de la part de "+mesTab[1]+" :"+messageEntier);


        }


    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
