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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.arjemarielrequina.stackstudent.StackContract.*;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
  ArrayList<Student> students;
  ListView student_listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student);
    setTitle("All Stack Students");
    student_listView = (ListView) findViewById(R.id.student_list);
    students = getAllStudents();
    StudentAdapter adapter = new StudentAdapter(StudentActivity.this, students);
    student_listView.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.student, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_add_student) {
      Intent intent = new Intent(StudentActivity.this, AddStudent.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
   }
    return super.onOptionsItemSelected(item);
  }

  // The Custom ArrayAdapter to be used in setting up the data in the arraylist of students
  // to be attached into student_listView
  public static class StudentAdapter extends ArrayAdapter<Student> {
    private static final String LOG_TAG = SubjectAdapter.class.getSimpleName();
    public StudentAdapter(Activity context, ArrayList<Student> students) {
      super(context, 0, students);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
      final Student student = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_student, parent, false);
      }
      TextView titleView = (TextView) convertView.findViewById(R.id.student_name);
      titleView.setText(student.fullname);
      ImageButton delBtn = (ImageButton) convertView.findViewById(R.id.deleteButton);
      delBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
          builder.setTitle("Student Deletion");
          builder.setMessage("The corresponding records will also be deleted.");
          builder.setCancelable(false);
          builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              Uri uri = ContentUris.withAppendedId(StudentEntry.CONTENT_URI, student.id);
              int result = parent.getContext().getContentResolver().delete(uri, null, null);
              String selection = RecordEntry.COLUMN_STUDENT_ID + "= ?";
              String[] selectionArgs = new String[]{String.valueOf(student.id)};
              result = parent.getContext().getContentResolver().delete(RecordEntry.CONTENT_URI, selection, selectionArgs);
              result = parent.getContext().getContentResolver().delete(EnrollEntry.CONTENT_URI, selection, selectionArgs);
              if(result != -1){
                Toast.makeText(parent.getContext(), "Student Sucessfully Deleted! ", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                Intent intent = new Intent(parent.getContext(), StudentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                parent.getContext().startActivity(intent);
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
      ImageButton editBtn = (ImageButton) convertView.findViewById(R.id.editButton);
      editBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Intent intent = new Intent(parent.getContext(), EditStudent.class);
          intent.putExtra("id", student.id);
          intent.putExtra("firstname", student.firstname);
          intent.putExtra("middlename", student.middlename);
          intent.putExtra("lastname", student.lastname);
          intent.putExtra("email", student.email);
          intent.putExtra("contact", student.contact);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          parent.getContext().startActivity(intent);
        }
      });
      return convertView;
    }
  }

  // The function that retrieves all the students from the database for displaying and for other
  // operations such as UPDATE and DELETE
  public  ArrayList<Student> getAllStudents(){
    String[] projection = {
            StudentEntry._ID,
            StudentEntry.COLUMN_FIRSTNAME,
            StudentEntry.COLUMN_MIDNAME,
            StudentEntry.COLUMN_LASTNAME,
            StudentEntry.COLUMN_EMAIL,
            StudentEntry.COLUMN_CONTACT
    };
    ArrayList<Student> students = new ArrayList<>();
    Cursor cursor = getContentResolver().query(StudentEntry.CONTENT_URI, projection, null, null, null);
    try{
      int idIdx = cursor.getColumnIndex(StudentEntry._ID);
      int fnameIdx = cursor.getColumnIndex(StudentEntry.COLUMN_FIRSTNAME);
      int mnameIdx = cursor.getColumnIndex(StudentEntry.COLUMN_MIDNAME);
      int lnameIdx = cursor.getColumnIndex(StudentEntry.COLUMN_LASTNAME);
      int emailIdx = cursor.getColumnIndex(StudentEntry.COLUMN_EMAIL);
      int contactIdx = cursor.getColumnIndex(StudentEntry.COLUMN_CONTACT);
      Toast.makeText(StudentActivity.this, String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
        Student stud = new Student(cursor.getInt(idIdx), cursor.getString(fnameIdx), cursor.getString(mnameIdx), cursor.getString(lnameIdx), cursor.getString(emailIdx), cursor.getInt(contactIdx));
        students.add(stud);
      }
    } finally {
      cursor.close();
    }
    return students;
  }
}
