import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.regex.*;


public class ClientThread extends ClientFonctions implements Runnable{

  private String id;
  private int port;
  private boolean start;

  public ClientThread(String i, int p){
    this.id = i;
    this.port = p;
    this.start = false;
  }

  public boolean getStart(){
    return this.start;
  }

  public void setStart(boolean s){
    this.start = s;
  }

  public String getId(){
    return this.id;
  }

  public void setId(String i){
    this.id = i;
  }

  public int getPort(){
    return this.port;
  }

  public void setPort(int p){
    this.port = p;
  }



  public void run(){

    try{

      Scanner sc= new Scanner(System.in);

      Socket socket = new Socket("localhost", 4242);

      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

      String choix;

      System.out.println();

      System.out.println("*******************************************************");
      System.out.println("*                                                     *");
      System.out.println("*                                                     *");
      System.out.println("*                 Connexion acceptée                  *");
      System.out.println("*                                                     *");
      System.out.println("*                                                     *");
      System.out.println("*******************************************************");

      System.out.println();


      debut(this, pw, br); //affiche la liste des parties créés



      boolean test = true;

      while(true){ //tant que le joueur n'est pas inscris dans une partie et qu'il n'est pas pret, on reste dans ce while
        System.out.println();
        System.out.println("***Que voulez vous faire? ***");
        System.out.println();

        System.out.println("Faites ok quand vous etes pret (si vous etes inscris à une partie)");
        System.out.println("Faites 1 pour vous inscire");
        System.out.println("Faites 2 pour vous désinscrire de la partie");
        System.out.println("Faites 3 pour connaitre la taille d'un labyrinthe");
        System.out.println("Faites 4 pour connaitre les joueurs inscrits à une partie");
        System.out.println("Faites 5 pour connaitre les parties créés mais non commencées");
        System.out.println();
        System.out.print("Votre choix: ");
        choix=sc.nextLine();


        if(choix.equals("1")){
          connexion(this, pw, br);
          this.setStart(true);
      }
      if(choix.equals("2")){
        desinscription(this, pw, br);
        this.setStart(false);
      }

      if(choix.equals("3"))
        tailleLabyrinthe(this, pw, br);

      if(choix.equals("4"))
        listeJoueursInscrits(this, pw, br);

      if(choix.equals("5"))
        listePartiesCrees(this, pw, br);

      if(choix.equals("ok")){

        //if(this.getStart()==true){
          envoieStart(this, pw, br);
          break;
        //  test = false;
        }


    }
    System.out.println("ok");
    /**********************Lancement du jeu************************/

    String reponse = br.readLine();
    System.out.println();

    System.out.println("*******************************************************");
    System.out.println("*                                                     *");
    System.out.println("*                                                     *");
    System.out.println("*       Lancement du Jeu! Amusez vous bien :)         *");
    System.out.println("*                                                     *");
    System.out.println("*                                                     *");
    System.out.println("*******************************************************");

    System.out.println();

    System.out.println();
    System.out.println("###Requete reçue :\""+reponse+"\"###");
    System.out.println();

    String mess1 = reponse.substring(0, reponse.length()-3);
    String [] messTab = mess1.split(" ");
    String addresseDiffusion = decompleterIp(messTab[5]);
    String partieNum = recupererLittleEndian(messTab[1]);
    String hLaby = recupererLittleEndian(messTab[2]);
    String wLaby = recupererLittleEndian(messTab[3]);
    String nbF = recupererLittleEndian(messTab[4]);

    String portDiffusionS = messTab[6];
    int portDiffusion = Integer.parseInt(portDiffusionS);

    System.out.println("Bienvenue sur la partie "+partieNum+" avec un labyrinthe de taille "+hLaby+" sur "+wLaby+" avec "+nbF+" fantomes!");
    System.out.println();
    System.out.println("Le port de cette partie est "+portDiffusionS);
    System.out.println();
    System.out.println("L'adresse ip est "+addresseDiffusion);
    System.out.println();

    MultiDiffusion md = new MultiDiffusion(addresseDiffusion, portDiffusion);
    Thread t=new Thread(md);
    t.start();

    mess1 = br.readLine();

    System.out.println();
    System.out.println("###Requete reçue :\""+mess1+"\"###");
    System.out.println();

    mess1 = mess1.substring(0, mess1.length()-3);
    messTab = mess1.split(" ");

    String idJoueur = messTab[1];
    System.out.println(idJoueur);
    System.out.println(messTab[2]);
    String xJ = decompleter30(messTab[2]);

    String yJ = decompleter30(messTab[3]);

    System.out.println("Monsieur/madame "+idJoueur+" votre position est "+xJ+" "+yJ);
    System.out.println();

    ClientUDP clientUdp = new ClientUDP(this.getPort());
    Thread t2=new Thread(clientUdp);
    t2.start();





    while(true){
      System.out.println();
      System.out.println("***Que voulez vous faire? ***");
      System.out.println();
      System.out.println("Deplacement: ");
      System.out.println();
      System.out.println("1 pour monter");
      System.out.println("2 pour descendre");
      System.out.println("3 pour aller à gauche");
      System.out.println("4 pour aller à droite");
      System.out.println();

      System.out.println("5 pour quitter");
      System.out.println();

      System.out.println("6 pour liste des joueurs");
      System.out.println();
      System.out.println("Messages");
      System.out.println();

      System.out.println("7 pour envoyer un message général");
      System.out.println("8 pour envoyer un message perso");


      System.out.println();
      System.out.print("Votre choix: ");
      String a = sc.nextLine();

      if(a.equals("5")){
        System.out.println();
        System.out.println("@@@Requete envoyée : \""+"QUIT***"+"\"@@@");
        System.out.println();

        pw.println("QUIT***");
        pw.flush();
        String m = br.readLine();
        System.out.println();
        System.out.println("###Requete reçue :\""+m+"\"###");
        System.out.println();

        System.out.println("Quel dommage, à la prochaine!");
        if(m.equals("BYE***")){
          break;
        }

      }
      else if(a.equals("6")){
        System.out.println();
        System.out.println("@@@Requete envoyée : \""+"GLIST?***"+"\"@@@");
        System.out.println();
        pw.println("GLIST?***");
        pw.flush();

        String mes = br.readLine();
        System.out.println();
        System.out.println("###Requete reçue :\""+mes+"\"###");
        System.out.println();
        String mes1 = mes.substring(0, mes.length()-3);

        String [] mesTab = mes1.split(" ");
        String nbJoS = mesTab[1];
        int nbJo = Integer.parseInt(nbJoS);
        System.out.println("Il y a "+nbJo+" joueurs dans la partie");
        System.out.println("Voici leur position et score:");
        for(int i = 0; i<nbJo; i++){
          mes= br.readLine();

          System.out.println();
          System.out.println("###Requete reçue :\""+mes+"\"###");
          System.out.println();
          mes= mes.substring(0, mes.length()-3);

          mesTab = mes.split(" ");
          String idJou = mesTab[1];
          String xJou = decompleter30(mesTab[2]);
          String yJou = decompleter30(mesTab[3]);
          String scoreJou = decompleter40(mesTab[4]);
          System.out.println("Le joueur "+idJou+" en position "+xJou+" "+yJou+" a un score de "+scoreJou+" points.");
          System.out.println();
        }
      }
        else if(a.equals("8")){
          System.out.println("Quel message ");
          System.out.println();
          System.out.print("Votre choix: ");
          String m = sc.nextLine();
          System.out.println("A quelle personne(id) ");
          System.out.println();
          System.out.print("Votre choix: ");
          String idM = sc.nextLine();
          System.out.println();
          System.out.println("@@@Requete envoyée : \""+"SEND? "+idM+" "+m+"***"+"\"@@@");
          System.out.println();
          pw.println("SEND? "+idM+" "+m+"***");
          pw.flush();
          String message = br.readLine();

          System.out.println();
          System.out.println("###Requete reçue :\""+message+"\"###");
          System.out.println();
          message= message.substring(0, message.length()-3);
          String [] mesTab = message.split(" ");
          if(mesTab[0].equals("SEND!")){
            System.out.println("Le message a été envoyé");
          }
          else
            System.out.println("Le message n'a pas été envoyé");
        }
        else if(a.equals("7")){
          System.out.println("Quel message ");
          System.out.println();
          System.out.print("Votre choix: ");
          String m = sc.nextLine();
          System.out.println();
          System.out.println("@@@Requete envoyée : \""+"ALL? "+m+"***"+"\"@@@");
          System.out.println();
          pw.println("ALL? "+m+"***");
          pw.flush();
          String message = br.readLine();

          System.out.println();
          System.out.println("###Requete reçue :\""+message+"\"###");
          System.out.println();
          message= message.substring(0, message.length()-3);
          String [] mesTab = message.split(" ");
          if(mesTab[0].equals("ALL!")){
            System.out.println("Le message a été envoyé");
          }
          else
            System.out.println("Le message n'a pas été envoyé");

        }

      else{

        /*********Deplacement*****************/

        System.out.println("De combien? ");
        System.out.println();
        System.out.print("Votre choix: ");
        String nbDeplacement = sc.nextLine();
        System.out.println(nbDeplacement);
        if(a.equals("1")){
          int nbD = Integer.parseInt(nbDeplacement);
          System.out.println();
          System.out.println("@@@Requete envoyée : \""+"UP "+completer30(nbD)+"***"+"\"@@@");
          System.out.println();
          pw.println("UP "+completer30(nbD)+"***");
          pw.flush();
        }
        if(a.equals("2")){
          int nbD = Integer.parseInt(nbDeplacement);
          System.out.println();
          System.out.println("@@@Requete envoyée : \""+"DOWN "+completer30(nbD)+"***"+"\"@@@");
          System.out.println();
          pw.println("DOWN "+completer30(nbD)+"***");
          pw.flush();
        }
        if(a.equals("3")){
          int nbD = Integer.parseInt(nbDeplacement);
          System.out.println();
          System.out.println("@@@Requete envoyée : \""+"LEFT "+completer30(nbD)+"***"+"\"@@@");
          System.out.println();
          pw.println("LEFT "+completer30(nbD)+"***");
          pw.flush();
        }
        if(a.equals("4")){
          int nbD = Integer.parseInt(nbDeplacement);
          System.out.println();
          System.out.println("@@@Requete envoyée : \""+"RIGHT "+completer30(nbD)+"***"+"\"@@@");
          System.out.println();
          pw.println("RIGHT "+completer30(nbD)+"***");
          pw.flush();
        }
      }

}
  /*    String mess = br.readLine();
      System.out.println();
      System.out.println("###Requete reçue :\""+mess+"\"###");
      System.out.println();

      mess1 = mess.substring(0, mess.length()-3);

      messTab = mess1.split(" ");

*/

    System.out.println("fin");
      pw.close();
      br.close();
      socket.close();
      t2.interrupt();
      t.interrupt();

    }catch(Exception e){
  //    System.out.println(e);
  //    e.printStackTrace();
    }
  }
}
