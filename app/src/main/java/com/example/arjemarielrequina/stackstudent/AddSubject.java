package com.example.arjemarielrequina.stackstudent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.example.arjemarielrequina.stackstudent.StackContract.*;

public class AddSubject extends AppCompatActivity {
  ArrayList<Subject> subjects;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("Add Subject");
    setContentView(R.layout.activity_add_subject);
    final TextView titleView = (TextView) findViewById(R.id.editTitle);
    final TextView descripView = (TextView) findViewById(R.id.editDescrip);
    Button   saveBtn = (Button) findViewById(R.id.editBtn);
    Button  backBtn = (Button) findViewById(R.id.backBtn);
    getAllSubjects();
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(AddSubject.this, SubjectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });
    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean proceed = true;
        if(titleView.getText().toString().isEmpty()){
          Toast.makeText(AddSubject.this,"Title is Required!" , Toast.LENGTH_SHORT).show();
          proceed = false;
        }
        for(Subject subject : subjects){
          if(subject.title.toLowerCase().equals(titleView.getText().toString().toLowerCase())){
            Toast.makeText(AddSubject.this,"Subject Already Exists!" , Toast.LENGTH_SHORT).show();
            proceed = false;
            break;
          }
        }

        if(proceed){
          ContentValues values = new ContentValues();
          values.put(SubjectEntry.COLUMN_TITLE, titleView.getText().toString());
          values.put(SubjectEntry.COLUMN_DESCRIPTION, descripView.getText().toString());
          Uri newUri = getContentResolver().insert(SubjectEntry.CONTENT_URI, values);
          if(newUri != null){
            Toast.makeText(AddSubject.this,"Subject Successfully Added!" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddSubject.this, SubjectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          } else {
            Toast.makeText(AddSubject.this,"An Error Occurred!" , Toast.LENGTH_SHORT).show();
          }
        }

      }
    });
  }

  public void getAllSubjects(){
    String[] projection = {
            SubjectEntry._ID,
            SubjectEntry.COLUMN_TITLE,
            SubjectEntry.COLUMN_DESCRIPTION };
    Cursor cursor = getContentResolver().query(SubjectEntry.CONTENT_URI, projection, null, null, null);
    subjects = new ArrayList<>();
    try{
      int idIdx = cursor.getColumnIndex(SubjectEntry._ID);
      int titleIdx = cursor.getColumnIndex(SubjectEntry.COLUMN_TITLE);
      int detailsIdx = cursor.getColumnIndex(SubjectEntry.COLUMN_DESCRIPTION);

      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
        Subject sub = new Subject(cursor.getInt(idIdx), cursor.getString(titleIdx), cursor.getString(detailsIdx));
        subjects.add(sub);
      }
    } finally {
      cursor.close();
    }
  }
}
