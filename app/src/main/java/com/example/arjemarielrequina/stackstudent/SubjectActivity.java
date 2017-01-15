package com.example.arjemarielrequina.stackstudent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjemarielrequina.stackstudent.StackContract.*;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  static ArrayList<Subject> subjects;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subject);
    setTitle("Stack Subjects");

    subjects = getAllSubjects();
    SubjectAdapter adapter = new SubjectAdapter(SubjectActivity.this, subjects);
    ListView subject_listView = (ListView) findViewById(R.id.subject_list_view);
    subject_listView.setAdapter(adapter);
    subject_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
                              long id) {
        Subject subject = (Subject) parent.getAdapter().getItem(position);
        Intent intent = new Intent(SubjectActivity.this, EnrollView.class);
        intent.putExtra("subject_id", subject.id);
        startActivity(intent);
      }
    });

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.subject, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    // Handles when the user clicks the add_subject option in the menu
    // It goes to AddSubject class
    if (id == R.id.action_add_subject) {
      Intent intent = new Intent(SubjectActivity.this, AddSubject.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    // Handles when the user clicks the view students option on the navigation bar
    // Goes to the Student Activity class
    if (id == R.id.nav_view_students) {
      Intent intent = new Intent(SubjectActivity.this, StudentActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  // Retrieves all the subjects in the database for displaying and further operations such
  // as UPDATE or DELETE
  public ArrayList<Subject> getAllSubjects() {
    String[] projection = {
            SubjectEntry._ID,
            SubjectEntry.COLUMN_TITLE,
            SubjectEntry.COLUMN_DESCRIPTION};
    Cursor cursor = getContentResolver().query(SubjectEntry.CONTENT_URI, projection, null, null, null);
    ArrayList<Subject> subjects = new ArrayList<>();
    try {
      int idIdx = cursor.getColumnIndex(SubjectEntry._ID);
      int titleIdx = cursor.getColumnIndex(SubjectEntry.COLUMN_TITLE);
      int detailsIdx = cursor.getColumnIndex(SubjectEntry.COLUMN_DESCRIPTION);

      for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        Subject sub = new Subject(cursor.getInt(idIdx), cursor.getString(titleIdx), cursor.getString(detailsIdx));
        subjects.add(sub);
      }
      return subjects;
    } finally {
      cursor.close();
    }
  }

  // The ArrayAdapter used in setting up the data from arraylist of subjects
  // to be attached to the subject_listview
  public static class SubjectAdapter extends ArrayAdapter<Subject>{
    private static final String LOG_TAG = SubjectAdapter.class.getSimpleName();
    public SubjectAdapter(Activity context, ArrayList<Subject> subjects) {
      super(context, 0, subjects);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
      final Subject subject = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_subjects, parent, false);
      }
      TextView titleView = (TextView) convertView.findViewById(R.id.subject_title);
      titleView.setText(subject.title);
      ImageButton delBtn = (ImageButton) convertView.findViewById(R.id.deleteButton);
      delBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
          builder.setTitle("Subject Deletion");
          builder.setMessage("The corresponding records will also be deleted.");
          builder.setCancelable(false);
          builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              Uri uri = ContentUris.withAppendedId(SubjectEntry.CONTENT_URI, subject.id);
              int result = parent.getContext().getContentResolver().delete(uri, null, null);
              String selection = RecordEntry.COLUMN_SUBJECT_ID + "= ?";
              String[] selectionArgs = new String[]{String.valueOf(subject.id)};
              result = parent.getContext().getContentResolver().delete(RecordEntry.CONTENT_URI, selection, selectionArgs);
              result = parent.getContext().getContentResolver().delete(EnrollEntry.CONTENT_URI, selection, selectionArgs);

              if(result != -1){
                Toast.makeText(parent.getContext(), "Subject Sucessfully Deleted! ", Toast.LENGTH_SHORT).show();
                subjects.remove(position);
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

      ImageButton editBtn = (ImageButton) convertView.findViewById(R.id.editButton);
      editBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Intent intent = new Intent(parent.getContext(), EditSubject.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("id", subject.id);
          intent.putExtra("title", subject.title);
          intent.putExtra("details", subject.details);
          parent.getContext().startActivity(intent);
        }
      });
      return convertView;
    }
  }
}


