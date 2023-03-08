package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private Button btn_sign_instr,btn_sign_student,btn_back;
    private EditText et_sign_username, et_sign_password, et_full_name;
    private DataBaseHelper dbHelper = new DataBaseHelper(SignUp.this);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        
        et_sign_username = (EditText) findViewById(R.id.et_signUp_Username);
        et_sign_password = (EditText) findViewById(R.id.et_signUp_Password);
        et_full_name = findViewById(R.id.sign_up_et_full_name);

        
       
        btn_back = findViewById(R.id.btn_signUp_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });
        btn_sign_instr = (Button) findViewById(R.id.btn_signUp_sign_Instr);
        btn_sign_instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sign_username = et_sign_username.getText().toString();
                String sign_password = et_sign_password.getText().toString();
                String sign_fullname = et_full_name.getText().toString();

                if(sign_username.isEmpty() || sign_password.isEmpty() || sign_fullname.isEmpty()){
                    Toast.makeText(SignUp.this, "Please Enter both Username and Password to Sign Up", Toast.LENGTH_SHORT).show();
                }
                else if(dbHelper.searchInstr(sign_username, sign_password)){
                    Toast.makeText(SignUp.this, "Try a new user name or password!", Toast.LENGTH_SHORT).show();
                }
                else {

                    dbHelper.addInstructor(sign_username, sign_password,sign_fullname );
                    Toast.makeText(SignUp.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                    openInstr();
                }
            }
        });



        btn_sign_student = (Button) findViewById(R.id.btn_signUp_sign_student);
        btn_sign_student.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String sign_username = et_sign_username.getText().toString();
                String sign_password = et_sign_password.getText().toString();
                String sign_fullname = et_full_name.getText().toString();

                if(sign_username.isEmpty() || sign_password.isEmpty() || sign_fullname.isEmpty()){
                    Toast.makeText(SignUp.this, "Please Enter both Username and Password to Sign Up", Toast.LENGTH_SHORT).show();
                }
                else if(dbHelper.searchStudent(sign_username, sign_password)){
                    Toast.makeText(SignUp.this, "Try a new user name or password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbHelper.addStudent(sign_username, sign_password, sign_fullname);
                    Toast.makeText(SignUp.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                    openStudent();
                }

            }
        });
    }

    public void openInstr(){
        Intent intent = new Intent(this, Instr.class);
        intent.putExtra("Instr_userName", et_sign_username.getText().toString());
        intent.putExtra("teacherName", et_full_name.getText().toString());
        startActivity(intent);
    }
    public void openStudent(){
        Intent intent = new Intent(this, Student.class);
        intent.putExtra("Student_userName",et_sign_username.getText().toString());
        intent.putExtra("Student_passWord", et_sign_password.getText().toString());
        startActivity(intent);
    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    };

}