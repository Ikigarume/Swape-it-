package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchingActivity.this, BoardingActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000); // 3000 milliseconds (3 seconds) delay
    }
}