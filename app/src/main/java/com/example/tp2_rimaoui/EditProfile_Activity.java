package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfile_Activity extends AppCompatActivity {

    private SessionManager sessionManager ;
    //private EditText pseudo ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sessionManager = new SessionManager(this);
        //pseudo = (EditText) findViewById(R.id.pseudo_editText) ;

        //Intent intent = getIntent();
/*
        if(intent != null) {
            pseudo.setHint(intent.getExtras().getString("pseudo"));
        }
        else {

        }*/
        //pseudo.setHint("edit your pseudo here");

    }
    public void goingBack(View v){ onBackPressed();}
}
