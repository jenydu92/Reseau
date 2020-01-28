import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ServeurThread extends ServeurFonctions implements Runnable{

  public Socket socket;

  int port;
  ArrayList <Games> listePartie;
  Games jeu;
  Joueur joueur;

  public ServeurThread(Socket s, int p, ArrayList <Games> l){
    this.socket = s;

    this.port = p;
    this.listePartie = l;
  }

  public void run(){

    try{

      PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      int nombreJoueur;

      nouveauJoueurConnecte(this, pw, br);


      while(true){
        String mess = br.readLine();
        System.out.println(mess);
        String mess1 = mess.substring(0, mess.length()-3);
        String [] messTab = mess1.split(" ");

        System.out.println("le mess= "+messTab[0]);

/******************************Creation partie***************************/

        if(messTab[0].equals("NEW")){
          String portString = messTab[2];
          int portClient = Integer.parseInt(portString);
          String idJoueur = messTab[1];

          creationPartie(this, pw, br, messTab[1], portClient);
          System.out.println("Voici l'id du joueur qui créé une partie: "+this.joueur.getId());
        }


/******************************Rejoindre partie**************************/

        if(messTab[0].equals("REG")){
          String portString = messTab[2];
          int portClient = Integer.parseInt(portString);
          String idJoueur = messTab[1];

          int partieChoisie = Integer.parseInt(messTab[3]);
          rejoindrePartie(this, pw, br, idJoueur, portClient, partieChoisie);
        }


/*****************************desinscription********************/

        if(messTab[0].equals("UNREG")){
          desincrireJoueur(this, pw, br, this.jeu, this.joueur);
        }

/*************************taille du labyrinthe*****************/

        if(messTab[0].equals("SIZE?")){
          String partieNumS = messTab[1];
          int partieNum = Integer.parseInt(partieNumS);
          tailleLabyrintheDemande(this, pw, br, partieNum);
        }


/*******************liste joueurs inscris****************/

        if(messTab[0].equals("LIST?")){
          String partieNumS = messTab[1];
          int partieNum = Integer.parseInt(partieNumS);
          joueursInscritsPartie(this, pw, br, this.jeu, partieNum);
        }


/******************liste des games non commencees************/

        if(messTab[0].equals("GAMES?")){
          partiesNonCommencees(this, pw, br);
        }


        if(messTab[0].equals("START")){
          System.out.println("voici:"+this.joueur+"la");

          if(this.joueur == null){
            pw.println("bye");
            pw.flush();
            pw.close();
            br.close();
            this.socket.close();
          }

          else if(this.joueur.getGame()==0){
            System.out.println("salut");
            pw.println("bye");
            pw.flush();
            pw.close();
            br.close();
            this.socket.close();
          }
          else {
            System.out.println("problemeZ");

            joueurPret(this.joueur);
            while(!partiePrete(this.jeu)){
              System.out.println("iciiii");
              if(partiePrete(this.jeu)){
                System.out.println("la partie va commencer");
                break;
              }
            }
            break;

          }
        }

        System.out.println("le joueur est pret");


        }


/*******************debut du jeu********************/

  this.jeu.partieCommence();

  System.out.println("probleme");


    String numS = ""+this.jeu.getNumero();
    String hS = ""+this.jeu.laby.length;
    String wS = ""+this.jeu.laby[0].length;
    String fS = ""+this.jeu.getNbFantome();

      pw.println("WELCOME "+littleEndian(numS)+" "+littleEndian(hS)+" "+littleEndian(wS)+" "+littleEndian(fS)+" "+completerIp(this.jeu.getAddresseIp())+" "+this.jeu.getNumPort()+"***");
      pw.flush();
      mettreJoueursLaby(this, pw, br, this.jeu.getNumero());

      pw.println("POS "+this.joueur.getId()+" "+completer30(this.joueur.getX())+" "+completer30(this.joueur.getY())+"***");
      pw.flush();

      while(this.jeu.getPartie()){
        if(this.jeu.listeJoueur.size()==0 || this.jeu.getNbFantome() ==0){
          if(this.jeu.getPartie()==true){
            Joueur gagnant = this.jeu.gagnant();
            String messagePourEnvoie = "END "+gagnant.getId()+" "+completer40(gagnant.getScore())+"+++";
            byte[]data=messagePourEnvoie.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,this.jeu.getIa());
            this.jeu.getDso().send(paquet);
            this.jeu.setPartie(false);
          }

          break;
        }

        afficheLaby(this, pw, br);
        String mess = br.readLine();
        System.out.println(mess);
        String mess1 = mess.substring(0, mess.length()-3);
        String [] messTab = mess1.split(" ");
        System.out.println("le mess= "+mess);

        if(messTab[0].equals("SEND?")){
          if(this.jeu.getPartie()==false){
          //  finDeJeu(this, pw);
            break;
          }
          String idM = messTab[1];
          String messageEnvoie = messTab[2];
          envoieMessagePerso(this, idM, messageEnvoie, pw);
        }
        if(messTab[0].equals("ALL?")){
          if(this.jeu.getPartie()==false){
            //finDeJeu(this, pw);
            break;
          }
        String messageEnvoie = mess.substring(5, mess.length()-3);
          envoieMessageGeneral(this, messageEnvoie, pw);
        }

        if(messTab[0].equals("UP")){
          if(this.jeu.getPartie()==false){
          //  finDeJeu(this, pw);
            break;
          }
          String nbaS = messTab[1];
          int nba = Integer.parseInt(nbaS);
          monter(this, pw, br, nba, this.joueur.getX(), this.joueur.getY());
          this.jeu.incrementeDeplacement();
          this.jeu.mouvementFantome();
        }

        if(messTab[0].equals("DOWN")){
          if(this.jeu.getPartie()==false){
          //  finDeJeu(this, pw);
            break;
          }
          String nbaS = messTab[1];
          int nba = Integer.parseInt(nbaS);
          descendre(this, pw, br, nba, this.joueur.getX(), this.joueur.getY());
          this.jeu.incrementeDeplacement();
          this.jeu.mouvementFantome();
        }

        if(messTab[0].equals("LEFT")){
          if(this.jeu.getPartie()==false){
        //    finDeJeu(this, pw);
            break;
          }
          String nbaS = messTab[1];
          int nba = Integer.parseInt(nbaS);
          gauche(this, pw, br, nba, this.joueur.getX(), this.joueur.getY());
          this.jeu.incrementeDeplacement();
          this.jeu.mouvementFantome();
        }

        if(messTab[0].equals("RIGHT")){
          if(this.jeu.getPartie()==false){
          //  finDeJeu(this, pw);
            break;
          }
          String nbaS = messTab[1];

          int nba = Integer.parseInt(nbaS);

          droite(this, pw, br, nba, this.joueur.getX(), this.joueur.getY());
          this.jeu.incrementeDeplacement();
          this.jeu.mouvementFantome();
        }

        if(messTab[0].equals("QUIT")){

          supprimerJoueur(this, pw, br, this.joueur.getId());
          break;
        }

        if(messTab[0].equals("GLIST?")){
          if(this.jeu.getPartie()==false){
          //  finDeJeu(this, pw);
            break;
          }
          listeJoueursPosition(this, pw, br);
        }




      }

      pw.println("BYE***");
      pw.flush();
      pw.close();
      br.close();
      this.socket.close();

    }catch(Exception e){

    }
  }
}
