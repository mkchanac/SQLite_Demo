package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentDb";
    private static final String TABLE_STUDENTS = "students";   // Table name
    private static final String KEY_ID = "id";        // column-id
    private static final String KEY_NAME = "name";    // column-name
    private static final String KEY_STUDENT_ID = "student_id";    // column-student_id

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_STUDENT_ID + " TEXT" + ")";
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }
    // code to add the new student
    void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName()); // Contact Name
        values.put(KEY_STUDENT_ID, student.getStudentId()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_STUDENTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single student
    Student getStudent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STUDENTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_STUDENT_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Student student = new Student(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        return student;
    }

    // code to get all students in a list view
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<Student>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STUDENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(Integer.parseInt(cursor.getString(0)));
                student.setName(cursor.getString(1));
                student.setStudentId(cursor.getString(2));
                // Adding student to list
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        // return student list
        return studentList;
    }

    // code to update the single student
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName());
        values.put(KEY_STUDENT_ID, student.getStudentId());

        // updating row
        return db.update(TABLE_STUDENTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
    }

    // Deleting single student
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, KEY_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
        db.close();
    }

    // Deleting all students
    public void deleteAllStudents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, "", null);
        db.close();
    }

    // Getting students Count
    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
