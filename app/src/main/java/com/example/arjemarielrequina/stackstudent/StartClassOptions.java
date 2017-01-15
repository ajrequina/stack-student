package com.example.arjemarielrequina.stackstudent;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.RandomAccess;

// This is the class in which the user inputs the minimum priority before the randomization process
public class StartClassOptions extends AppCompatActivity {
  int subject_id = 0;
  ArrayList<String> chosen;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_class_options);
    setTitle("Start Class Options");
    getExtras();


    NumberPicker pick = (NumberPicker) findViewById(R.id.numberPicker);
    pick.setMinValue(0);
    pick.setMaxValue(100);
    final NumberPicker picker = (NumberPicker) findViewById(R.id.numberPicker);
    Button saveBtn = (Button) findViewById(R.id.savebutton);
    Button backBtn = (Button) findViewById(R.id.backbutton);
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(StartClassOptions.this, EnrollView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });
    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(StartClassOptions.this, RandomClass.class);
        intent.putExtra("subject_id", subject_id);
        intent.putExtra("chosen", chosen);
        intent.putExtra("min", picker.getValue());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });
  }
  // Gets the data found in the intent extras
  private void getExtras(){
    Bundle extras = getIntent().getExtras();
    if(extras == null) {
      subject_id = -1;
      chosen = new ArrayList<>();
    } else {
      chosen = extras.getStringArrayList("chosen");
      subject_id = extras.getInt("subject_id");
    }
  }
}
