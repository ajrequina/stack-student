package com.example.arjemarielrequina.stackstudent;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditStudent extends AppCompatActivity {
  int id;
  String firstname;
  String middlename;
  String lastname;
  String email;
  int contact;

  EditText firstname_text;
  EditText middlename_text;
  EditText lastname_text;
  EditText email_text;
  EditText contact_text;
  Button editBtn;
  Button backBtn;

  ArrayList<Student> students;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_student);
    getExtras();
    initAllViews();
    getAllStudents();

    firstname_text.setText(firstname);
    middlename_text.setText(middlename);
    lastname_text.setText(lastname);
    email_text.setText(email);

    // The button listener for updating the student info
    editBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        boolean proceed = true;
        if (firstname_text.getText().toString().isEmpty()) {
          Toast.makeText(EditStudent.this, "Firstname is Required!", Toast.LENGTH_SHORT).show();
          proceed = false;
        }
        if (proceed && lastname_text.getText().toString().isEmpty()) {
          proceed = false;
          Toast.makeText(EditStudent.this, "Lastname is Required!", Toast.LENGTH_SHORT).show();
        }
        if (proceed && email_text.getText().toString().isEmpty() && contact_text.getText().toString().isEmpty()) {
          proceed = false;
          Toast.makeText(EditStudent.this, "Email Address or Contact Number is Required!", Toast.LENGTH_SHORT).show();
        }
        if (proceed) {

          boolean check = true;
          String checkname = firstname_text.getText().toString() + middlename_text.getText().toString() + lastname_text.getText().toString();
          checkname = checkname.toLowerCase();

          String text = "";
          for (Student s : students) {
            if (s.id != id && checkname.equals(s.checkname)) {

              check = false;
              text = "The Name Already Exists!";
              break;
            }
            if (s.id != id && !email_text.getText().toString().isEmpty() && email_text.getText().toString().equals(s.email)) {
              check = false;
              text = "The Email Already Exists!";
              break;
            }
            if (s.id != id && !contact_text.getText().toString().isEmpty() && contact_text.getText().toString().equals(s.contact)) {
              text = "The Contact Number Already Exists!";
              check = false;
              break;
            }
          }

          if (!check) {
            Toast.makeText(EditStudent.this, text, Toast.LENGTH_SHORT).show();
          } else {
            Uri uri = ContentUris.withAppendedId(StackContract.StudentEntry.CONTENT_URI, id);
            ContentValues values = new ContentValues();
            values.put(StackContract.StudentEntry.COLUMN_FIRSTNAME, firstname_text.getText().toString());
            values.put(StackContract.StudentEntry.COLUMN_MIDNAME, middlename_text.getText().toString());
            values.put(StackContract.StudentEntry.COLUMN_LASTNAME, lastname_text.getText().toString());
            values.put(StackContract.StudentEntry.COLUMN_EMAIL, email_text.getText().toString());
            if(!contact_text.getText().toString().isEmpty()){
              values.put(StackContract.StudentEntry.COLUMN_CONTACT, contact_text.getText().toString());
            }
            int i = getContentResolver().update(uri, values, null, null);
            if (i >= 0) {
              Toast.makeText(EditStudent.this, "Student Information Successfully Updated!", Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(EditStudent.this, StudentActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
            } else {
              Toast.makeText(EditStudent.this, "An Error Occurred!", Toast.LENGTH_SHORT).show();
            }

          }
        }
      }
    });

    // The button click listener for going back to student list
    backBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(EditStudent.this, StudentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });
  }
  // Initializes all the views needed on these activity
  public void initAllViews(){
    firstname_text = (EditText) findViewById(R.id.editFname);
    middlename_text = (EditText) findViewById(R.id.editMname);
    lastname_text = (EditText) findViewById(R.id.editLname);
    email_text = (EditText) findViewById(R.id.editEmail);
    contact_text = (EditText) findViewById(R.id.editContact);
    editBtn = (Button) findViewById(R.id.editStudentBtn);
    backBtn = (Button) findViewById(R.id.backBtn);
  }

  // This function get the extras or data came from the intent.
  public void getExtras(){
    Bundle extras = getIntent().getExtras();
    if(extras == null) {
      id = -1;
      firstname = "";
      middlename = "";
      lastname = "";
      email = "";
      contact = -1;
    } else {
      id = extras.getInt("id");
      firstname = extras.getString("firstname");
      middlename = extras.getString("middlename");
      lastname = extras.getString("lastname");
      email = extras.getString("email");
      contact = extras.getInt("contact");
    }
  }

  public void getAllStudents(){
    String[] projection = {
            StackContract.StudentEntry._ID,
            StackContract.StudentEntry.COLUMN_FIRSTNAME,
            StackContract.StudentEntry.COLUMN_MIDNAME,
            StackContract.StudentEntry.COLUMN_LASTNAME,
            StackContract.StudentEntry.COLUMN_EMAIL,
            StackContract.StudentEntry.COLUMN_CONTACT
    };
    Cursor cursor = getContentResolver().query(StackContract.StudentEntry.CONTENT_URI, projection, null, null, null);
    students = new ArrayList<>();
    try{
      int idIdx = cursor.getColumnIndex(StackContract.StudentEntry._ID);
      int fnameIdx = cursor.getColumnIndex(StackContract.StudentEntry.COLUMN_FIRSTNAME);
      int mnameIdx = cursor.getColumnIndex(StackContract.StudentEntry.COLUMN_MIDNAME);
      int lnameIdx = cursor.getColumnIndex(StackContract.StudentEntry.COLUMN_LASTNAME);
      int emailIdx = cursor.getColumnIndex(StackContract.StudentEntry.COLUMN_EMAIL);
      int contactIdx = cursor.getColumnIndex(StackContract.StudentEntry.COLUMN_CONTACT);
      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
        Student stud = new Student(cursor.getInt(idIdx), cursor.getString(fnameIdx), cursor.getString(mnameIdx), cursor.getString(lnameIdx), cursor.getString(emailIdx), cursor.getInt(contactIdx));
        students.add(stud);
      }
    } finally {
      cursor.close();
    }
  }
}
