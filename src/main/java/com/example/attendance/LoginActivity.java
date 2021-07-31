package com.example.attendance;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btnListele;
    private ListView VeriListele;
    private TextView about;
    FloatingActionButton fabCarpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnListele = (Button) findViewById(R.id.Listele);
        VeriListele = (ListView) findViewById(R.id.veriListele);
        fabCarpi = findViewById(R.id.floatingActionButton4);
        about = findViewById(R.id.about);

        DB dataBase = new DB(LoginActivity.this);
        SQLiteDatabase sqLiteDatabase = dataBase.getReadableDatabase();


        btnListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = dataBase.VeriListele();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,list);
                VeriListele.setAdapter(adapter);
            }
        });

          fabCarpi.setOnClickListener(v -> {
              Intent intent2=new Intent(LoginActivity.this,MainActivity.class);
              startActivity(intent2);

        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String name = bundle.getString("isim");
            String number = bundle.getString("numara");
         //   helloUser.setText(name);
        }
    }
}