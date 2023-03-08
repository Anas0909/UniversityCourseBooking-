package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    private Button btn_login_admin,btn_login_instr,btn_login_student, btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn_login_admin = (Button) findViewById(R.id.btn_login_admin);
        btn_login_instr = (Button) findViewById(R.id.btn_login_Instr);
        btn_login_student = (Button) findViewById(R.id.btn_login_student);
        btn_back = findViewById(R.id.btn_login_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMain();
            }
        });
        btn_login_student.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogInStudent();
            }
        });
        btn_login_instr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogInInstructor();
            }
        });
        btn_login_admin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogInAdmin();
            }
        });
    }

    private void OpenMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openLogInAdmin(){
        Intent intent = new Intent(this, LoginAdmin.class);
        startActivity(intent);
    }
    public void openLogInInstructor(){
        Intent intent = new Intent(this, LoginInstructor.class);
        startActivity(intent);
    }
    public void openLogInStudent(){
        Intent intent = new Intent(this, LoginStudent.class);
        startActivity(intent);
    }


}