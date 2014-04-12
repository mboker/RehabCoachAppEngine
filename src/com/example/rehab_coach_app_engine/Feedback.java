package com.example.rehab_coach_app_engine;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Feedback {

  @Id
  private String id;
  private int response1;
  private int response2;
  private int response3;
  private int response4;
  private int response5;
  private int activity_id;
  private String user_id;
  
  public Feedback(){}
  
  public String getId(){
	  return id;
  }
  
  
  public void setActivityID(int id){
	  activity_id = id;
  }
  
  public void setUserID(String id){
	  user_id = id;
  }
  
  public void setResponse1(int val){
	  response1 = val;
  }
  
  public void setResponse2(int val){
	  response2 = val;
  }
  
  public void setResponse3(int val){
	  response3 = val;
  }
  
  public void setResponse4(int val){
	  response4 = val;
  }
  
  public void setResponse5(int val){
	  response5 = val;
  }
  
  public int getResponse1(){
	  return response1;
  }
  
  public int getResponse2(){
	  return response2;
  }
  
  public int getResponse3(){
	  return response3;
  }
  
  public int getResponse4(){
	  return response4;
  }
  
  public int getResponse5(){
	  return response5;
  }
  
}
