package com.example.arjemarielrequina.stackstudent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChooseEnrollee extends AppCompatActivity {
  int subject_id;
  ListView list_students;
  StudentListAdapter adapter;
  ArrayList<Student> students;
  static boolean[] selected;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_enrollee);
    setTitle("Choose Students");
    getExtras();
    setData();
    list_students =  (ListView) findViewById(R.id.enroll_list);
    selected = new boolean[students.size()];
    adapter = new StudentListAdapter(ChooseEnrollee.this, students);
    list_students.setAdapter(adapter);
    list_students.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
  }

  // Sets the data and filter the list of students based on the list of the enrollees
  // for the display of the list of students will just those who are not enrolled in the subject
  public void setData(){
    getAllStudents();
    ArrayList<String> enrollees = getEnrollees();
    ArrayList<Student> filtered = new ArrayList<>();
    for(Student stud : students){
      boolean found = false;
      for(String en : enrollees){
        if(String.valueOf(stud.id).equals(en)){
          found = true;
          break;
        }
      }
      if(!found){
        filtered.add(stud);
      }
    }
    students = filtered;
  }

  // The function that returns all enrollees of the subject.
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

  // function to get data from the intent extras
  public void getExtras(){
    Bundle extras = getIntent().getExtras();
    if(extras == null) {
      subject_id = -1;
    } else {
      subject_id = extras.getInt("subject_id");
    }
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.addenroll, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if(id == R.id.action_enroll_selected){
     String selected = "";
     Student student = null;
     int position = 0;
     int cntChoice = list_students.getCount();
     SparseBooleanArray sparseBooleanArray = list_students.getCheckedItemPositions();
     for(int i = 0; i < cntChoice; i++){
       if(sparseBooleanArray.get(i)) {
         position = i;
         student = (Student)list_students.getItemAtPosition(i);
         selected = student.fullname + "\n";
       }
     }
     if(student != null) {
       ContentValues values = new ContentValues();
       values.put(StackContract.EnrollEntry.COLUMN_STUDENT_ID, student.id);
       values.put(StackContract.EnrollEntry.COLUMN_SUBJECT_ID, subject_id);
       Uri newUri = getContentResolver().insert(StackContract.EnrollEntry.CONTENT_URI, values);
       if(newUri != null){
         Toast.makeText(ChooseEnrollee.this,"Student Successfully Enrolled!" , Toast.LENGTH_SHORT).show();
         students.remove(position);
         adapter.notifyDataSetChanged();
       } else {
         Toast.makeText(ChooseEnrollee.this,"An Error Occurred!" , Toast.LENGTH_SHORT).show();
       }
     }

    } if(id == R.id.action_cancel_action){

    } if(id == R.id.action_back_action) {
      Intent intent = new Intent(ChooseEnrollee.this, EnrollView.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }

    return super.onOptionsItemSelected(item);
  }

  public static class StudentListAdapter extends ArrayAdapter<Student> {
    private static final String LOG_TAG = SubjectAdapter.class.getSimpleName();
    public StudentListAdapter(Activity context, ArrayList<Student> students) {
      super(context, 0, students);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
      final Student student = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_choose_enrollee, parent, false);
      }
      TextView studentName = (TextView) convertView.findViewById(R.id.student_name);
      studentName.setText(student.fullname);
      final CheckBox box = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
      selected[position] = box.isSelected();
      box.setOnClickListener( new View.OnClickListener(){
        @Override
        public void onClick(View view) {
          if(box.isChecked()){
            Toast.makeText(parent.getContext(),"Touched! -> " + position, Toast.LENGTH_SHORT).show();
          }
           selected[position] = box.isChecked();
        }
      });
      return convertView;
    }
  }

  // The function that retrieves all of the students from the database
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
