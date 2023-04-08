package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        String option = intent.getStringExtra("fragment");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(option.equals("NewAccount_Fragment")){
            NewAccount_Fragment yourFragment = new NewAccount_Fragment();
            fragmentTransaction.add(R.id.fragment_container, yourFragment);
        } else if (option.equals("Login_Fragment")){
            Login_Fragment yourFragment = new Login_Fragment();
            fragmentTransaction.add(R.id.fragment_container, yourFragment);
        }
        fragmentTransaction.commit();
    }
}