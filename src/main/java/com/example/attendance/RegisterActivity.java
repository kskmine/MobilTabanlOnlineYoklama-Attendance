package com.example.attendance;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

        EditText TxUsername, TxPassword, TxConPassword;
        Button BtnRegister;
        DB dbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            dbHelper = new DB (this);

            TxUsername = (EditText)findViewById(R.id.txUsernameReg);
            TxPassword = (EditText)findViewById(R.id.txPasswordReg);
            TxConPassword = (EditText)findViewById(R.id.txConPassword);
            BtnRegister = (Button)findViewById(R.id.btnRegister);

            TextView tvRegister = (TextView)findViewById(R.id.tvRegister);

            Toast.makeText(RegisterActivity.this, "Back to  .", Toast.LENGTH_SHORT).show();

            tvRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegisterActivity.this, TeacherActivity.class));
                }
            });

            BtnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = TxUsername.getText().toString().trim();
                    String password = TxPassword.getText().toString().trim();
                    String conPassword = TxConPassword.getText().toString().trim();

                    ContentValues values = new ContentValues();


                    if (!password.equals(conPassword)){
                        Toast.makeText(RegisterActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                    }else if (password.equals("") || username.equals("")){
                        Toast.makeText(RegisterActivity.this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
                    }else {
                       // values.put(DB.row_username, username);
                       // values.put(DB.row_password, password);
                   //     DB.insert(values);

                        Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });


        }

}
