package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginInstructor extends AppCompatActivity {
    private Button btn_login, btn_back;
    private EditText et_username, et_password;
    private DataBaseHelper dbHelper = new DataBaseHelper(LoginInstructor.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_instructor);
        btn_login = (Button) findViewById(R.id.btn_login_instr_login);
        btn_back = findViewById(R.id.btn_login_instr_back);
        et_username = findViewById(R.id.et_login_instr_username);
        et_password = findViewById(R.id.et_login_instr_password);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginInstructor.this, "Please Enter Both Fields", Toast.LENGTH_SHORT).show();
                }
                else if(dbHelper.searchInstr(username, password)){

                    openInstr();
                }
                else {
                    Toast.makeText(LoginInstructor.this, "Wrong Username Or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openInstr(){
        Intent intent = new Intent(this, Instr.class);
        intent.putExtra("Instr_userName", et_username.getText().toString());
        startActivity(intent);
    }
    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
