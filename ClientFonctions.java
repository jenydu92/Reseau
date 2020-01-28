import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class ClientFonctions {


/*********quand le client se connecte, il recoit la liste des parties**********/

  public void debut(ClientThread ct, PrintWriter pw, BufferedReader br){
    try{
    Scanner sc= new Scanner(System.in);

    String mess = br.readLine();
    System.out.println();
    System.out.println("###Requete reçue :\""+mess+"\"###");
    System.out.println();

    String mess1 = mess.substring(0, mess.length()-3);
    String [] reponseTab = mess1.split(" ");
    int nombre = Integer.parseInt(recupererLittleEndian(reponseTab[1]));
    System.out.println("Voici le nombre de partie non commencée(s): "+nombre);

    for(int i = 0; i < nombre; i++){
      mess = br.readLine();
      System.out.println();
      System.out.println("###Requete reçue \": "+mess+"\"###");
      System.out.println();

      mess1 = mess.substring(0, mess.length()-3);
      reponseTab = mess1.split(" ");
      System.out.println("La partie "+recupererLittleEndian(reponseTab[1])+" contient "+recupererLittleEndian(reponseTab[2])+" joueurs");
    }
      }catch(Exception e){
    //    System.out.println(e);
    //    e.printStackTrace();
      }

  }
 /************quand le client veut s'inscrire à une partie**************/

  public void connexion(ClientThread ct, PrintWriter pw, BufferedReader br){
    try{
      Scanner sc= new Scanner(System.in);
      System.out.println("***Voulez vous : ***");
      System.out.println("1: Creer une partie");
      System.out.println("2: Rejoindre une partie");
      System.out.println();
      System.out.print("Votre choix: ");
      String envoie;
      String reponse = sc.nextLine();
      int numeroPartie;
      System.out.println();
      while(true){
        if(reponse.equals("1")){
          envoie = "NEW "+ct.getId()+" "+ct.getPort()+"***";
          System.out.println();
          System.out.println("@@@Requete envoyée :\""+envoie+"\"@@@");
          System.out.println();
          pw.println(envoie);
          pw.flush();
          String mess = br.readLine();
          System.out.println();
          System.out.println("###Requete reçue :\""+mess+"\"###");
          System.out.println();

          String mess1 = mess.substring(0, mess.length()-3);

          String [] messTab = mess1.split(" ");


          if(messTab[0].equals("REGOK")){
            String partieInscris = recupererLittleEndian(messTab[1]);
            System.out.println();
            System.out.println("Vous avez été inscris sur la partie : "+partieInscris);
            System.out.println();
            break;
          }
          else{
            System.out.println("Vous n'avez pas pu vous inscrire");
            break;
          }
        }else if(reponse.equals("2")) {
            while(true){

              System.out.println("***Dans quelle partie?***");
              System.out.println();
              System.out.print("Votre choix: ");
              numeroPartie = sc.nextInt();
              if(numeroPartie > 0 && numeroPartie <= 8)
                break;
            }
          String num = ""+numeroPartie;
          envoie = "REG "+ct.getId()+" "+ct.getPort()+" "+littleEndian(num)+"***";
          System.out.println();
          System.out.println("@@@Requete envoyée : "+envoie+"@@@");
          System.out.println();
          pw.println(envoie);
          pw.flush();
          String mess = br.readLine();
          System.out.println();
          System.out.println("###Requete reçue :\""+mess+"\"###");
          System.out.println();

          String mess1 = mess.substring(0, mess.length()-3);
          String [] messTab = mess1.split(" ");


          if(messTab[0].equals("REGOK")){
            String partieInscris = recupererLittleEndian(messTab[1]);
            System.out.println();
            System.out.println("Vous avez été inscris sur la partie : "+partieInscris);
            System.out.println();
            break;

          }
          else{
            System.out.println("Vous n'avez pas pu vous inscrire");
            break;
          }
        }
        else{
          System.out.println("***Voulez vous :***");
          System.out.println("1: Creer une partie");
          System.out.println("2: Rejoindre une partie");
          System.out.println();
          System.out.print("Votre choix: ");
          reponse = sc.nextLine();
          System.out.println();

        }
      }


    }catch(Exception e){
  //    System.out.println(e);
  //    e.printStackTrace();
    }
  }
/*****************quand le client veut se desinscire d'une partie******************/

  public void desinscription(ClientThread ct, PrintWriter pw, BufferedReader br){
    try{
      System.out.println();
      System.out.println("@@@Requete envoyée :\""+"UNREG***"+"\"@@@");
      System.out.println();
      pw.println("UNREG***");
      pw.flush();

      String mess = br.readLine();
      System.out.println();
      System.out.println("###Requete reçue :\""+mess+"\"###");
      System.out.println();

      String mess1 = mess.substring(0, mess.length()-3);
      String [] reponseTab = mess1.split(" ");
      if(reponseTab[0].equals("UNREGOK")){
        String numPartieS = reponseTab[1];
        int numPartie = Integer.parseInt(numPartieS);
        System.out.println("Vous avez été désinscris de la partie : "+numPartie);
      }
      else{
        System.out.println("Vous n'etiez pas inscris");
      }
    }catch(Exception e){

    }
  }

/***********quand le client dit qu'il est pret**************/

  public void envoieStart(ClientThread ct, PrintWriter pw, BufferedReader br){
    System.out.println();
    System.out.println("@@@Requete envoyée : \"START***\"@@@");

    System.out.println();

    System.out.println("Vous etes pret, attendez les autres joueurs!!");
    System.out.println();

    pw.println("START***");
    pw.flush();

  }

/***************affiche la liste des joueurs inscrits à une partie***********/

  public void listeJoueursInscrits(ClientThread ct, PrintWriter pw, BufferedReader br){
    try{
      Scanner sc= new Scanner(System.in);

      System.out.println("De quelle partie? ");
      System.out.println();
      System.out.print("Votre choix: ");
      String numPartieS = sc.nextLine();
      int numPartie = Integer.parseInt(numPartieS);
      System.out.println();
      System.out.println("@@@Requete envoyée : \""+"LIST? "+littleEndian(numPartieS)+"***"+"\"@@@");
      System.out.println();
      pw.println("LIST? "+littleEndian(numPartieS)+"***");
      pw.flush();
      String mess = br.readLine();
      System.out.println();
      System.out.println("###Requete reçue :\""+mess+"\"###");
      System.out.println();

      String mess1 = mess.substring(0, mess.length()-3);
      String [] reponseTab = mess1.split(" ");
      if(reponseTab[0].equals("DUNNO")){
        System.out.println("Il n'existe pas de partie "+numPartie);
      }else{
        String nbJoueurS = reponseTab[2];
        int nbJoueur = Integer.parseInt(nbJoueurS);
        System.out.println("Il y a "+nbJoueur+" dans la partie");
        System.out.println("Voici la liste des joueurs: ");
        for(int i = 0; i< nbJoueur; i++){
          mess = br.readLine();
          System.out.println();
          System.out.println("###Requete reçue :\""+mess+"\"###");
          System.out.println();

          mess1 = mess.substring(0, mess.length()-3);
          reponseTab = mess1.split(" ");
          String joueur = reponseTab[1];
          System.out.println("Joueur : "+joueur);
        }
      }
    }catch(Exception e){

    }
  }

/*****************affiche la liste des parties créés***********/

  public void listePartiesCrees(ClientThread ct, PrintWriter pw, BufferedReader br){
    try{
      System.out.println();
      System.out.println("@@@Requete envoyée : \"GAMES?***\"@@@");
      System.out.println();
      pw.println("GAMES?***");
      pw.flush();
      String mess = br.readLine();
      System.out.println();
      System.out.println("###Requete reçue :\""+mess+"\"###");
      System.out.println();

      String mess1 = mess.substring(0, mess.length()-3);
      String [] reponseTab = mess1.split(" ");
      String nombreS = recupererLittleEndian(reponseTab[1]);
      int nombre = Integer.parseInt(nombreS);
      System.out.println(nombre);
      String recu;
      for(int i = 0; i<nombre; i++){
        recu= br.readLine();
        System.out.println();
        System.out.println("###Requete reçue :\""+recu+"\"###");
        System.out.println();

        recu = recu.substring(0, recu.length()-3);
        reponseTab = recu.split(" ");
        nombreS = recupererLittleEndian(reponseTab[1]);
        nombre = Integer.parseInt(nombreS);
        String joueurS = recupererLittleEndian(reponseTab[2]);
        int joueur = Integer.parseInt(joueurS);

        System.out.println();
        System.out.println("La partie "+nombre+" contient "+joueur+" joueurs");
        System.out.println();

      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }

public void tailleLabyrinthe(ClientThread ct, PrintWriter pw, BufferedReader br){
  try{
    Scanner sc= new Scanner(System.in);
    System.out.println("De quelle partie ? ");
    System.out.println();
    System.out.print("Votre choix: ");
    String a = sc.nextLine();
    System.out.println("@@@Requete envoyée :\""+"SIZE? "+littleEndian(a)+"***"+"\"@@@");

    pw.println("SIZE? "+littleEndian(a)+"***");
    pw.flush();
    String mess = br.readLine();
    System.out.println();
    System.out.println("###Requete reçue :\""+mess+"\"###");
    System.out.println();

    String mess1 = mess.substring(0, mess.length()-3);
    String [] reponseTab = mess1.split(" ");
    String nombreS = recupererLittleEndian(reponseTab[1]);
    int nombre = Integer.parseInt(nombreS);
    String hS = recupererLittleEndian(reponseTab[2]);
    int h = Integer.parseInt(hS);
    String wS = recupererLittleEndian(reponseTab[3]);
    int w = Integer.parseInt(wS);
    System.out.println("Le labyrinthe de la partie "+nombre+" a une largeur de "+w+" et une longueur de "+h);
  }catch(Exception e){

  }
  }

  public String completerIp(String ip){
    String ipComplete = ip;
    for(int i = 0; i < 15; i++){
      if(ipComplete.length()<15){
        ipComplete = ipComplete+"#";
      }
    }
    return ipComplete;
  }

  public String decompleterIp(String ip){
    String ipDecomplete;
    String [] ipTab = ip.split("#");
    ipDecomplete = ipTab[0];
    return ipDecomplete;
  }

  public String completer30(int a){
    String a2 = ""+a;
  if(a>=0 && a < 10){
    String a1 = "00"+a;
    return a1;
  }else if(a > 9 && a < 100){
    String a1 = "0"+a;
    return a1;
  }else
    return a2;
}

public String decompleter30(String a){
String a1 = a.substring(0,1);
String a2 = a.substring(1,2);
String a3 = a.substring(2,3);

String fi;
if(a1.equals("0") && a2.equals("0"))
  return a3;
else if(a1.equals("0")){
  fi = a2+a3;
  return fi;
}
else{
  fi = a1+a2+a3;
  return fi;
}

}
public String completer4O(int a){
  String a2 = ""+a;
  if(a>=0 && a < 10){
    String a1 = "000"+a;
    return a1;
  }else if(a > 9 && a < 100){
    String a1 = "00"+a;
    return a1;
  }else if(a >99 && a < 1000){
    String a1 = "0"+a;
    return a1;
  }else
    return a2;
}
public String decompleter40(String a){
  String a1 = a.substring(0,1);
  String a2 = a.substring(1,2);
  String a3 = a.substring(2,3);
  String a4 = a.substring(3,4);
  String fi;
  if(a1.equals("0") && a2.equals("0") && a3.equals("0"))
    return a4;
  else if(a1.equals("0") && a2.equals("0")){
    fi = a3+a4;
    return fi;
  }
  else if(a1.equals("0")){
    fi = a2+a3+a4;
    return fi;
  }
  else{
    fi = a1+a2+a3+a4;
    return fi;
  }

}


  public String littleEndian(String a){
    if(a.length()==2)
    return a;
    else
      return "0"+a;
  }

  public String recupererLittleEndian(String a){
    if(a.substring(0,1).equals("0"))
      return a.substring(1,2);
    else
      return a;
  }
}
