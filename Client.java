import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.regex.*;


public class Client{

  //static ArrayList <Integer> listePortUdp;

  public static void main(String args[]){

    Scanner sc= new Scanner(System.in);
    String portS;
    int port;
    String id;
    System.out.println();

    System.out.println("*******************************************************");
    System.out.println("*                                                     *");
    System.out.println("*                                                     *");
    System.out.println("*   Bienvenue dans le jeu du LABYRHANTE en réseaux    *");
    System.out.println("*                                                     *");
    System.out.println("*                                                     *");
    System.out.println("*******************************************************");

    System.out.println();

    while(true){
      System.out.println("Choisissez un id:");
      id = sc.nextLine();
      if(id.length() > 8){
        System.out.println("Vous devez entrer max 8 caracteres alphanumériques");
      }
      else if(!(id.matches("(\\w)*"))){
        System.out.println("Vous devez entrer seulement des caracteres alphanumériques");
      }
      else
        break;

    }
    System.out.println();

    while(true){
      System.out.println("Choisissez votre port udp: ");
      portS = sc.nextLine();
      port = Integer.parseInt(portS);
      if(portS.length() != 4)
        System.out.println("Entrez un port de taille 4");
      else
        break;
    }

    ClientThread client = new ClientThread(id, port);
    Thread t=new Thread(client);
    t.start();

  }
}
