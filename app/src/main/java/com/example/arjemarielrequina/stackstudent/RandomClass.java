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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * This is the class that handles the randomization of the enrolled students
 * */
public class RandomClass extends AppCompatActivity {
  private static int subject_id;
  private ArrayList<String> chosen;
  private int min;
  ArrayList<Student> students;
  static Student selected = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_random_class);
    setTitle("Random Class");
    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if (extras == null) {
        subject_id = -1;
        chosen = new ArrayList<>();
        min = -1;
      } else {
        subject_id = extras.getInt("subject_id");
        min = extras.getInt("min");
      }
    } else {
      subject_id = (Integer) savedInstanceState.getSerializable("subject_id");
      chosen = (ArrayList<String>) savedInstanceState.getSerializable("chosen");
      min = (Integer) savedInstanceState.getSerializable("min");
    }
   // Toast.makeText(RandomClass.this, String.valueOf(min), Toast.LENGTH_SHORT).show();

    setData();
    checkAndRandomize();


  }

  // Checks the possibilities when the user can no longer randomize
  // Also, this performs the randomization
  public void checkAndRandomize(){
    TextView textView = (TextView) findViewById(R.id.foundView);
    final TextView scoreView = (TextView) findViewById(R.id.scoreText);
    Button another = (Button) findViewById(R.id.otherBtn);
    Button stop = (Button) findViewById(R.id.stopBtn);
    ArrayList<Student> unchosen = new ArrayList<>();
    for (Student student : students) {
      boolean found = false;
      for (String id : chosen) {
        if (id.equals(String.valueOf(student.id))) {
          found = true;
          break;
        }
      }
      if (!found) {
        unchosen.add(student);
      }
    }

    // Case 1 : The student list size is 0  and the chosen size is 0, therefore, there are no students on this subject
    if(students.size() == 0 && chosen.size() == 0){
      AlertDialog.Builder builder = new AlertDialog.Builder(RandomClass.this);
      builder.setMessage("There are no students on this subject.");
      builder.setCancelable(false);
      builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent(RandomClass.this, EnrollView.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          dialog.cancel();
        }
      });
      builder.show();
    }
    // Case 2 : if the unchosen size is 0 while the chosen size is more than 0 then all students have been called
    if(unchosen.size() == 0 && chosen.size() > 0){
      AlertDialog.Builder builder = new AlertDialog.Builder(RandomClass.this);
      builder.setMessage("All students are already called.");
      builder.setCancelable(false);
      builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent(RandomClass.this, EnrollView.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          dialog.cancel();
        }
      });
    } else {
      // If above cases fails, then the function will go to randomization
      ArrayList<Student> priority = new ArrayList<>();
      for(Student unchose : unchosen){
        if(unchose.aveScore() <= min){
          priority.add(unchose);
        }
      }

      if(priority.size() > 0){
        Random rand = new Random();
        int x = rand.nextInt(priority.size());
        selected = priority.get(x);
      } else {
        Random rand = new Random();
        int x = rand.nextInt(unchosen.size());
        selected = unchosen.get(x);
      }
      if(selected != null){
        chosen.add(String.valueOf(selected.id));
      }

      textView.setText(selected.fullname);
      // When the user wants another student to be called
      another.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(scoreView.getText().toString().isEmpty()){
            Toast.makeText(RandomClass.this,"Score is Required!" , Toast.LENGTH_SHORT).show();
          } else {
            ContentValues values = new ContentValues();
            values.put(StackContract.RecordEntry.COLUMN_STUDENT_ID, selected.id);
            values.put(StackContract.RecordEntry.COLUMN_SUBJECT_ID, subject_id);
            values.put(StackContract.RecordEntry.COLUMN_SCORE, Integer.parseInt(scoreView.getText().toString()));
            Uri newUri = getContentResolver().insert(StackContract.RecordEntry.CONTENT_URI, values);
            Intent intent = new Intent(RandomClass.this, RandomClass.class);
            intent.putExtra("min", min);
            intent.putExtra("subject_id", subject_id);
            intent.putExtra("chosen", chosen);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }

        }
      });
    }
  }

  // fetches all the enrolles of the subject
  public ArrayList<String> getEnrollees(){
    String[] projection = {
            StackContract.EnrollEntry.COLUMN_STUDENT_ID
    };
    String selection =  StackContract.EnrollEntry.COLUMN_SUBJECT_ID + " = ? ";
    String selectionArgs[] = {String.valueOf(subject_id)};
    Cursor cursor = getContentResolver().query(StackContract.EnrollEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    ArrayList<String> students = new ArrayList<>();
    try{
      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
        students.add(cursor.getString(0));
      }
    } finally {
      cursor.close();
    }
    return students;
  }
  // Fetches all the records under a subject through subject id
  public ArrayList<ArrayList<String>> getRecords(){
    ArrayList<ArrayList<String>> data = new ArrayList<>();
    String[] projection = {
            StackContract.RecordEntry.COLUMN_STUDENT_ID,
            StackContract.RecordEntry.COLUMN_SCORE
    };

    String selection =  StackContract.RecordEntry.COLUMN_SUBJECT_ID + " = ? ";
    String selectionArgs[] = {String.valueOf(subject_id)};
    Cursor cursor = getContentResolver().query(StackContract.RecordEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    try{
      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
        ArrayList<String> stud = new ArrayList<>();
        stud.add(cursor.getString(0));
        stud.add(cursor.getString(1));
        data.add(stud);
      }
    } finally {
      cursor.close();
    }
    return data;
  }

  // The main function that calls the functions that fetches each data found in each table
  // The filtering process of the students also done in here.
  public void setData(){
    Toast.makeText(RandomClass.this, String.valueOf(getEnrollees().size()), Toast.LENGTH_SHORT).show();
    ArrayList<String> enrollee_id = getEnrollees();
    ArrayList<Student> allStudent = getAllStudents();
    ArrayList<Student> filteredStudents = new ArrayList<Student>();
    for(String id : enrollee_id){
      for(Student student : allStudent){
        if(String.valueOf(student.id).equals(id)){
          filteredStudents.add(student);
        }
      }
    }

    ArrayList<ArrayList<String>> records = getRecords();
    for(ArrayList<String> record : records){
      for(Student student : filteredStudents){
        if(record.get(0).equals(String.valueOf(student.id))){
          student.accumScore(Integer.parseInt(record.get(1)));
        }
      }
    }

    students = filteredStudents;
  }

  // Fetches all the students in the database.
  public ArrayList<Student> getAllStudents(){
    String[] projection = {
            StackContract.StudentEntry._ID,
            StackContract.StudentEntry.COLUMN_FIRSTNAME,
            StackContract.StudentEntry.COLUMN_MIDNAME,
            StackContract.StudentEntry.COLUMN_LASTNAME,
            StackContract.StudentEntry.COLUMN_EMAIL,
            StackContract.StudentEntry.COLUMN_CONTACT
    };
    Cursor cursor = getContentResolver().query(StackContract.StudentEntry.CONTENT_URI, projection, null, null, null);
    ArrayList<Student> students = new ArrayList<>();
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
      return students;
    } finally {
      cursor.close();
    }
  }
}
