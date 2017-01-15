package com.example.arjemarielrequina.stackstudent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.arjemarielrequina.stackstudent.StackContract.*;

import java.util.ArrayList;

public class AddStudent extends AppCompatActivity {
  ArrayList<Student> students;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_student);
    setTitle("Add Student");
    final EditText firstname = (EditText) findViewById(R.id.editFname);
    final EditText middlename = (EditText) findViewById(R.id.editMname);
    final EditText lastname = (EditText) findViewById(R.id.editLname);
    final EditText email = (EditText) findViewById(R.id.editEmail);
    final EditText contact = (EditText) findViewById(R.id.editContact);
    getAllStudents();

    Button addBtn = (Button) findViewById(R.id.editStudentBtn);
    Button backBtn = (Button) findViewById(R.id.backBtn);
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(AddStudent.this, StudentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });


    addBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean proceed = true;
        if(firstname.getText().toString().isEmpty()){
          Toast.makeText(AddStudent.this,"Firstname is Required!" , Toast.LENGTH_SHORT).show();
          proceed = false;
        } if(proceed && lastname.getText().toString().isEmpty()){
          proceed = false;
          Toast.makeText(AddStudent.this,"Lastname is Required!" , Toast.LENGTH_SHORT).show();
        } if(proceed && email.getText().toString().isEmpty() && contact.getText().toString().isEmpty()){
          proceed = false;
          Toast.makeText(AddStudent.this,"Email Address or Contact Number is Required!" , Toast.LENGTH_SHORT).show();
        } if(proceed){

          boolean check = true;
          String checkname = firstname.getText().toString() + middlename.getText().toString() + lastname.getText().toString();
          checkname = checkname.toLowerCase();
          String text = "";
          for(Student s : students){
             if(checkname.equals(s.checkname)){
               check = false;
               text = "The Name Already Exists!";
               break;
             }
             if(!email.getText().toString().isEmpty() && email.getText().toString().equals(s.email)){
               check = false;
               text = "The Email Already Exists!";
               break;
             }
             if(!contact.getText().toString().isEmpty() && contact.getText().toString().equals(s.contact)){
               text = "The Contact Number Already Exists!";
               check = false;
               break;
             }
          }

          if(!check){
            Toast.makeText(AddStudent.this,text , Toast.LENGTH_SHORT).show();
          } else {
            ContentValues values = new ContentValues();
            values.put(StudentEntry.COLUMN_FIRSTNAME, firstname.getText().toString());
            values.put(StudentEntry.COLUMN_MIDNAME, middlename.getText().toString());
            values.put(StudentEntry.COLUMN_LASTNAME, lastname.getText().toString());
            values.put(StudentEntry.COLUMN_EMAIL, email.getText().toString());
            values.put(StudentEntry.COLUMN_CONTACT, contact.getText().toString());


            Uri newUri = getContentResolver().insert(StudentEntry.CONTENT_URI, values);
            if(newUri != null){
              Toast.makeText(AddStudent.this,"Student Successfully Added!" , Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(AddStudent.this, StudentActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
            } else {
              Toast.makeText(AddStudent.this,"An Error Occurred!" , Toast.LENGTH_SHORT).show();
            }

          }
        }
      }
    });
  }
  public void getAllStudents(){
    String[] projection = {
            StudentEntry._ID,
            StudentEntry.COLUMN_FIRSTNAME,
            StudentEntry.COLUMN_MIDNAME,
            StudentEntry.COLUMN_LASTNAME,
            StudentEntry.COLUMN_EMAIL,
            StudentEntry.COLUMN_CONTACT
    };
    Cursor cursor = getContentResolver().query(StudentEntry.CONTENT_URI, projection, null, null, null);
    students = new ArrayList<>();
    try{
      int idIdx = cursor.getColumnIndex(StudentEntry._ID);
      int fnameIdx = cursor.getColumnIndex(StudentEntry.COLUMN_FIRSTNAME);
      int mnameIdx = cursor.getColumnIndex(StudentEntry.COLUMN_MIDNAME);
      int lnameIdx = cursor.getColumnIndex(StudentEntry.COLUMN_LASTNAME);
      int emailIdx = cursor.getColumnIndex(StudentEntry.COLUMN_EMAIL);
      int contactIdx = cursor.getColumnIndex(StudentEntry.COLUMN_CONTACT);
      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
        Student stud = new Student(cursor.getInt(idIdx), cursor.getString(fnameIdx), cursor.getString(mnameIdx), cursor.getString(lnameIdx), cursor.getString(emailIdx), cursor.getInt(contactIdx));
        //Toast.makeText(AddStudent.this, String.valueOf(stud.toString()), Toast.LENGTH_SHORT).show();
        students.add(stud);
      }
    } finally {
      cursor.close();
    }
  }

}
