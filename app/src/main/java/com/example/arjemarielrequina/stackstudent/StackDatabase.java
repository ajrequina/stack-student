package com.example.arjemarielrequina.stackstudent;

/**
 * Created by Arjemariel Requina on 12/8/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.arjemarielrequina.stackstudent.StackContract.*;

/*
  This is the Database class of the application. This is where the creation of the database and tables take place.
  This is where also the upgrading of the database take place if ever there will be changes on the database on the future.
  This is the class that will be instantiated in the custom content provider class (StackProvider Class) for the
  CRUD operations of the app.
*/
public class StackDatabase extends SQLiteOpenHelper  {
  public static final String LOG_TAG = StackDatabase.class.getSimpleName();

  private static final String DATABASE_NAME = "stackstudent2.db";

  // The current version of the app. This will be incremented on the future if ever there will be changes on the database.
  private static final int DATABASE_VERSION = 1;

  public StackDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    /*
     The following are the create statements for the table that will be found on the database.
     These create statements will be only executed once if these tables does not exist yet on the database.
    */

    // The create statement for the creating the student table
    String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " + StudentEntry.TABLE_NAME + " ("
            + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + StudentEntry.COLUMN_FIRSTNAME + " TEXT NOT NULL, "
            + StudentEntry.COLUMN_MIDNAME + " TEXT, "
            + StudentEntry.COLUMN_LASTNAME + " TEXT NOT NULL, "
            + StudentEntry.COLUMN_EMAIL + " TEXT, "
            + StudentEntry.COLUMN_CONTACT + " TEXT );";

    // The create statement for the creating the subject table
    String SQL_CREATE_SUBJECT_TABLE = "CREATE TABLE " + SubjectEntry.TABLE_NAME + " ("
            + SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SubjectEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + SubjectEntry.COLUMN_DESCRIPTION + " TEXT );";

    // The create statement for the creating enrollment table
    String SQL_CREATE_ENROLL_TABLE = "CREATE TABLE " + EnrollEntry.TABLE_NAME + " ("
            + EnrollEntry.COLUMN_SUBJECT_ID + " INTEGER, "
            + EnrollEntry.COLUMN_STUDENT_ID + " INTEGER, "
            + " PRIMARY KEY ( "
            + EnrollEntry.COLUMN_SUBJECT_ID + ", "
            + EnrollEntry.COLUMN_STUDENT_ID + "));";

    // The create statement for creating record table
    String SQL_CREATE_RECORD_TABLE = "CREATE TABLE " + RecordEntry.TABLE_NAME + " ("
            + RecordEntry.COLUMN_SUBJECT_ID + " INTEGER, "
            + RecordEntry.COLUMN_STUDENT_ID + " INTEGER, "
            + RecordEntry.COLUMN_DATE + " TEXT NOT NULL, "
            + RecordEntry.COLUMN_SCORE + " INTEGER, "
            + " PRIMARY KEY ( "
            + RecordEntry.COLUMN_STUDENT_ID + ", "
            + RecordEntry.COLUMN_SUBJECT_ID + "));";

    // The create statement for creating user table
    String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " ("
            + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UserEntry.COLUMN_FIRSTNAME + " TEXT NOT NULL, "
            + UserEntry.COLUMN_MIDNAME + " TEXT, "
            + UserEntry.COLUMN_LASTNAME + " TEXT NOT NULL, "
            + UserEntry.COLUMN_EMAIL + " TEXT, "
            + UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL);";


    db.execSQL(SQL_CREATE_STUDENT_TABLE);
    db.execSQL(SQL_CREATE_SUBJECT_TABLE);
    db.execSQL(SQL_CREATE_ENROLL_TABLE);
    db.execSQL(SQL_CREATE_RECORD_TABLE);
    db.execSQL(SQL_CREATE_USER_TABLE);
  }
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
