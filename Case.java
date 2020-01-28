import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;



public class Case{

  private String type;
  private Joueur joueur;

  public Case(String type, Joueur joueur){
    this.type = type;
    this.joueur = joueur;
  }

  public String getType(){
    return this.type;
  }

  public void setType(String type){
    this.type = type;
  }

  public Joueur getJoueur(){
    return this.joueur;
  }

  public void setJoueur(Joueur joueur){
    this.joueur = joueur;
  }

}
