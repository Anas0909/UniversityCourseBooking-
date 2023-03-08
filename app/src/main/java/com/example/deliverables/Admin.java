package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {
    private Button btn_signOut, btnCreateCourse,  btnDeleteCourse, btnDeleteStudent, btnDeleteInstr, btnEditCourseInfo;
    private ListView lv_course, lv_instr, lv_student;
    private EditText et_courName, et_courseCode, et_newCourseName, et_newCourseCode, et_userName,et_password;
    private ArrayAdapter courseArrayAdapter;
    public DataBaseHelper dbHelper = new DataBaseHelper(Admin.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btn_signOut = (Button) findViewById(R.id.btn_admin_sign_out);    //set buttons to corresponding buttons on design page
        btnCreateCourse = (Button) findViewById(R.id.btn_admin_CreateCourse);
        btnEditCourseInfo = (Button) findViewById(R.id.btn_admin_editCourse);
        btnDeleteCourse = (Button) findViewById(R.id.btn_admin_deleteCourse);
        btnDeleteStudent = (Button) findViewById(R.id.btn_admin_deleteStudent);
        btnDeleteInstr = (Button) findViewById(R.id.btn_admin_deleteInstr);

        lv_course = (ListView) findViewById(R.id.lv_admin_viewAllCourse); //set list views
        lv_instr = (ListView) findViewById(R.id.lv_admin_view_all_Instr);
        lv_student = (ListView) findViewById(R.id.lv_admin_view_all_student);

        et_courName = (EditText) findViewById(R.id.et_admin_course_name1); //set editableText
        et_courseCode = (EditText) findViewById(R.id.et_admin_course_code1);
        et_newCourseName = (EditText) findViewById(R.id.et_admin_course_name2);
        et_newCourseCode = (EditText) findViewById(R.id.et_admin_course_code2);
        et_userName = findViewById(R.id.et_admin_username);
        et_password = findViewById(R.id.et_admin_password);
        ShowAllCourseOnList(dbHelper);
        ShowAllInstrOnList(dbHelper);
        ShowAllStudentOnList(dbHelper);


        //ClickListeners for each btn
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = et_courseCode.getText().toString().trim();
                String courseName = et_courName.getText().toString().trim();
                if (courseCode.isEmpty() || courseName.isEmpty()) {
                    Toast.makeText(Admin.this, "Please Enter both Course Code and Course Name", Toast.LENGTH_SHORT).show();
                }
                else if(dbHelper.searchCourse(courseCode)||dbHelper.searchCourseByName(courseName)){
                    Toast.makeText(Admin.this, "There's already a course with same code or name", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbHelper.addCourse(courseCode, courseName, "None", "None", "None", 0, "None");
                    ShowAllCourseOnList(dbHelper);
                    Toast.makeText(Admin.this, "Course Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = et_courseCode.getText().toString().trim();
                if(courseCode.isEmpty()){
                    Toast.makeText(Admin.this, "Please Enter a Valid Course Code", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.searchCourse(courseCode)){
                    dbHelper.delCourse(courseCode);
                    ShowAllCourseOnList(dbHelper);
                    Toast.makeText(Admin.this, "Course Course Deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Admin.this,  " Course Not Found", Toast.LENGTH_SHORT).show();

                }


            }
        });
        btnEditCourseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCourseCode = et_newCourseCode.getText().toString().trim();
                String newCourseName = et_newCourseName.getText().toString().trim();
                String oldCourseCode = et_courseCode.getText().toString().trim();
                if (newCourseCode.isEmpty() || newCourseName.isEmpty()) {
                    Toast.makeText(Admin.this, "Please Enter both new Course Code and new Course Name", Toast.LENGTH_SHORT).show();
                }
                else if(!dbHelper.searchCourse(oldCourseCode)){
                    Toast.makeText(Admin.this, "Course not found", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbHelper.updateCourseBasic(oldCourseCode, newCourseCode,newCourseName );
                    ShowAllCourseOnList(dbHelper);
                    Toast.makeText(Admin.this, "Course Info Edited Successfully", Toast.LENGTH_SHORT).show();

                }


            }
        });
        btnDeleteInstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String instrUsername = et_userName.getText().toString().trim();
                String instrPassword = et_password.getText().toString().trim();
                if(instrUsername.isEmpty()||instrPassword.isEmpty()){
                    Toast.makeText(Admin.this, "Please Enter both Username and Password", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.searchInstr(instrUsername, instrPassword)){
                    dbHelper.delInstructor(instrUsername);
                    ShowAllInstrOnList(dbHelper);
                    Toast.makeText(Admin.this, "Instructor Deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Admin.this,  "Insturctor Not Found", Toast.LENGTH_SHORT).show();

                }

            }
        });
        btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stuUsername = et_userName.getText().toString().trim();
                String stuPassword = et_password.getText().toString().trim();
                if(stuUsername.isEmpty()||stuPassword.isEmpty()){
                    Toast.makeText(Admin.this, "Please Enter both Username and Password", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.searchStudent(stuUsername, stuPassword)){
                    dbHelper.delStudent(stuUsername);
                    ShowAllStudentOnList(dbHelper);;
                    Toast.makeText(Admin.this, "Student Deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Admin.this,  "Student Not Found", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void ShowAllCourseOnList(DataBaseHelper dbHelper) { //show the entire course list


        ArrayList<String> infoArray = dbHelper.allCourses();
        courseArrayAdapter = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_list_item_1, infoArray);
        lv_course.setAdapter(courseArrayAdapter);
    }


    private void ShowAllInstrOnList(DataBaseHelper dbHelper) {
        ArrayList<String> infoArray = dbHelper.allInstrAccounts();
         courseArrayAdapter  = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_list_item_1,infoArray );
        lv_instr.setAdapter(courseArrayAdapter);
    }

    private void ShowAllStudentOnList(DataBaseHelper dbHelper){
        ArrayList<String> infoArray = dbHelper.allStudentAccounts();
        courseArrayAdapter  = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_list_item_1,infoArray );
        lv_student.setAdapter(courseArrayAdapter);

    }
}