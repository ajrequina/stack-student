package com.example.arjemarielrequina.stackstudent;

import android.content.ContentUris;
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

import java.util.ArrayList;

public class EditSubject extends AppCompatActivity {
  int id;
  String title;
  String details;

  EditText title_text;
  EditText details_text;
  Button editBtn;
  Button backBtn;
  ArrayList<Subject> subjects;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_subject);
    getExtras();
    initAllViews();
    subjects = getAllSubjects();

    title_text.setText(title);
    details_text.setText(details);

    editBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean proceed = true;
        if(title_text.getText().toString().isEmpty()){
          Toast.makeText(EditSubject.this,"Title is Required!" , Toast.LENGTH_SHORT).show();
          proceed = false;
        }
        for(Subject subject : subjects){
          if(subject.id  != id && subject.title.toLowerCase().equals(title_text.getText().toString().toLowerCase())){
            Toast.makeText(EditSubject.this,"Subject Already Exists!" , Toast.LENGTH_SHORT).show();
            proceed = false;
            break;
          }
        }

        if(proceed){
          ContentValues values = new ContentValues();
          values.put(StackContract.SubjectEntry.COLUMN_TITLE, title_text.getText().toString());
          if(!details_text.getText().toString().isEmpty()){
            values.put(StackContract.SubjectEntry.COLUMN_DESCRIPTION, details_text.getText().toString());
          }
          Uri uri = ContentUris.withAppendedId(StackContract.SubjectEntry.CONTENT_URI, id);
          int i  = getContentResolver().update(uri, values, null, null);
          if(i != -1){
            Toast.makeText(EditSubject.this,"Subject Information Successfully Updated!" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditSubject.this, SubjectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          } else {
            Toast.makeText(EditSubject.this,"An Error Occurred!" , Toast.LENGTH_SHORT).show();
          }
        }
      }
    });

    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(EditSubject.this, SubjectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });
  }

  // Initializes all the views needed on these activity
  public void initAllViews(){
    title_text = (EditText) findViewById(R.id.editTitle);
    details_text = (EditText) findViewById(R.id.editDescrip);
    editBtn = (Button) findViewById(R.id.editBtn);
    backBtn = (Button) findViewById(R.id.backBtn);
  }
  // This function get the extras or data came from the intent.
  public void getExtras(){
    Bundle extras = getIntent().getExtras();
    if(extras == null) {
      id = -1;
      title = "";
      details = "";
    } else {
      id = extras.getInt("id");
      title = extras.getString("title");
      details = extras.getString("details");
    }
  }

  // Retrieves all the subjects in the database for displaying and further operations such
  // as UPDATE or DELETE
  public ArrayList<Subject> getAllSubjects() {
    String[] projection = {
            StackContract.SubjectEntry._ID,
            StackContract.SubjectEntry.COLUMN_TITLE,
            StackContract.SubjectEntry.COLUMN_DESCRIPTION};
    Cursor cursor = getContentResolver().query(StackContract.SubjectEntry.CONTENT_URI, projection, null, null, null);
    ArrayList<Subject> subjects = new ArrayList<>();
    try {
      int idIdx = cursor.getColumnIndex(StackContract.SubjectEntry._ID);
      int titleIdx = cursor.getColumnIndex(StackContract.SubjectEntry.COLUMN_TITLE);
      int detailsIdx = cursor.getColumnIndex(StackContract.SubjectEntry.COLUMN_DESCRIPTION);

      for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        Subject sub = new Subject(cursor.getInt(idIdx), cursor.getString(titleIdx), cursor.getString(detailsIdx));
        subjects.add(sub);
      }
      return subjects;
    } finally {
      cursor.close();
    }
  }
}
