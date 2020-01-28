import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

//****************************************************************************

public class Server{

	public static void main(String[] args){

		try{


			ServerSocket server=new ServerSocket(4242);

      ArrayList <Games> listePartie = new ArrayList<Games>();

      while(true){
        Socket socket=server.accept();

        ServeurThread serveur = new ServeurThread(socket, socket.getLocalPort(), listePartie);
        Thread t=new Thread(serveur);
        t.start();
      }

    }catch(Exception e){
      //	System.out.println(e);
      //	e.printStackTrace();
    }
  }
}
