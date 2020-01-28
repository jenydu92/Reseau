import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class ServeurFonctions {

  private static int numeroDeGame = 0;
  private static int numPort = 4000;
  private static int addresseIp = 1;


/****************Requetes avant partie commence**************************/

  public void nouveauJoueurConnecte(ServeurThread s, PrintWriter pw, BufferedReader br){

    try{

      int nombrePartie = 0;
      for(int i = 0; i < s.listePartie.size(); i++){
        if(s.listePartie.get(i).getCommencee()==false)
          nombrePartie++;
      }
      String nbPartieS = ""+nombrePartie;
      pw.println("GAMES "+littleEndian(nbPartieS)+"***");
      pw.flush();
      int nombreJoueur;
      for(int i = 0; i < nombrePartie; i++){
        if(s.listePartie.get(i).getCommencee()==false){
          int partie = s.listePartie.get(i).getNumero();
          String partieS = ""+partie;
          nombreJoueur = s.listePartie.get(i).getNbJoueur();
          String nbJoueurS = ""+nombreJoueur;
          pw.println("GAMES "+littleEndian(partieS)+" "+littleEndian(nbJoueurS)+"***");
          pw.flush();
        }
      }

    }catch(Exception e){
  //    System.out.println(e);
  //    e.printStackTrace();
    }
  }

  public void creationPartie(ServeurThread s, PrintWriter pw, BufferedReader br, String id, int port){

    try{
      if(s.joueur == null){
        numeroDeGame++;
        numPort++;
        addresseIp++;
        Games jeu = new Games(numeroDeGame, numPort, addresseIp);
        Joueur joueur = new Joueur(id, port);
        joueur.setGame(numeroDeGame);
        jeu.ajouterJoueur(joueur);
        s.listePartie.add(jeu);

        System.out.println(s.listePartie.size());
        String num = ""+jeu.getNumero();
        pw.println("REGOK "+littleEndian(num)+"***");
        pw.flush();
        s.jeu = jeu;
        s.joueur = joueur;

      }

      else if(s.joueur.getGame() != 0 ){

        pw.println("REGNO***");
        pw.flush();
      }else {

        numeroDeGame++;
        numPort++;
        addresseIp++;
        Games jeu = new Games(numeroDeGame, numPort, addresseIp);
        Joueur joueur = new Joueur(id, port);
        joueur.setGame(numeroDeGame);
        jeu.ajouterJoueur(joueur);
        s.listePartie.add(jeu);

        String num = ""+jeu.getNumero();

        pw.println("REGOK "+littleEndian(num)+"***");
        pw.flush();
        s.jeu = jeu;
        s.joueur = joueur;

      }
    }catch(Exception e){

    }
  }

  public int trouverPartie(ServeurThread s, int id){
    while(true){
      for(int i = 0; i < s.listePartie.size(); i++){
        if(s.listePartie.get(i).getNumero() == id){
          return i;
        }
      }
      return -1;

    }
  }

  public synchronized void rejoindrePartie(ServeurThread s, PrintWriter pw, BufferedReader br, String id, int port, int partieChoisie){
    try{
      if(s.joueur == null){
        int indice = trouverPartie(s, partieChoisie);

        Joueur joueur = new Joueur(id, port);
        joueur.setGame(partieChoisie);


        s.listePartie.get(indice).ajouterJoueur(joueur);
        int nbJ = s.listePartie.get(indice).getNbJoueur();

        String num = ""+s.listePartie.get(indice).getNumero();

        pw.println("REGOK "+littleEndian(num)+"***");
        pw.flush();
        s.jeu = s.listePartie.get(indice);

        s.joueur = joueur;
      }
      else if(s.joueur.getGame() != 0 ){
        pw.println("REGNO***");
        pw.flush();
      }else {
        int indice = trouverPartie(s, partieChoisie);

        Joueur joueur = new Joueur(id, port);
        joueur.setGame(partieChoisie);


        s.listePartie.get(indice).ajouterJoueur(joueur);
        int nbJ = s.listePartie.get(indice).getNbJoueur();

        String num = ""+s.listePartie.get(indice).getNumero();

        pw.println("REGOK "+littleEndian(num)+"***");
        pw.flush();
        s.jeu = s.listePartie.get(indice);

        s.joueur = joueur;
      }
    }catch(Exception e){

    }
  }


  public void joueursInscritsPartie(ServeurThread s, PrintWriter pw, BufferedReader br, Games games, int id){
    int numPartie = trouverPartie(s, id);
    if(numPartie == -1){
      pw.println("DUNNO***");
      pw.flush();
    }else{
      int nbJoueur = s.listePartie.get(numPartie).getNbJoueur();
      String idS = ""+id;
      String nbJoueurS = ""+nbJoueur;
      pw.println("LIST! "+littleEndian(idS)+" "+littleEndian(nbJoueurS)+"***");
      pw.flush();
      for(int i = 0; i < nbJoueur; i++){
        String mess = "PLAYER "+s.listePartie.get(numPartie).listeJoueur.get(i).getId()+"***";
        pw.println(mess);
        pw.flush();
      }
    }
  }


  public void desincrireJoueur(ServeurThread s, PrintWriter pw, BufferedReader br, Games games, Joueur jo){
    if(jo == null){
      pw.println("DUNNO***");
      pw.flush();
    }
    else if(jo.getId().equals(" ")){
      pw.println("DUNNO***");
      pw.flush();
    }
    else{
      games.listeJoueur.remove(jo);
      games.setNbJoueur(games.listeJoueur.size());
      jo.setGame(0);
      int num = games.getNumero();
      String numS = ""+num;
      pw.println("UNREGOK "+littleEndian(numS)+"***");
      pw.flush();
    }
    if(games.listeJoueur.size()==0){
      s.listePartie.remove(games);
    }
  }
  public void tailleLabyrintheDemande(ServeurThread s, PrintWriter pw, BufferedReader br, int i){
    int indice = trouverPartie(s, i);
    if (indice == -1){
      pw.println("DUNNO***");
      pw.flush();
    }
    else{
      int w = s.listePartie.get(indice).laby[0].length;
      int h = s.listePartie.get(indice).laby.length;
      String iS = ""+i;
      String wS = ""+w;
      String hS = ""+h;
      pw.println("SIZE! "+littleEndian(iS)+" "+littleEndian(hS)+" "+littleEndian(wS)+"***");
      pw.flush();
    }
  }

  public void partiesNonCommencees(ServeurThread s, PrintWriter pw, BufferedReader br){
    try{
      int compteur = 0;
      for(int i = 0; i<s.listePartie.size(); i++){
        if(!(s.listePartie.get(i).getCommencee()))
        compteur++;
      }
      pw.println("GAMES "+compteur+"***");
      pw.flush();
      for(int j = 0; j<s.listePartie.size(); j++){
        if(!(s.listePartie.get(j).getCommencee())){
          String numPartieS = ""+s.listePartie.get(j).getNumero();
          String numJoueurS = ""+s.listePartie.get(j).getNbJoueur();
          pw.println("GAME "+littleEndian(numPartieS)+" "+littleEndian(numJoueurS)+"***");
          pw.flush();
        }
      }
    }catch(Exception e){

    }
  }

  /*************place les joueurs dans le labyrinthe de façon aleatoire, dans des cases libres********/

    public synchronized void mettreJoueursLaby(ServeurThread s, PrintWriter pw, BufferedReader br, int id){
      int indice = trouverPartie(s, id);
        Games g = s.listePartie.get(indice);

        int i;
        int j;


        while(true){

          i = (int)(Math.random() * (s.jeu.laby.length));

          j = (int)(Math.random() * s.jeu.laby[0].length);

          if(s.listePartie.get(indice).laby[i][j].getType()=="libre"){
            g.laby[i][j].setType("joueur");

            g.laby[i][j].setJoueur(s.joueur);
            s.joueur.setX(i);
            s.joueur.setY(j);
            break;


          }

        }

    }

  /*************affiche le laby selon le type de la case****************/

  public void afficheLaby(ServeurThread s, PrintWriter pw, BufferedReader br){
    for(int i=0; i < s.jeu.laby.length; i++){
      System.out.println();
      for(int j = 0; j < s.jeu.laby[i].length; j++){
        if(s.jeu.laby[i][j].getType().equals("mur"))
          System.out.print("0");
        if(s.jeu.laby[i][j].getType().equals("libre"))
          System.out.print("1");
        if(s.jeu.laby[i][j].getType().equals("fantome"))
          System.out.print("2");
        if(s.jeu.laby[i][j].getType().equals("joueur"))
          System.out.print("3");

      }
    }
  }

  public void envoieMessagePerso(ServeurThread s, String id,  String messa, PrintWriter pw){


    try{
      int indice = s.jeu.trouverJoueur(id);
      if(indice == -1){
        pw.println("NOSEND***");
        pw.flush();
      }else{
        Joueur test = s.jeu.listeJoueur.get(indice);
        DatagramSocket dso=new DatagramSocket();
        byte[]data;
        String message="MESP "+s.joueur.getId()+" "+messa+"+++";
        data=message.getBytes();
        DatagramPacket paquet=new DatagramPacket(data,data.length, InetAddress.getByName("localhost"),test.getPort());
        dso.send(paquet);
        System.out.println("Message envoyé");
        pw.println("SEND!***");
        pw.flush();
      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }



  public void envoieMessageGeneral(ServeurThread s, String message, PrintWriter pw){
    try{
      String messagePourEnvoie = "MESA "+s.joueur.getId()+" "+message+"+++";
      byte[]data=messagePourEnvoie.getBytes();
      DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
      s.jeu.getDso().send(paquet);
      System.out.println("le serveur multi a envoyé un paquet");
      pw.println("ALL!***");
      pw.flush();
    }
   catch(Exception e){
    e.printStackTrace();
  }
  }



/*
  public void finDeJeu(ServeurThread s, PrintWriter pw){
    pw.println("BYE***");
    pw.flush();
  }*/
  /********************met l'attribut pret du joueur à true*****************/

    public void joueurPret(Joueur j){
      j.setPret(true);
    }

    public boolean partiePrete(Games jeu){

      for(int i = 0; i<jeu.listeJoueur.size(); i++){
        if(!(jeu.listeJoueur.get(i).getPret())){
          return false;
        }
      }
      return true;
    }
    /************supprimer le joueur de la liste et du laby****************/

      public void supprimerJoueur(ServeurThread s, PrintWriter pw, BufferedReader br, String id){
        try{
          int posX = s.joueur.getX();
          int posY = s.joueur.getY();
          s.jeu.laby[posX][posY].setType("libre");
          s.jeu.laby[posX][posY].setJoueur(null);

          int indice = s.jeu.trouverJoueur(id);
          s.jeu.listeJoueur.remove(indice);
      /*    pw.println("BYE***");
          pw.flush();
          pw.close();
          br.close();
          s.socket.close();*/
        }catch(Exception e){

        }
      }

      public Joueur trouverJoueurGeneral(ServeurThread s, String id){
        for(int i = 0; i < s.listePartie.size(); i++){
          for(int j = 0; j < s.listePartie.get(i).listeJoueur.size(); j++){
            if(s.listePartie.get(i).listeJoueur.get(j).getId().equals(id))
              return s.listePartie.get(i).listeJoueur.get(j);
          }
        }
        return null;
      }

      public void listeJoueursPosition(ServeurThread s, PrintWriter pw, BufferedReader br){
        try{
          int nbJoueur = s.jeu.listeJoueur.size();
          String nbJoueurS = ""+nbJoueur;
          pw.println("GLIST! "+littleEndian(nbJoueurS)+"***");
          pw.flush();
          for(int i = 0; i< nbJoueur; i++){
            pw.println("GPLAYER "+s.jeu.listeJoueur.get(i).getId()+" "+completer30(s.jeu.listeJoueur.get(i).getX())+" "+completer30(s.jeu.listeJoueur.get(i).getY())+" "+completer40(s.jeu.listeJoueur.get(i).getScore())+"***");
            pw.flush();
          }
        }catch(Exception e){

        }
      }


  /**********************DEPLACEMENT**************************/

  public int positionMonter(ServeurThread s, int x, int y){
    if(s.jeu.laby[x][y].getType().equals("joueur"))
      return positionMonter(s, x+1, y);
    return x;
  }


  public void monter(ServeurThread s, PrintWriter pw, BufferedReader br, int i, int x, int y){
    try{
      int score = s.joueur.getScore();
      int scoreTest = score;
      boolean test = true;
      int compteur = i;
      int xProvisoir= x;
      int xPosAvant = x;
      s.jeu.laby[x][y].setType("libre");
      s.jeu.laby[x][y].setJoueur(null);
      for(int a = 0; a<=i; a++){
        xProvisoir = xProvisoir - 1;

        if(xProvisoir <0){
          int nX = positionMonter(s,xProvisoir + 1,y);
          s.joueur.setScore(score);
          s.joueur.setX(nX);
          s.jeu.laby[nX][y].setType("joueur");
          s.jeu.laby[nX][y].setJoueur(s.joueur);
          break;

        }
        else if(a==i-1){
          if(s.jeu.laby[xProvisoir][y].getType().equals("mur")){
            int nouveauX = positionMonter(s, xProvisoir+1, y);
            s.joueur.setScore(score);
            s.joueur.setX(nouveauX);
            s.jeu.laby[nouveauX][y].setType("joueur");
            s.jeu.laby[nouveauX][y].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[xProvisoir][y].getType().equals("fantome")){
            score++;
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);
            s.jeu.laby[xProvisoir][y].setType("joueur");
            s.jeu.laby[xProvisoir][y].setJoueur(s.joueur);
            s.joueur.setX(xProvisoir);
            s.joueur.setScore(score);
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

            break;
          }
          else if(s.jeu.laby[xProvisoir][y].getType().equals("joueur")){
            int nouvoX = positionMonter(s, xProvisoir+1, y);
            s.joueur.setScore(score);
            s.joueur.setX(nouvoX);
            s.jeu.laby[nouvoX][y].setType("joueur");
            s.jeu.laby[nouvoX][y].setJoueur(s.joueur);
            break;
          }
          else{
            s.jeu.laby[xProvisoir][y].setType("joueur");
            s.jeu.laby[xProvisoir][y].setJoueur(s.joueur);
            s.joueur.setX(xProvisoir);
            s.joueur.setScore(score);
            break;
          }
        }else{
          if(s.jeu.laby[xProvisoir][y].getType().equals("mur")){
            int nouveauX = positionMonter(s, xProvisoir+1, y);
            s.joueur.setScore(score);
            s.joueur.setX(nouveauX);
            s.jeu.laby[nouveauX][y].setType("joueur");
            s.jeu.laby[nouveauX][y].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[xProvisoir][y].getType().equals("fantome")){
            score++;
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            s.jeu.laby[xProvisoir][y].setType("libre");
            System.out.println("MULTI DIFF");
            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
          }
        }
      }
      if(scoreTest<score){
        pw.println("MOF "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+" "+s.joueur.getScore()+"***");
        pw.flush();
      }
      else{
        pw.println("MOV "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"***");
        pw.flush();
      }
    }catch(Exception e){

    }

  }


  public int positionDescendre(ServeurThread s, int x, int y){
    if(s.jeu.laby[x][y].getType().equals("joueur"))
      return positionDescendre(s, x-1, y);
    return x;
  }


  public void descendre(ServeurThread s, PrintWriter pw, BufferedReader br, int i, int x, int y){
    try{
      int score = s.joueur.getScore();
      int scoreTest = score;
      boolean test = true;
      int compteur = i;
      int xProvisoir= x;
      int xPosAvant = x;
      s.jeu.laby[x][y].setType("libre");
      s.jeu.laby[x][y].setJoueur(null);
      for(int a = 0; a<=i; a++){
        xProvisoir = xProvisoir + 1;

        if(xProvisoir > s.jeu.laby.length){
          int nX = positionDescendre(s,xProvisoir - 1,y);
          s.joueur.setScore(score);
          s.joueur.setX(nX);
          s.jeu.laby[nX][y].setType("joueur");
          s.jeu.laby[nX][y].setJoueur(s.joueur);
          break;

        }

        else if(a==i-1){
          if(s.jeu.laby[xProvisoir][y].getType().equals("mur")){
            int nouveauX = positionDescendre(s, xProvisoir-1, y);
            s.joueur.setScore(score);
            s.joueur.setX(nouveauX);
            s.jeu.laby[nouveauX][y].setType("joueur");
            s.jeu.laby[nouveauX][y].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[xProvisoir][y].getType().equals("fantome")){
            score++;
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            s.jeu.laby[xProvisoir][y].setType("joueur");
            s.jeu.laby[xProvisoir][y].setJoueur(s.joueur);
            s.joueur.setX(xProvisoir);
            s.joueur.setScore(score);
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

            break;
          }
          else if(s.jeu.laby[xProvisoir][y].getType().equals("joueur")){
            int nouvoX = positionDescendre(s, xProvisoir-1, y);
            s.joueur.setScore(score);
            s.joueur.setX(nouvoX);
            s.jeu.laby[nouvoX][y].setType("joueur");
            s.jeu.laby[nouvoX][y].setJoueur(s.joueur);
            break;
          }
          else{
            s.jeu.laby[xProvisoir][y].setType("joueur");
            s.jeu.laby[xProvisoir][y].setJoueur(s.joueur);
            s.joueur.setX(xProvisoir);
            s.joueur.setScore(score);
            break;
          }
        }else{
          if(s.jeu.laby[xProvisoir][y].getType().equals("mur")){
            int nouveauX = positionDescendre(s, xProvisoir-1, y);
            s.joueur.setScore(score);
            s.joueur.setX(nouveauX);
            s.jeu.laby[nouveauX][y].setType("joueur");
            s.jeu.laby[nouveauX][y].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[xProvisoir][y].getType().equals("fantome")){
            score++;
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            s.jeu.laby[xProvisoir][y].setType("libre");
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

          }
        }
      }
      if(scoreTest<score){
        pw.println("MOF "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+" "+s.joueur.getScore()+"***");
        pw.flush();
      }
      else{
        pw.println("MOV "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"***");
        pw.flush();
      }
    }catch(Exception e){

    }

  }

  public int positionGauche(ServeurThread s, int x, int y){
    if(s.jeu.laby[x][y].getType().equals("joueur"))
      return positionDescendre(s, x, y+1);
    return y;
  }


  public void gauche(ServeurThread s, PrintWriter pw, BufferedReader br, int i, int x, int y){
    try{
      int score = s.joueur.getScore();
      int scoreTest = score;
      boolean test = true;
      int compteur = i;
      int yProvisoir= y;
      int xPosAvant = x;
      s.jeu.laby[x][y].setType("libre");
      s.jeu.laby[x][y].setJoueur(null);
      for(int a = 0; a<i; a++){
        yProvisoir = yProvisoir -1;

        if(yProvisoir <0){
          int nY = positionGauche(s,x,yProvisoir+1);
          s.joueur.setScore(score);
          s.joueur.setY(nY);
          s.jeu.laby[x][nY].setType("joueur");
          s.jeu.laby[x][nY].setJoueur(s.joueur);
          break;

        }
        else if(a==i-1){
          if(s.jeu.laby[x][yProvisoir].getType().equals("mur")){
            int nouveauY = positionGauche(s, x, yProvisoir+1);
            s.joueur.setScore(score);
            s.joueur.setY(nouveauY);
            s.jeu.laby[x][nouveauY].setType("joueur");
            s.jeu.laby[x][nouveauY].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[x][yProvisoir].getType().equals("fantome")){
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            score++;
            s.jeu.laby[x][yProvisoir].setType("joueur");
            s.jeu.laby[x][yProvisoir].setJoueur(s.joueur);
            s.joueur.setY(yProvisoir);
            s.joueur.setScore(score);
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

            break;
          }
          else if(s.jeu.laby[x][yProvisoir].getType().equals("joueur")){
            int nouvoY = positionGauche(s, x,yProvisoir+1);
            s.joueur.setScore(score);
            s.joueur.setY(nouvoY);
            s.jeu.laby[x][nouvoY].setType("joueur");
            s.jeu.laby[x][nouvoY].setJoueur(s.joueur);
            break;
          }
          else{
            s.jeu.laby[x][yProvisoir].setType("joueur");
            s.jeu.laby[x][yProvisoir].setJoueur(s.joueur);
            s.joueur.setY(yProvisoir);
            s.joueur.setScore(score);
            break;
          }
        }else{
          if(s.jeu.laby[x][yProvisoir].getType().equals("mur")){
            int nouveauY = positionGauche(s,x,yProvisoir+1);
            s.joueur.setScore(score);
            s.joueur.setY(nouveauY);
            s.jeu.laby[x][nouveauY].setType("joueur");
            s.jeu.laby[x][nouveauY].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[x][yProvisoir].getType().equals("fantome")){
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            score++;
            s.jeu.laby[x][yProvisoir].setType("libre");
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

          }
        }
      }
      if(scoreTest<score){
        pw.println("MOF "+completer30(s.joueur.getX())+" "+s.joueur.getY()+" "+completer30(s.joueur.getScore())+"***");
        pw.flush();
      }
      else{
        pw.println("MOV "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"***");
        pw.flush();
      }
    }catch(Exception e){

    }
  }

  public int positionDroite(ServeurThread s, int x, int y){
    if(s.jeu.laby[x][y].getType().equals("joueur"))
      return positionDescendre(s, x, y-1);
    return y;
  }


  public void droite(ServeurThread s, PrintWriter pw, BufferedReader br, int i, int x, int y){
    try{
      int score = s.joueur.getScore();
      int scoreTest = score;
      boolean test = true;
      int compteur = i;
      int yProvisoir= y;
      int xPosAvant = x;
      s.jeu.laby[x][y].setType("libre");
      s.jeu.laby[x][y].setJoueur(null);
      for(int a = 0; a<i; a++){

        yProvisoir = yProvisoir +1;
        if(yProvisoir > s.jeu.laby[0].length){
          int nY = positionDroite(s,x,yProvisoir-1);
          s.joueur.setScore(score);
          s.joueur.setY(nY);
          s.jeu.laby[x][nY].setType("joueur");
          s.jeu.laby[x][nY].setJoueur(s.joueur);
          break;

        }
        else if(a==i-1){
          if(s.jeu.laby[x][yProvisoir].getType().equals("mur")){
            int nouveauY = positionDroite(s, x, yProvisoir-1);
            s.joueur.setScore(score);
            s.joueur.setY(nouveauY);
            s.jeu.laby[x][nouveauY].setType("joueur");
            s.jeu.laby[x][nouveauY].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[x][yProvisoir].getType().equals("fantome")){
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            score++;
            s.jeu.laby[x][yProvisoir].setType("joueur");
            s.jeu.laby[x][yProvisoir].setJoueur(s.joueur);
            s.joueur.setY(yProvisoir);
            s.joueur.setScore(score);
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

            break;
          }
          else if(s.jeu.laby[x][yProvisoir].getType().equals("joueur")){
            int nouvoY = positionDroite(s, x,yProvisoir-1);
            s.joueur.setScore(score);
            s.joueur.setY(nouvoY);
            s.jeu.laby[x][nouvoY].setType("joueur");
            s.jeu.laby[x][nouvoY].setJoueur(s.joueur);
            break;
          }
          else{
            s.jeu.laby[x][yProvisoir].setType("joueur");
            s.jeu.laby[x][yProvisoir].setJoueur(s.joueur);
            s.joueur.setY(yProvisoir);
            s.joueur.setScore(score);
            break;
          }
        }else{
          if(s.jeu.laby[x][yProvisoir].getType().equals("mur")){
            int nouveauY = positionDroite(s,x,yProvisoir-1);
            s.joueur.setScore(score);
            s.joueur.setY(nouveauY);
            s.jeu.laby[x][nouveauY].setType("joueur");
            s.jeu.laby[x][nouveauY].setJoueur(s.joueur);
            break;
          }
          else if(s.jeu.laby[x][yProvisoir].getType().equals("fantome")){
            s.jeu.setNbFantome(s.jeu.getNbFantome()-1);

            score++;
            s.joueur.setScore(score);
            s.jeu.laby[x][yProvisoir].setType("libre");
            System.out.println("MULTI DIFF");

            String messageF = "SCOR "+s.joueur.getId()+" "+completer40(s.joueur.getScore())+" "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"+++";
            byte[]data=messageF.getBytes();
            DatagramPacket paquet=new DatagramPacket(data,data.length,s.jeu.getIa());
            s.jeu.getDso().send(paquet);
            System.out.println("le serveur multi a envoyé un paquet");

          }
        }
      }
      if(scoreTest<score){
        pw.println("MOF "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+" "+s.joueur.getScore()+"***");
        pw.flush();
      }
      else{
        pw.println("MOV "+completer30(s.joueur.getX())+" "+completer30(s.joueur.getY())+"***");
        pw.flush();
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }

/**********************Fonctions octets*********************/


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
    System.out.println("j");
  String a1 = a.substring(0,1);
  System.out.println("d");

  String a2 = a.substring(1,2);
  System.out.println("g");

  String a3 = a.substring(2,3);
  System.out.println("m");

  String fi;
  System.out.println(a1+" "+a2+" "+a3);
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
  public String completer40(int a){
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

}
