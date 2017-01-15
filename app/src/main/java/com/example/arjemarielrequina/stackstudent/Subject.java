package com.example.arjemarielrequina.stackstudent;

/**
 * Created by Arjemariel Requina on 12/9/2016.
 */

// The custom object class that is used in populating subjects data
public class Subject {
  public int id;
  public String title;
  public String details;

  public Subject(int id, String title, String details){
    this.id = id;
    this.title = title;
    this.details = details;
  }
}
