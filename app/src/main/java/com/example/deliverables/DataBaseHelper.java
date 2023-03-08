package com.example.deliverables;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    //Variables for admin database
    private static final String ADMIN_TABLE = "ADMIN_TABLE";
    private static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    private static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    private static final String ADMIN_ID = "ADMIN_ID";
    //Variables for instructor database
    private static final String INSTRUCTOR_TABLE = "INSTRUCTOR_TABLE";
    private static final String INSTRUCTOR_ID = "INSTRUCTOR_ID";
    private static final String INSTRUCTOR_USERNAME = "INSTRUCTOR_USERNAME";
    private static final String INSTRUCTOR_PASSWORD = "INSTRUCTOR_PASSWORD";
    private static final String INSTRUCTOR_TEACHER_NAME = "INSTRUCTOR_TEACHER_NAME";
    //Variables for student database
    private static final String STUDENT_TABLE = "STUDENT_TABLE";
    private static final String STUDENT_ID = "STUDENT_ID";
    private static final String STUDENT_USERNAME = "STUDENT_USERNAME";
    private static final String STUDENT_PASSWORD = "STUDENT_PASSWORD";
    private static final String STUDENT_FULL_NAME = "STUDENT_FULL_NAME";
    private static final String ENROLLED_COURSES = "ENROLLED_COURSES";
    //Variables for course info database
    private static final String COURSE_TABLE = "COURSE_TABLE";
    private static final String COURSE_ID = "COURSE_ID";
    private static final String COURSE_CODE = "COURSE_CODE";
    private static final String COURSE_NAME = "COURSE_NAME";
    private static final String COURSE_INFO = "COURSE_INFO";
    private static final String COURSE_DAYS = "COURSE_DAYS";
    private static final String COURSE_TIME = "COURSE_TIME";
    private static final String STUDENT_CAPACITY = "STUDENT_CAPACITY";
    private static final String ASSIGN_CHECKED = "ASSIGN_CHECKED";




    public DataBaseHelper(@Nullable Context context) {
        super(context, "Project.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //create 4 databases for admin, instructor, student and course info
        String adminDBTableCreate = "CREATE TABLE " + ADMIN_TABLE + " (" + ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ADMIN_USERNAME + " TEXT, " + ADMIN_PASSWORD + " TEXT)";
        db.execSQL(adminDBTableCreate);

        String instructorDBTableCreate = "CREATE TABLE " + INSTRUCTOR_TABLE + " (" + INSTRUCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + INSTRUCTOR_USERNAME + " TEXT, " + INSTRUCTOR_PASSWORD + " TEXT, " + INSTRUCTOR_TEACHER_NAME + " TEXT)";
        db.execSQL(instructorDBTableCreate);

        String studentDBTableCreate = "CREATE TABLE " + STUDENT_TABLE + " (" + STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STUDENT_USERNAME + " TEXT, " + STUDENT_PASSWORD + " TEXT, " + STUDENT_FULL_NAME + " TEXT, " + ENROLLED_COURSES + " TEXT)";
        db.execSQL(studentDBTableCreate);

        String courseDBTableCreate = "CREATE TABLE " + COURSE_TABLE + " (" + COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_CODE + " TEXT, " + COURSE_NAME + " TEXT, " + COURSE_INFO + " TEXT, " + COURSE_DAYS + " TEXT, " + COURSE_TIME + " TEXT, " + STUDENT_CAPACITY + " INT, " + ASSIGN_CHECKED + " TEXT)";
        db.execSQL(courseDBTableCreate);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addAdmin() { //only called once
        SQLiteDatabase adminDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ADMIN_USERNAME, "admin");
        cv.put(ADMIN_PASSWORD, "admin123");
        adminDB.insert(ADMIN_TABLE, null, cv);
    }

    public void addInstructor(String username, String password, String fullName) {
        SQLiteDatabase instrDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(INSTRUCTOR_USERNAME, username);
        cv.put(INSTRUCTOR_PASSWORD, password);
        cv.put(INSTRUCTOR_TEACHER_NAME, fullName);
        instrDB.insert(INSTRUCTOR_TABLE, null, cv);
    }

    public void addStudent(String username, String password, String fullName) {
        SQLiteDatabase studentDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STUDENT_USERNAME, username);
        cv.put(STUDENT_PASSWORD, password);
        cv.put(STUDENT_FULL_NAME, fullName);
        cv.put(ENROLLED_COURSES, "");
        studentDB.insert(STUDENT_TABLE, null, cv);
    }

    public void addCourse(String code, String name, String description, String days, String time, int capacity, String assignChecked) {
        SQLiteDatabase courseDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_CODE, code);
        cv.put(COURSE_NAME, name);
        cv.put(COURSE_INFO, description);
        cv.put(COURSE_DAYS, days);
        cv.put(COURSE_TIME, time);
        cv.put(STUDENT_CAPACITY, capacity);
        cv.put(ASSIGN_CHECKED, assignChecked);
        courseDB.insert(COURSE_TABLE, null, cv);
    }

    public void delInstructor(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delQueryString = "DELETE FROM " + INSTRUCTOR_TABLE + " WHERE " + INSTRUCTOR_USERNAME + " = '" + username + "'";
        db.execSQL(delQueryString);
    }

    public void delStudent(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delQueryString = "DELETE FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = '" + username + "'";
        db.execSQL(delQueryString);
    }

    public void delCourse(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delQueryString = "DELETE FROM " + COURSE_TABLE + " WHERE " + COURSE_CODE + " = '" + code + "'";
        db.execSQL(delQueryString);
    }

    public void updateCourseInfo(String code, String info, String days, String time, int capacity, String assign) { //edit all course info except code and name
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_INFO, info);
        cv.put(COURSE_DAYS, days);
        cv.put(COURSE_TIME, time);
        cv.put(STUDENT_CAPACITY, capacity);
        cv.put(ASSIGN_CHECKED, assign);
        db.update(COURSE_TABLE, cv, "COURSE_CODE=?", new String[]{code});
        db.close();
    }

    public void updateCourseBasic(String oldCode, String newCode, String newName) { // edit only course code and name
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_CODE, newCode);
        cv.put(COURSE_NAME, newName);
        db.update(COURSE_TABLE, cv, "COURSE_CODE=?", new String[]{oldCode});
        db.close();


    }


    public ArrayList<String> allCourses() {
        ArrayList<String> courseContents = new ArrayList<>();//create a Arraylist that converts course information to strings

        String courseQueryString = "SELECT * FROM " + COURSE_TABLE; //SQlite database operation statement, select the course table in database
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(courseQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                String courseCode = cursor.getString(1);
                String courseName = cursor.getString(2);
                String courseInfo = cursor.getString(3);
                String courseDays = cursor.getString(4);
                String courseTime = cursor.getString(5);
                int studentCapacity = cursor.getInt(6);
                String courseAssigned = cursor.getString(7);

                Integer capacity = new Integer(studentCapacity);//convert the capacity to Integer then covert it to string in next line
                courseContents.add("Course Code: " + courseCode + "; Course Name: " + courseName + "; Course Description: " + courseInfo + "; Course Days: " + courseDays + "; Course Time: " +
                        courseTime + "; Course Student Capacity : " + capacity.toString() + "; Assigned Teacher: " + courseAssigned);
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }


        return courseContents;
    }

    public ArrayList<String> allInstrAccounts() {
        ArrayList<String> instrAccountContents = new ArrayList<>();//create a Arraylist that converts instructor information  to strings

        String instrQueryString = "SELECT * FROM " + INSTRUCTOR_TABLE; //SQlite database operation statement, select the instructor account table in database
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(instrQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                String fullname = cursor.getString(3);


                instrAccountContents.add("Insturctor Username: " + username + "; Password: " + password + "; Teacher Name: " + fullname);
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }
        return instrAccountContents;
    }

    public ArrayList<String> allStudentAccounts() {
        ArrayList<String> studentAccountContents = new ArrayList<>();//create a Arraylist that converts student information  to strings

        String studentQueryString = "SELECT * FROM " + STUDENT_TABLE; //SQlite database operation statement, select the student account table in database
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(studentQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                String fullname = cursor.getString(3);


                studentAccountContents.add("Student Username: " + username + "; Password: " + password + "; Student Name: " + fullname);
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }
        return studentAccountContents;
    }

    public boolean searchCourse(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_CODE + " = " + " '" + code + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }

    }

    public boolean searchCourseByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_NAME + " = " + " '" + name + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean searchAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + ADMIN_TABLE + " WHERE " + ADMIN_USERNAME + " = " + " '" + username + "' " + " AND " + ADMIN_PASSWORD + " = " + " '" + password + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean searchInstr(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + INSTRUCTOR_TABLE + " WHERE " + INSTRUCTOR_USERNAME + " = " + " '" + username + "' " + " AND " + INSTRUCTOR_PASSWORD + " = " + " '" + password + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean searchStudent(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = " + " '" + username + "' " + " AND " + STUDENT_PASSWORD + " = " + " '" + password + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public String getAssignTeacher(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_CODE + " = " + " '" + code + "' ";
        Cursor cursor = db.rawQuery(searchQuery, null);
        String assignTeacher = "";
        if (cursor.moveToFirst()) {
            assignTeacher = cursor.getString(7);
        }
        return assignTeacher;
    }

    public String getTeacherNameFromUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + INSTRUCTOR_TABLE + " WHERE " + INSTRUCTOR_USERNAME + " = " + " '" + username + "' ";
        Cursor cursor = db.rawQuery(searchQuery, null);
        String TeacherName = "";
        if (cursor.moveToFirst()) {
            TeacherName = cursor.getString(3);
        }
        return TeacherName;
    };

    public ArrayList<String> searchedCoursesByCode(String code) {
        ArrayList<String> courseContents = new ArrayList<>();

        String searchQueryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_CODE + " = " + " '" + code + "' ";
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(searchQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                String courseCode = cursor.getString(1);
                String courseName = cursor.getString(2);
                String courseInfo = cursor.getString(3);
                String courseDays = cursor.getString(4);
                String courseTime = cursor.getString(5);
                int studentCapacity = cursor.getInt(6);
                String courseAssigned = cursor.getString(7);

                Integer capacity = new Integer(studentCapacity);//convert the capacity to Integer then covert it to string in next line
                courseContents.add("Course Code: " + courseCode + "; Course Name: " + courseName + "; Course Description: " + courseInfo + "; Course Days: " + courseDays + "; Course Time: " +
                        courseTime + "; Course Student Capacity : " + capacity.toString() + "; Assigned Teacher: " + courseAssigned);
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }


        return courseContents;
    }

    public ArrayList<String> searchedCoursesByName(String name) {
        ArrayList<String> courseContents = new ArrayList<>();

        String searchQueryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_NAME + " = " + " '" + name + "' ";
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(searchQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                String courseCode = cursor.getString(1);
                String courseName = cursor.getString(2);
                String courseInfo = cursor.getString(3);
                String courseDays = cursor.getString(4);
                String courseTime = cursor.getString(5);
                int studentCapacity = cursor.getInt(6);
                String courseAssigned = cursor.getString(7);

                Integer capacity = new Integer(studentCapacity);//convert the capacity to Integer then covert it to string in next line
                courseContents.add("Course Code: " + courseCode + "; Course Name: " + courseName + "; Course Description: " + courseInfo + "; Course Days: " + courseDays + "; Course Time: " +
                        courseTime + "; Course Student Capacity : " + capacity.toString() + "; Assigned Teacher: " + courseAssigned);
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }


        return courseContents;
    }

    public ArrayList<String> searchedCourseByDay(String day){
        ArrayList<String> courseContents = new ArrayList<>();

        String searchQueryString = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_DAYS + " = " + " '" + day + "' ";
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(searchQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                String courseCode = cursor.getString(1);
                String courseName = cursor.getString(2);
                String courseInfo = cursor.getString(3);
                String courseDays = cursor.getString(4);
                String courseTime = cursor.getString(5);
                int studentCapacity = cursor.getInt(6);
                String courseAssigned = cursor.getString(7);

                Integer capacity = new Integer(studentCapacity);//convert the capacity to Integer then covert it to string in next line
                courseContents.add("Course Code: " + courseCode + "; Course Name: " + courseName + "; Course Description: " + courseInfo + "; Course Days: " + courseDays + "; Course Time: " +
                        courseTime + "; Course Student Capacity : " + capacity.toString() + "; Assigned Teacher: " + courseAssigned);
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }


        return courseContents;
    }

    public void enrollCourse(String code, String name,  String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = " + " '" + username + "' " + " AND " + STUDENT_PASSWORD + " = " + " '" + password + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            String searchQueryString1 = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = " + " '" + username + "' " + " AND " + STUDENT_PASSWORD + " = " + " '" + password + "' ";
            String searchQueryString2 = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_CODE + " = " + " '" + code + "' "+ " AND " + COURSE_NAME + " = " + " '" + name + "' ";
            String courseEnrolled= "";
            String courseCode= "";
            Cursor cursor1 = db.rawQuery(searchQueryString1, null);
            if(cursor1.moveToNext()){
                courseEnrolled = cursor1.getString(4);
            }
            Cursor cursor2 = db.rawQuery(searchQueryString2, null);
            if(cursor2.moveToNext()){
                courseCode = cursor2.getString(1);
            }
            if (!courseEnrolled.contains(courseCode)){
                ContentValues cv = new ContentValues();
                cv.put(ENROLLED_COURSES,courseEnrolled + code + ";" );
                db.update(STUDENT_TABLE, cv, "STUDENT_USERNAME=?", new String[]{username});
            }
        }
    }
    public void UnEnrollCourse(String code, String name, String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQueryString = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = " + " '" + username + "' " + " AND " + STUDENT_PASSWORD + " = " + " '" + password + "' ";
        Cursor cursor = db.rawQuery(searchQueryString, null);
        if (cursor.moveToFirst()) {
            String searchQueryString1 = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = " + " '" + username + "' " + " AND " + STUDENT_PASSWORD + " = " + " '" + password + "' ";
            String searchQueryString2 = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COURSE_CODE + " = " + " '" + code + "' "+ " AND " + COURSE_NAME + " = " + " '" + name + "' ";
            String courseEnrolled= "";
            String courseCode= "";
            Cursor cursor1 = db.rawQuery(searchQueryString1, null);
            if(cursor1.moveToNext()){
                courseEnrolled = cursor1.getString(4);
            }
            Cursor cursor2 = db.rawQuery(searchQueryString2, null);
            if(cursor2.moveToNext()){
                courseCode = cursor2.getString(1);
            }
            if (courseEnrolled.contains(courseCode)){
                ContentValues cv = new ContentValues();
                String newEnrolledCourses = courseEnrolled.replace(courseCode +";", "");
                cv.put(ENROLLED_COURSES,newEnrolledCourses );
                db.update(STUDENT_TABLE, cv, "STUDENT_USERNAME=?", new String[]{username});
            }
        }
    }

    public String viewEnrolledCourse(String username, String password){
        String searchQueryString = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_USERNAME + " = " + " '" + username + "' " + " AND " + STUDENT_PASSWORD + " = " + " '" + password + "' ";
        SQLiteDatabase db = this.getReadableDatabase(); //open the database
        Cursor cursor = db.rawQuery(searchQueryString, null);
        String courseEnrolled ="";
        if(cursor.moveToFirst()){
            courseEnrolled = cursor.getString(4);
        }
        return courseEnrolled;
    }

    public ArrayList<String> allStudentName(String code) {
        ArrayList<String> studentNameContents = new ArrayList<>();//create a Arraylist that converts student information  to strings

        String studentQueryString = "SELECT * FROM " + STUDENT_TABLE; //SQlite database operation statement, select the student account table in database
        SQLiteDatabase db = this.getReadableDatabase(); //open the database

        Cursor cursor = db.rawQuery(studentQueryString, null); //create a cursor pointing to current selected row
        if (cursor.moveToFirst()) { //if the table has at least one set of data
            do {
                if(cursor.getString(4).contains(code)){
                studentNameContents.add(cursor.getString(3) +" ");
                }
            }
            while (cursor.moveToNext()); //loop through all available sets of data in the table
        }
        return studentNameContents;
    }








}


