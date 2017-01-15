package com.example.arjemarielrequina.stackstudent;

/**
 * Created by Arjemariel Requina on 12/8/2016.
 */
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.arjemarielrequina.stackstudent.StackContract.*;
/**
 * The Custom Content Provider Class (StackProvider) of the application. This serves as the bridge of the application in accessing
 * the database. This content provider also serves as preparation of the application in sharing data with other application in
 * the future.
 */
public class StackProvider extends ContentProvider {
  public static final String LOG_TAG = StackProvider.class.getSimpleName();
  private static final int STUDENT = 10;
  private static final int STUDENT_ID = 11;
  private static final int SUBJECT = 20;
  private static final int SUBJECT_ID = 21;
  private static final int USER = 30;
  private static final int USER_ID = 31;
  private static final int RECORD = 40;
  private static final int ENROLL = 50;
  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_STUDENTS, STUDENT);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_STUDENTS + "/#", STUDENT_ID);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_SUBJECTS, SUBJECT);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_SUBJECTS + "/#", SUBJECT_ID);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_USER, USER);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_USER + "/#", USER_ID);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_RECORDS, RECORD);
    sUriMatcher.addURI(StackContract.CONTENT_AUTHORITY, StackContract.PATH_ENROLL, ENROLL);
  }
  private StackDatabase stackdb;
  @Override
  public boolean onCreate() {
    stackdb = new StackDatabase(getContext());
    return true;
  }
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case STUDENT:
        return StudentEntry.CONTENT_LIST_TYPE;
      case STUDENT_ID:
        return StudentEntry.CONTENT_ITEM_TYPE;
      case SUBJECT :
        return SubjectEntry.CONTENT_LIST_TYPE;
      case SUBJECT_ID :
        return SubjectEntry.CONTENT_ITEM_TYPE;
      case USER:
        return UserEntry.CONTENT_LIST_TYPE;
      case USER_ID:
        return UserEntry.CONTENT_ITEM_TYPE;
      case RECORD:
        return RecordEntry.CONTENT_LIST_TYPE;
      case ENROLL:
        return EnrollEntry.CONTENT_LIST_TYPE;
      default:
        throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
    }
  }
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    SQLiteDatabase database = stackdb.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    long id = -1;
    switch (match) {
      case STUDENT:
         id = database.insert(StudentEntry.TABLE_NAME, null, values);
         return ContentUris.withAppendedId(uri, id);
      case SUBJECT:
        id = database.insert(SubjectEntry.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
      case USER:
        id = database.insert(UserEntry.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
      case RECORD:
        id = database.insert(RecordEntry.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
      case ENROLL:
        id = database.insert(EnrollEntry.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
      default:
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }
  }
  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {
    final int match = sUriMatcher.match(uri);
    SQLiteDatabase database = stackdb.getWritableDatabase();
    switch (match) {
      case STUDENT:
        return database.update(StudentEntry.TABLE_NAME, values, selection, selectionArgs);
      case STUDENT_ID:
        selection = StudentEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        return database.update(StudentEntry.TABLE_NAME, values, selection, selectionArgs);
      case SUBJECT:
        return database.update(SubjectEntry.TABLE_NAME, values, selection, selectionArgs);
      case SUBJECT_ID:
        selection = SubjectEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        return database.update(SubjectEntry.TABLE_NAME, values, selection, selectionArgs);
      case USER:
        return database.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
      case USER_ID:
        selection = UserEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        return database.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
      case RECORD:
        return database.update(RecordEntry.TABLE_NAME, values, selection, selectionArgs);
      case ENROLL:
        return database.update(EnrollEntry.TABLE_NAME, values, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
    }
  }
  public int delete(Uri uri, String selection, String[] selectionArgs) {

    SQLiteDatabase database = stackdb.getWritableDatabase();

    final int match = sUriMatcher.match(uri);
    switch (match) {
      case STUDENT:
        return database.delete(StudentEntry.TABLE_NAME, selection, selectionArgs);
      case STUDENT_ID:
        selection = StudentEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        return database.delete(StudentEntry.TABLE_NAME, selection, selectionArgs);
      case SUBJECT:
        return database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
      case SUBJECT_ID:
        selection = SubjectEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        return database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
      case USER:
        return database.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
      case USER_ID:
        selection = UserEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        return database.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
      case RECORD:
        return database.delete(RecordEntry.TABLE_NAME, selection, selectionArgs);
      case ENROLL:
        return database.delete(EnrollEntry.TABLE_NAME, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Deletion is not supported for " + uri);
    }
  }
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) {

    SQLiteDatabase database = stackdb.getReadableDatabase();
    Cursor cursor;
    int match = sUriMatcher.match(uri);
    switch (match) {
      case STUDENT:
        cursor = database.query(StudentEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case STUDENT_ID:
        selection = StudentEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        cursor = database.query(StudentEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case SUBJECT:
        cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case SUBJECT_ID:
        selection = SubjectEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case USER:
        cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case USER_ID:
        selection = UserEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case RECORD:
        cursor = database.query(RecordEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      case ENROLL:
        cursor = database.query(EnrollEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
    }
    return cursor;
   }

}
