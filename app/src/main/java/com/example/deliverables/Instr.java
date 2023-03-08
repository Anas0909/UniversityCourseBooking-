package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Instr extends AppCompatActivity {
    private Button btn_instr_sign_out, btn_assign, btn_unAssign, btn_Edit, btn_search_byCode, btn_search_byName;
    private String userName, teacherName;
    private TextView tv_Intro;
    private ListView lv_Allcourse, lv_searchCourse;
    private EditText et_courseCode, et_courseName;
    private DataBaseHelper dbHelper = new DataBaseHelper(Instr.this);
    private ArrayAdapter courseArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instr);
        btn_instr_sign_out = (Button) findViewById(R.id.btn_instr_sign_out);
        btn_assign = (Button) findViewById(R.id.btn_instr_assign);
        btn_unAssign = (Button) findViewById(R.id.btn_instr_unassign);
        btn_Edit = (Button) findViewById(R.id.btn_instr_edit);
        btn_search_byCode = findViewById(R.id.btn_instr_search_bycode);
        btn_search_byName = findViewById(R.id.btn_instr_search_byname);

        lv_Allcourse= findViewById(R.id.lv_instr_view_all_course);
        lv_searchCourse = findViewById(R.id.lv_instr_search_course);
        et_courseCode = findViewById(R.id.et_instr_course_code);
        et_courseName = findViewById(R.id.et_instr_course_name);
        tv_Intro = findViewById(R.id.txt_instr_intro);

        Intent i = getIntent();
        userName = i.getStringExtra("Instr_userName");
        ShowAllCourseOnList(dbHelper);
        if(userName == null){
            teacherName = i.getStringExtra("teacherName");
        }
        else{
            teacherName = dbHelper.getTeacherNameFromUsername(userName);
        }

        tv_Intro.setText("Hello! Instructor : " + teacherName);

        btn_instr_sign_out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });
        btn_search_byCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = et_courseCode.getText().toString().trim();
                if(dbHelper.searchCourse(courseCode)){
                    ShowCourseByCode(dbHelper, courseCode);
                    Toast.makeText(Instr.this, "CourseFound", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Instr.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }
                ShowAllCourseOnList(dbHelper);
            }
        });
        btn_search_byName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseName = et_courseName.getText().toString().trim();
                if(dbHelper.searchCourseByName(courseName)){
                    ShowCourseByName(dbHelper, courseName);
                    Toast.makeText(Instr.this, "CourseFound", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(Instr.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }
                ShowAllCourseOnList(dbHelper);
            }
        });
        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_courseCode.getText().toString().trim();

                if(code.isEmpty()){
                    Toast.makeText(Instr.this, "Please input a course code!", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.searchCourse(code)){
                    String assignedTeacher = assignInstructorCheck(code);
                    String currentTeacher = teacherName;
                    if(assignedTeacher.equals("None")){
                        openAssignInfo();
                    }
                    else if(!assignedTeacher.equals(currentTeacher)){
                        Toast.makeText(Instr.this, "The course has already been signed by existing Teacher, " + assignedTeacher, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Instr.this, "You are already assigned into this course!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Instr.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_courseCode.getText().toString().trim();
                String assignedTeacher = assignInstructorCheck(code);
                String currentTeacher = teacherName;
                if(code.isEmpty()){
                    Toast.makeText(Instr.this, "Please input a course code!", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.searchCourse(code)){
                    if(assignedTeacher.equals(currentTeacher)){
                        openAssignInfo();
                    }
                    else{
                        Toast.makeText(Instr.this, "You can not edit the course unless you are assigned into it", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Instr.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_unAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_courseCode.getText().toString().trim();
                String assignedTeacher = assignInstructorCheck(code);
                String currentTeacher = teacherName;
                if(code.isEmpty()){
                    Toast.makeText(Instr.this, "Please input a course code!", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.searchCourse(code)){
                    if(assignedTeacher.equals(currentTeacher)){
                        dbHelper.updateCourseInfo(code, "None", "None", "None", 0, "None");
                    }
                    else{
                        Toast.makeText(Instr.this, "You can not un-assign from the course unless you are assigned into it", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Instr.this, "Course Not Found!", Toast.LENGTH_SHORT).show();
                }
                ShowAllCourseOnList(dbHelper);
            }
        });
        lv_Allcourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> infoArray = new ArrayList<>();
                String selectedFromList =(String) (lv_Allcourse.getItemAtPosition(i));
                infoArray.add(selectedFromList);
                String[]split1 = infoArray.get(0).split(";");
                String []split2 = split1[0].split(": ");
                String codeToSearch = split2[1];
                if(teacherName.equals(dbHelper.getAssignTeacher(codeToSearch))){
                    ArrayList<String> infoArray2 = dbHelper.allStudentName(codeToSearch);
                    courseArrayAdapter = new ArrayAdapter<String>(Instr.this, android.R.layout.simple_list_item_1,infoArray2);
                    lv_searchCourse.setAdapter(courseArrayAdapter);
                }
                else{
                    Toast.makeText(Instr.this, "You can not view the students unless assigned into the course", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void ShowAllCourseOnList(DataBaseHelper dbHelper) { //show the entire course list
        ArrayList<String> infoArray = dbHelper.allCourses();
        courseArrayAdapter = new ArrayAdapter<String>(Instr.this, android.R.layout.simple_list_item_1, infoArray);
        lv_Allcourse.setAdapter(courseArrayAdapter);
    }

    public void openAssignInfo(){
        Intent intent = new Intent(this, AssignInfo.class);
        intent.putExtra("teacherName", teacherName);
        intent.putExtra("courseCode", et_courseCode.getText().toString());
        startActivity(intent);
    }

    private String assignInstructorCheck(String code) {
            String assignedTeacher = dbHelper.getAssignTeacher(code);
            return assignedTeacher;
    }

    private void ShowCourseByCode(DataBaseHelper dbHelper, String code) {
        ArrayList<String> infoArray = dbHelper.searchedCoursesByCode(code);
        courseArrayAdapter = new ArrayAdapter<String>(Instr.this, android.R.layout.simple_list_item_1, infoArray);
        lv_searchCourse.setAdapter(courseArrayAdapter);
    }

    private void ShowCourseByName(DataBaseHelper dbHelper, String name) {
        ArrayList<String> infoArray = dbHelper.searchedCoursesByName(name);
        courseArrayAdapter = new ArrayAdapter<String>(Instr.this, android.R.layout.simple_list_item_1, infoArray);
        lv_searchCourse.setAdapter(courseArrayAdapter);
    }
}