import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

//****************************************************************************

public class Joueur{

  private String id;
  private int port;
  private boolean pret;
  private int score;
  private int y;
  private int x;
  private int game;


  public Joueur(String id, int port){
    this.id = id;
    this.port = port;
    this.score = 0;
    this.pret = false;
    }

  public String getId(){
    return this.id;
  }

  public void setId(String id){
    this.id = id;
  }


  public int getGame(){
    return this.game;
  }

  public void setGame(int g){
    this.game = g;
  }

  public int getX(){
    return this.x;
  }

  public void setX(int x){
    this.x = x;
  }

  public int getY(){
    return this.y;
  }

  public void setY(int y){
    this.y = y;
  }
  public boolean getPret(){
    return this.pret;
  }

  public void setPret(boolean pret){
    this.pret = pret;
  }

  public void setScore(int n){
    this.score = n;
  }

  public int getScore(){
    return this.score;
  }

  public int getPort(){
    return this.port;
  }

  public void setPort(int port){
    this.port = port;
  }



}
