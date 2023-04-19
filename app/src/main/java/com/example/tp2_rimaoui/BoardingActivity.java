package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BoardingActivity extends AppCompatActivity {

    private String option ;
    private Button button_create ;
    private TextView button_login ;
    private SessionManager  sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);

        button_create = findViewById(R.id.button_create);
        button_login = findViewById(R.id.button_login);

        sessionManager = new SessionManager(this);

        if(sessionManager.isLogged()){
            Intent intent = new Intent(this, Home_page.class);
            startActivity(intent);
            finish();
        }

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick(view);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick(view);
            }
        });
    }

    private void myClick(View view) {
        switch (view.getId()){
            case R.id.button_create:
                option = "NewAccount_Fragment" ;
                break;
            case R.id.button_login:
                option= "Login_Fragment" ;
                break ;
        }
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("fragment",option);
        this.startActivity(intent);
    }
}