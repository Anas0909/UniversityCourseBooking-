package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Student extends AppCompatActivity {
    private Button btn_signOut, btn_enroll, btn_unEnroll, btn_searchByCode, btn_searchByName, btn_searchByDay,btn_viewClasses ;
    private String username, password;
    private String[] daySelection = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private EditText et_courseCode, et_courseName;
    private ListView lv_showCourses;
    private Spinner sp_day;
    private DataBaseHelper dbHelper = new DataBaseHelper(Student.this);
    private ArrayAdapter courseArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Intent i = getIntent();
        username = i.getStringExtra("Student_userName");
        password = i.getStringExtra("Student_passWord");

        TextView txt_intro = findViewById(R.id.txt_student_intro);
        txt_intro.setText("Hello, Student: " + username);



        btn_enroll = findViewById(R.id.btn_student_enroll);
        btn_unEnroll = findViewById((R.id.btn_student_unenroll));
        btn_searchByCode = findViewById((R.id.btn_student_search_byCode));
        btn_searchByName = findViewById(R.id.btn_student_search_byName);
        btn_searchByDay = findViewById(R.id.btn_student_search_byDay);
        btn_viewClasses = findViewById(R.id.btn_student_view_enrolled_courses);
        et_courseCode = findViewById(R.id.et_student_course_code);
        et_courseName= findViewById(R.id.et_student_course_name);
        lv_showCourses = findViewById(R.id.lv_student_showCourse);
        sp_day = findViewById(R.id.sp_student_day);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(Student.this, android.R.layout.simple_spinner_dropdown_item, daySelection);
        sp_day.setAdapter(dayAdapter);

        viewAllClasses(username, password);

        btn_signOut = (Button) findViewById(R.id.btn_student_sign_out);
        btn_signOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });
        btn_viewClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAllClasses(username,password);
            }
        });
        btn_searchByCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = et_courseCode.getText().toString().trim();
                if(dbHelper.searchCourse(courseCode)){
                    searchByCode(courseCode);
                    Toast.makeText(Student.this, "Course Found", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Student.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_searchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseName = et_courseName.getText().toString().trim();
                if(dbHelper.searchCourseByName(courseName)){
                    searchByName(courseName);
                    Toast.makeText(Student.this, "Course Found", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Student.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollCourse(et_courseCode.getText().toString().trim());
            }
        });
        btn_unEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unEnrollCourse(et_courseCode.getText().toString().trim());
            }
        });

        btn_searchByDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseDay = sp_day.getSelectedItem().toString();
                if(!dbHelper.searchedCourseByDay(courseDay).isEmpty()){
                    searchByDay(courseDay);
                    Toast.makeText(Student.this, "CourseFound", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Student.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void enrollCourse(String code) {
        String courseCode = code;
        String courseName = et_courseName.getText().toString().trim();
        String StudentUsername = username;
        String StudentPassword = password;
        if (et_courseCode.getText().toString().isEmpty() || et_courseName.getText().toString().isEmpty()) {
            Toast.makeText(Student.this, "Input both code and name to enroll!", Toast.LENGTH_SHORT).show();
        }
        else if (dbHelper.searchCourse(courseCode)) {
            if (dbHelper.viewEnrolledCourse(StudentUsername, StudentPassword).contains(courseCode)) {
                Toast.makeText(Student.this, "You already enrolled into the course", Toast.LENGTH_SHORT).show();
            }
            else if(dbHelper.viewEnrolledCourse(StudentUsername, StudentPassword).equals("") || dbHelper.viewEnrolledCourse(StudentUsername, StudentPassword).isEmpty()){
                if(dbHelper.searchCourse(courseCode)&&dbHelper.searchCourseByName(courseName)){
                    dbHelper.enrollCourse(courseCode, courseName, StudentUsername, StudentPassword);
                    Toast.makeText(Student.this, "Course enrolled", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Student.this, "Course Not found!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                boolean Flag = true;
                ArrayList<String> temp = dbHelper.searchedCoursesByCode(courseCode);
                String[] split1 = temp.get(0).split(";");
                String dayEnrolling = split1[3];
                String timeEnrolling = split1[4];
                String [] timeSplit = split1[5].split(" : ");
                int capacity = Integer.parseInt(timeSplit[1]);

                String courseList = dbHelper.viewEnrolledCourse(username, password);
                ArrayList<String> studentNumber = dbHelper.allStudentName(courseCode);
                String[] courseCodes = courseList.split(";");


                for (String s : courseCodes) {
                    ArrayList<String> temp2 = dbHelper.searchedCoursesByCode(s);
                    String[] split2 = temp2.get(0).split(";");
                    String dayEnrolled = split2[3];
                    String timeEnrolled = split2[4];

                    if (dayEnrolled.equals(dayEnrolling) && timeEnrolled.equals(timeEnrolling)) {
                        Flag = false;
                    }
                }
                if(dayEnrolling.equals("Course Days: None") && timeEnrolling.equals("Course Time: None") ){
                    Flag = true;
                }
                if (Flag == false) {
                    Toast.makeText(Student.this, "Course Time conflicts", Toast.LENGTH_SHORT).show();
                }
                else if(studentNumber.size()>=capacity){
                    Toast.makeText(Student.this, "Capacity is Full!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbHelper.enrollCourse(courseCode, courseName, StudentUsername, StudentPassword);
                    Toast.makeText(Student.this, "Course enrolled", Toast.LENGTH_SHORT).show();
                }

            }

        }

        else{
            Toast.makeText(Student.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
        }
        viewAllClasses(username, password);
    }
    public void unEnrollCourse(String code){
        String courseCode = code;
        String courseName = et_courseName.getText().toString().trim();
        String StudentUsername = username;
        String StudentPassword = password;
        if(et_courseCode.getText().toString().isEmpty() || et_courseName.getText().toString().isEmpty()){
            Toast.makeText(Student.this, "Input both code and name to enroll!", Toast.LENGTH_SHORT).show();
        }
        else if(dbHelper.searchCourse(courseCode)){
            ArrayList<String> course = dbHelper.searchedCoursesByCode(courseCode);
            if(course.get(0).contains(courseName)) {
                dbHelper.UnEnrollCourse(courseCode, courseName, StudentUsername, StudentPassword);
                Toast.makeText(Student.this, "Course un-enrolled", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Student.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Student.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
        }
        viewAllClasses(username, password);
    }
    public void searchByCode(String code){
        ArrayList<String> infoArray = dbHelper.searchedCoursesByCode(code);
        courseArrayAdapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_list_item_1, infoArray);
        lv_showCourses.setAdapter(courseArrayAdapter);
    }

    public void searchByName(String name){
        ArrayList<String> infoArray = dbHelper.searchedCoursesByName(name);
        courseArrayAdapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_list_item_1, infoArray);
        lv_showCourses.setAdapter(courseArrayAdapter);
    }
    public void searchByDay(String day){
        ArrayList<String> infoArray = dbHelper.searchedCourseByDay(day);
        courseArrayAdapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_list_item_1, infoArray);
        lv_showCourses.setAdapter(courseArrayAdapter);
    }

    public void viewAllClasses(String username, String password){
        String courseList = dbHelper.viewEnrolledCourse(username, password);
        ArrayList<String> coursesInfo = new ArrayList<>();
        String [] courseCodes = courseList.split(";");

        for (String s : courseCodes){
            ArrayList<String> temp = dbHelper.searchedCoursesByCode(s);
            if(temp.size()!=0) {
                coursesInfo.add(temp.get(0));
            }
        }

        courseArrayAdapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_list_item_1, coursesInfo);
        lv_showCourses.setAdapter(courseArrayAdapter);
    }

}