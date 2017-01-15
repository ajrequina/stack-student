package com.example.arjemarielrequina.stackstudent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arjemarielrequina.stackstudent.StackContract.*;

public class LoginActivity extends AppCompatActivity {
  boolean isLogin = true;
  String password = null;
  String fullname = null;
  String email = null;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);
    String[] projection = {UserEntry.COLUMN_PASSWORD};
    Cursor cursor = getContentResolver().query(UserEntry.CONTENT_URI, projection, null, null, null);
    //if the cursor count is zero, it means that the user does not signed up yet
    if(cursor.getCount() == 0){
      setContentView(R.layout.activity_signup);
      isLogin = false;
    }

    // jfksjakfjaskfjaskfja

    if(isLogin){
      cursor.moveToFirst();
      password = cursor.getString(0);
      loginCheck();
      this.setTitle("Stack Student - Sign In");
    } else {
      this.setTitle("Stack Student - Sign Up");
      signupCheck();
    }
  }
  //function that checks if the user inputted password is correct, if yes he/she will be redirected to subject view page
  private void loginCheck(){
    final EditText text = (EditText) findViewById(R.id.editPassword);
    Button login = (Button) findViewById(R.id.loginBtn);
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean proceed = true;
        if(text.getText().toString().isEmpty()){
          Toast.makeText(LoginActivity.this,"Please input your password!" , Toast.LENGTH_SHORT).show();
          proceed = false;
        } if(proceed){
          boolean check = password.equals(text.getText().toString());
          if(!check){
            Toast.makeText(LoginActivity.this,"Incorrect password!" , Toast.LENGTH_SHORT).show();
          } else {
              Intent intent = new Intent(LoginActivity.this, SubjectActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
          }
        }
      }
    });

  }
  // The function that checks and store the sign up information of the user
  private void signupCheck(){
    final EditText firstname = (EditText) findViewById(R.id.editFname);
    final EditText middlename = (EditText) findViewById(R.id.editMname);
    final EditText lastname = (EditText) findViewById(R.id.editLname);
    final EditText email = (EditText) findViewById(R.id.editEmail);
    final EditText password1 = (EditText) findViewById(R.id.editPassword1);
    final EditText password2 = (EditText) findViewById(R.id.editPassword2);

    Button signup = (Button) findViewById(R.id.signupBtn);
    signup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean proceed = true;
        if(firstname.getText().toString().isEmpty()){
          Toast.makeText(LoginActivity.this,"Firstname is Required!" , Toast.LENGTH_SHORT).show();
          proceed = false;
        } if(proceed && lastname.getText().toString().isEmpty()){
          proceed = false;
          Toast.makeText(LoginActivity.this,"Lastname is Required!" , Toast.LENGTH_SHORT).show();
        } if(proceed && email.getText().toString().isEmpty()){
          proceed = false;
          Toast.makeText(LoginActivity.this,"Email is Required!" , Toast.LENGTH_SHORT).show();
        } if(proceed && password1.getText().toString().isEmpty()){
          proceed = false;
          Toast.makeText(LoginActivity.this,"Password is Required!" , Toast.LENGTH_SHORT).show();
        } if(proceed && password2.getText().toString().isEmpty()){
          proceed = false;
          Toast.makeText(LoginActivity.this,"Please Confirm your Password!" , Toast.LENGTH_SHORT).show();
        } if(proceed){
          boolean check = password1.getText().toString().equals(password2.getText().toString());
          if(!check){
            Toast.makeText(LoginActivity.this,"Passwords Do Not Match!" , Toast.LENGTH_SHORT).show();
          } else {
            ContentValues values = new ContentValues();
            values.put(UserEntry.COLUMN_FIRSTNAME, firstname.getText().toString());
            values.put(UserEntry.COLUMN_MIDNAME, middlename.getText().toString());
            values.put(UserEntry.COLUMN_LASTNAME, lastname.getText().toString());
            values.put(UserEntry.COLUMN_EMAIL, email.getText().toString());
            values.put(UserEntry.COLUMN_PASSWORD, password1.getText().toString());
            Uri newUri = getContentResolver().insert(UserEntry.CONTENT_URI, values);
            if(newUri != null){
              Intent intent = new Intent(LoginActivity.this, SubjectActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
            } else {
              Toast.makeText(LoginActivity.this,"An Error Occurred!" , Toast.LENGTH_SHORT).show();
            }

          }
        }
      }
    });
  }
}
