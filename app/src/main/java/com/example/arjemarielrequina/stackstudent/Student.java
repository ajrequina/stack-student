package com.example.arjemarielrequina.stackstudent;

/**
 * Created by Arjemariel Requina on 12/8/2016.
 */

// The Class that used in populating students' data.
  // This class have the necessary variables that are relevant to the students
public class Student {
  public int id;
  public String firstname;
  public String middlename;
  public String lastname;
  public String email;
  public int contact;
  public String fullname;
  public int score = 0;
  public int count = 0;
  public String checkname;

  public Student(int id, String firstname, String middlename, String lastname, String email, int contact){
    this.id = id;
    this.firstname = firstname;
    this.middlename = middlename;
    this.lastname = lastname;
    this.email = email;
    this.contact = contact;
    lastname = lastname.toLowerCase();
    lastname = lastname.substring(0,1).toUpperCase() + lastname.substring(1);
    firstname = firstname.toLowerCase();
    firstname = firstname.substring(0,1).toUpperCase() + firstname.substring(1);
    middlename = middlename.toLowerCase();
    middlename = middlename.substring(0,1).toUpperCase() + middlename.substring(1);
    this.fullname = lastname + ", " + firstname + " " + middlename;
    this.checkname = firstname + middlename + lastname;
    this.checkname = this.checkname.toLowerCase();
  }
  public void accumScore(int score){
    this.score += score;
    this.count++;
  }

  public double aveScore(){
    if(count == 0 ){
      return count;
    } else {
      return score / count;
    }
  }
  public void setScore(int score){
    this.score = score;
  }
}
