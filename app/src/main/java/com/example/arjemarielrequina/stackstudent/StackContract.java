package com.example.arjemarielrequina.stackstudent;

/**
 * Created by Arjemariel Requina on 12/8/2016.
 */
import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/*
  The Contract for the application. These is where the path names, column names and other variables
  that is repeatedly used by the application are being defined. The purpose for this class is to lessen the error
  brought by the repeated initialization of the same variable content on each class. Also, when there will be changes
  such as column name in the database, we will only need to change that column name on this class and the changes
  will reflect throughout all the classes.

*/
public class StackContract {
  private StackContract() {
  }

  // The content authority that will be used on the custom content provider class (StackProvider Class)
  public static final String CONTENT_AUTHORITY = "com.example.arjemarielrequina.stackstudent";

  // The base content uri in which the several paths for URI will be appended that will used in the StackProvider Class
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  // All possible paths for the URIs that will be used in the StackProvider Class
  // These paths are also based on the tables that can be found on the Stack database
  public static final String PATH_STUDENTS = "student";
  public static final String PATH_SUBJECTS = "subject";
  public static final String PATH_RECORDS = "record";
  public static final String PATH_ENROLL = "enrollment";
  public static final String PATH_USER = "user";

  // The following are all the Entry classes that will be used throughout the application.
  // Like the paths, these are all also based on the tables that can be found on the Stack database
  // Each of the Entry classes have their table_name, column_names that will be used in accessing the database
  // Also, each of the Entry class have their respective content_uris that will be used in  the StackProvider class

  // The Entry class for the student table in the database
  public static final class StudentEntry implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STUDENTS);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS;


    public final static String TABLE_NAME = "student";


    public final static String _ID = BaseColumns._ID;

    public final static String COLUMN_FIRSTNAME = "firstname";

    public static final String COLUMN_MIDNAME = "middlename";

    public final static String COLUMN_LASTNAME = "lastname";

    public final static String COLUMN_EMAIL = "email";

    public final static String COLUMN_CONTACT = "contact";

  }

  // The Entry class for the subject table in the database
  public static final class SubjectEntry implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBJECTS);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECTS;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECTS;


    public final static String TABLE_NAME = "subject";

    public final static String _ID = BaseColumns._ID;

    public final static String COLUMN_TITLE = "title";

    public static final String COLUMN_DESCRIPTION = "description";

  }
  // The Entry class for enrollment on the database
  public static final class EnrollEntry implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ENROLL);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ENROLL;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ENROLL;


    public final static String TABLE_NAME = "enrollment";

    public final static String COLUMN_SUBJECT_ID = "subject_id";

    public static final String COLUMN_STUDENT_ID = "student_id";

  }

  // The Entry class for record table in the database
  public static final class RecordEntry implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECORDS);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDS;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDS;


    public final static String TABLE_NAME = "record";

    public final static String COLUMN_SUBJECT_ID = "subject_id";

    public static final String COLUMN_STUDENT_ID = "student_id";

    public static final String COLUMN_DATE = "date";

    public static final String COLUMN_SCORE = "score";

  }

  // The Entry class for the user table in the database.
  public static final class UserEntry implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;


    public final static String TABLE_NAME = "user";

    public final static String _ID = BaseColumns._ID;

    public final static String COLUMN_FIRSTNAME= "firstname";

    public static final String COLUMN_MIDNAME = "middlename";

    public static final String COLUMN_LASTNAME = "lastname";

    public static final String COLUMN_EMAIL = "email";

    public static final String COLUMN_PASSWORD = "password";
  }
}
