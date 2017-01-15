package com.example.arjemarielrequina.stackstudent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjemarielrequina.stackstudent.StackContract.SubjectEntry;

import java.util.ArrayList;

/**
 * Created by Arjemariel Requina on 12/9/2016.
 */

//// ------------ NOT INCLUDED ------------------ /////
public class SubjectAdapter extends ArrayAdapter<Subject>{
  private static final String LOG_TAG = SubjectAdapter.class.getSimpleName();
  ;
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
        builder.setMessage("The corresponding records will also be deleted.");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            Uri uri = ContentUris.withAppendedId(SubjectEntry.CONTENT_URI, subject.id);
            int result = parent.getContext().getContentResolver().delete(uri, null, null);
            if(result != -1){
              Toast.makeText(parent.getContext(), "Subject Sucessfully Deleted! ", Toast.LENGTH_SHORT).show();
              notifyDataSetChanged();
            } else {
              Toast.makeText(parent.getContext(), "An Error Occurred! ", Toast.LENGTH_SHORT).show();
            }

            dialog.cancel();
          }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        builder.show();
      }
    });
    return convertView;
  }
 }

