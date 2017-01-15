package com.example.arjemarielrequina.stackstudent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EnrollView extends AppCompatActivity {
  private static int subject_id;
  private String[] ids;
  ListView list_enrollees;
  ArrayList<Student> students;
  EnrolleeListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("View Enrolled Students");
    setContentView(R.layout.activity_enroll_view);
    getExtras();

    list_enrollees =  (ListView) findViewById(R.id.enroll_list);
    setData();
    adapter = new EnrolleeListAdapter(EnrollView.this, students);
    list_enrollees.setAdapter(adapter);
  }

  public void getExtras(){
    Bundle extras = getIntent().getExtras();
    if(extras == null) {
      subject_id = -1;
    } else {
      subject_id = extras.getInt("subject_id");
    }
  }

  // The adapter that the this class uses to attach data on the list view
  public static class EnrolleeListAdapter extends ArrayAdapter<Student> {
    private static final String LOG_TAG = SubjectAdapter.class.getSimpleName();
    public EnrolleeListAdapter(Activity context, ArrayList<Student> students) {
      super(context, 0, students);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
      final Student student = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_enroll, parent, false);
      }
      TextView studentName = (TextView) convertView.findViewById(R.id.student_name);
      studentName.setText(student.fullname);
      TextView studentScore = (TextView) convertView.findViewById(R.id.student_ave_score);
      studentScore.setText("Ave. Score : " + String.valueOf(student.count));
      ImageButton delBtn = (ImageButton) convertView.findViewById(R.id.deleteButton);
      delBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
          builder.setMessage("The corresponding scores will be deleted.");
          builder.setCancelable(false);
          builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              String selection = StackContract.EnrollEntry.COLUMN_STUDENT_ID + " = ? and "
                      + StackContract.EnrollEntry.COLUMN_SUBJECT_ID + " = ? ";
              String selectionArgs[] = {String.valueOf(student.id), String.valueOf(subject_id)};
              int result = parent.getContext().getContentResolver().delete(StackContract.EnrollEntry.CONTENT_URI, selection, selectionArgs);
                  result = parent.getContext().getContentResolver().delete(StackContract.RecordEntry.CONTENT_URI, selection, selectionArgs);
              if(result != -1){
                Toast.makeText(parent.getContext(), "Student Sucessfully Unenrolled! ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(parent.getContext(), EnrollView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                parent.getContext().startActivity(intent);
                notifyDataSetChanged();
              } else {
                Toast.makeText(parent.getContext(), "An Error Occurred! ", Toast.LENGTH_SHORT).show();
              }
              dialog.cancel();
            }
          }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              dialog.cancel();
            }
          });
          builder.show();
        }
      });
      studentName.setText(student.fullname);

      return convertView;
    }
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.enroll, menu);
    return true;
  }

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

  public void setData(){
    Toast.makeText(EnrollView.this, String.valueOf(getEnrollees().size()), Toast.LENGTH_SHORT).show();
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
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_add_enroll) {
      Intent intent = new Intent(EnrollView.this, ChooseEnrollee.class);
      intent.putExtra("subject_id", subject_id);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    } if(id ==  R.id.action_start_class){
      Intent intent = new Intent(EnrollView.this, StartClassOptions.class);
      intent.putExtra("subject_id", subject_id);
      ArrayList<String> chosen = new ArrayList<>();
      intent.putExtra("chosen", chosen);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }
    return super.onOptionsItemSelected(item);
  }
}
