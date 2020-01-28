import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

//****************************************************************************

public class Games{

  private int nombreJoueur;
  private int numero; //ID de la game
  ArrayList <Joueur> listeJoueur;
  boolean commencee = false;
  private int nbFantome;
  public Case laby [][];
  //private String ipDiffusion;
  //private int portUpd;
  private boolean joueursDsLaby;
  private DatagramSocket dso;
  private InetSocketAddress ia;
  private int numPort;
  private String addresseIp;
  private int deplacement;
  private boolean partie = true;

  public Games(int numero, int numPort, int address){
    try{
      this.numero = numero;
      this.nombreJoueur = 0;
      this.listeJoueur = new ArrayList <Joueur> ();
      this.nbFantome = 8;

      this.laby = generateurLaby();
    //  this.joueursDsLaby = false;
      this.dso=new DatagramSocket();
      this.ia=new InetSocketAddress("225.225.225."+address,numPort);
  //    this.ia=new InetSocketAddress("225.1.2.1",4001);

  //    this.ia=new InetSocketAddress("225.225.225.225",4000);
  //    this.numPort = 4000;

      this.numPort = numPort;
      this.addresseIp = "225.225.225."+address;
//    this.addresseIp = "225.1.2.1";

    }catch(Exception e){
      //    System.out.println(e);
      //    e.printStackTrace();
    }
  }




  /*  public String getIpDiffusion(){
      return this.ipDiffusion;
    }

    public void setIpDiffusion(String ip){
      this.ipDiffusion = ip;
    }
    public int getPortUpd(){
      return this.portUpd;
    }

    public void setPortUdp(int port){
      this.portUpd = port;
    }*/
/****************GETTEURS ET SETTEURS*********************/

      public int getNbFantome(){
        return this.nbFantome;
      }

      public void setNbFantome(int f){
        this.nbFantome = f;
      }

      public void partieCommence(){
        this.commencee = true;
      }
      public boolean getCommencee(){
        return this.commencee;
      }

      public void setPartie(boolean p){
        this.partie = p;
      }
      public boolean getPartie(){
        return this.partie;
      }

      public boolean getJoueursDsLaby(){
        return this.joueursDsLaby;
      }

      public void setJoueursDsLaby(boolean b){
        this.joueursDsLaby = b;

      }
    public int getNumero(){
      return this.numero;
    }

    public void setNbJoueur(int n){
      this.nombreJoueur = n;
    }

  public int getNumPort(){
    return this.numPort;
  }

  public void setNumPort(int num){
    this.numPort = num;
  }
  public int getDeplacement(){
    return this.deplacement;
  }

  public void setDeplacement(int num){
    this.deplacement = num;
  }

  public String getAddresseIp(){
    return this.addresseIp;
  }

  public void setAddresseIp(String a){
    this.addresseIp = a;
  }


    public DatagramSocket getDso(){
      return this.dso;
    }

    public void setDso(DatagramSocket dso){
      this.dso = dso;
    }

    public InetSocketAddress getIa(){
      return this.ia;
    }

    public void setIa(InetSocketAddress ia){
      this.ia = ia;
    }


  public Case[][] getLaby(){
    return this.laby;
  }
  public int getNbJoueur(){
    return this.nombreJoueur;
  }

  /****************Generation des labyrinthes***********************/

    public Case[][] generateurLaby(){
      //Case tab [][]=new Case[][];
      Case mur = new Case("mur", null);
      Case libre = new Case("libre", null);
      Case fantome = new Case("fantome", null);
      int lab[][];
      int nbRandom = (int)(Math.random() * 4)+1;
  //  int nbRandom = 4;

      System.out.println("nbr random : "+nbRandom);
      lab  = generateurLabyVide(nbRandom);
      lab = labyAvecFantome(lab);
      System.out.println("fantome ajouté");
      Case tab [][]=new Case[lab.length][lab[0].length];

      for(int i = 0; i < lab.length; i++){
        System.out.println();
        for(int j = 0; j < lab[i].length; j++){
          System.out.print(lab[i][j]);
          if(lab[i][j]==0){
            mur = new Case("mur", null);
            tab[i][j]=mur;
          }
          if(lab[i][j]==1){
            libre = new Case("libre", null);
            tab[i][j]=libre;
          }
          if(lab[i][j]==2){
            fantome = new Case("fantome", null);
            tab[i][j]=fantome;
          }
        }
      }
      System.out.println("objet ajouté");

      return tab;
    }

    public int[][] generateurLabyVide(int nb){
      if(nb==1){
         int tab[][] = {{0,0,0,0,0,0,0,0},{1,1,1,1,1,1,1,0},{0,1,0,1,0,0,1,0},{0,1,1,1,1,1,1,0},{0,0,0,1,0,0,1,0}, {0,1,0,1,0,0,1,1}, {0,1,1,1,1,0,0,0}, {0,0,0,0,0,0,0,0}};
         return tab;
      }
      else if(nb==2){
        int tab[][] = {{0,1,0,0,0,0,0},{0,1,0,0,1,1,0}, {0,1,1,1,1,0,0}, {0,0,1,0,0,1,0}, {0,1,1,1,1,1,0}, {0,0,1,0,0,1,0}, {0,0,0,0,0,1,0} };
        return tab;
      }
      else if(nb==3){
        int tab[][] = {{0,0,1,0,0,0,0,0,0,0}, {0,0,1,0,1,1,0,0,0,0}, {0,1,1,0,1,0,0,1,1,1}, {0,1,0,0,1,1,1,1,0,0}, {0,1,1,1,1,0,1,0,0,0}, {0,0,0,0,0,0,0,0,0,0}};
        return tab;
      }
      else if(nb == 4){
        int tab[][] = {{0,0,1,0,0,0,0,0,0,0,0}, {0,0,1,0,1,1,1,0,0,0,0}, {0,0,1,0,0,1,0,0,1,0,0},{0,0,1,0,0,1,0,0,1,0,0}, {0,0,0,0,0,1,0,0,0,0,0}, {0,0,1,0,0,1,0,0,1,0,0},{0,0,1,1,1,1,1,1,1,0,0},{0,0,0,0,0,0,0,1,0,0,0},{0,0,1,1,1,1,1,1,1,1,0},{0,0,1,0,0,1,0,0,0,0,0},{0,0,1,0,0,1,1,1,0,0,0},{0,0,1,0,0,0,0,0,0,0,0}};
        return tab;
    }
      else
        return null;
    }


    public int[][] labyAvecFantome(int tab[][]){
      int fantome = this.getNbFantome();
      System.out.println("fantome :"+fantome);
      int i;
      int j;
      while(fantome > 0){
        i = (int)(Math.random() * tab.length);
        j = (int)(Math.random() * tab[0].length);
        if(tab[i][j]==1){
          tab[i][j]=2;
          fantome --;
        }
      }
      return tab;

    }

/****************Fonctions liés aux deplacements*********************/


    public void deplacementFantomeDansLaby(){
      int fantome = this.getNbFantome();
      for(int i = 0; i < this.laby.length; i++){
        for(int j = 0; j < this.laby[i].length; j++){
          if(laby[i][j].getType().equals("fantome")){
            this.laby[i][j].setType("libre");
          }
        }
      }
      for(int i = 0; i < this.laby.length; i++){
        for(int j = 0; j < this.laby[i].length; j++){
          if(laby[i][j].getType().equals("fantome")){
            this.laby[i][j].setType("libre");
          }
        }
      }
        int i;
        int j;
        while(fantome > 0){
          i = (int)(Math.random() * this.laby.length);
          j = (int)(Math.random() * this.laby[0].length);
          if(this.laby[i][j].getType().equals("libre")){
            this.laby[i][j].setType("fantome");
            try{
              String message = "FANT "+completer30(i)+" "+completer30(j)+"+++";
              byte[]data=message.getBytes();
              DatagramPacket paquet=new DatagramPacket(data,data.length,this.getIa());
              this.getDso().send(paquet);
              fantome --;
            }
          catch(Exception e){
            //    System.out.println(e);
            //    e.printStackTrace();
          }
          }
        }

    }







  public void incrementeDeplacement(){
    this.setDeplacement(1+this.getDeplacement());
  }

  public void mouvementFantome(){
    if(this.getDeplacement()%5 == 0){
      deplacementFantomeDansLaby();

    }
  }

/************************Fonctions liées à la partie******************/


  public void ajouterJoueur(Joueur j){

    this.listeJoueur.add(j);
    this.nombreJoueur +=1;

  }
  public Joueur gagnant(){
    Joueur jo = this.listeJoueur.get(0);
    for(int i = 0; i < this.listeJoueur.size(); i++){
      if(jo.getScore()<this.listeJoueur.get(i).getScore())
        jo = this.listeJoueur.get(i);
    }
    return jo;
  }

  public int trouverJoueur(String id){
    for(int i = 0; i < this.listeJoueur.size(); i++){
      if((this.listeJoueur.get(i).getId()).equals(id))
        return i;
    }
    return -1;
  }
  /*public void gamesPlusUn(){
    this.numero = this.numero + 1;
  }*/




  /*public boolean partiePrete(){
    for(int i = 0; i < this.listeJoueur.size(); i++){
      System.out.println("voici le joueur de la partie et son etat : "+this.listeJoueur.get(i).getId()+" "+this.listeJoueur.get(i).getPret());
      if(!(this.listeJoueur.get(i).getPret()))
        return false;
    }
    return true;
  }*/





/**********************Fonctions completer octet*************************/


  public String completer30(int a){
    String a2 = ""+a;
  if(a>=0 && a < 10){
    String a1 = "00"+a;
    return a1;
  }else if(a > 9 && a < 100){
    String a1 = "0"+a;
    return a1;
  }else{
    return a2;
  }
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
}
