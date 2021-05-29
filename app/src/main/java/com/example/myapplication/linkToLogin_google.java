package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class linkToLogin_google extends AppCompatActivity {

    Button linkToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_to_login_google);
        linkToLogin = findViewById(R.id.button_Go);
        linkToLogin.setText("Go");

        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(linkToLogin_google.this, MenuActivity.class));
            }
        });


    }
}