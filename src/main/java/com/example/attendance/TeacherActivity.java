package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherActivity extends AppCompatActivity {
    EditText TxUsername, TxPassword;
    Button BtnLogin;
    DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity);

        TxUsername = (EditText)findViewById(R.id.txUsername);
        TxPassword = (EditText)findViewById(R.id.txPassword);
        BtnLogin = (Button)findViewById(R.id.btnLogin);

        dbHelper = new DB (this);

        TextView tvCreateAccount = (TextView)findViewById(R.id.tvCreateAccount);

        Toast.makeText(TeacherActivity.this, "I don't have account yet.", Toast.LENGTH_SHORT).show();

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherActivity.this, RegisterActivity.class));
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = TxUsername.getText().toString().trim();
                String password = TxPassword.getText().toString().trim();

               // Boolean res = dbHelper.checkUser(username,password);
              /*  if(res == true){
                    Toast.makeText(TeacherActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeacherActivity.this, LoginActivity.class));
                }else {
                    Toast.makeText(TeacherActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }
}