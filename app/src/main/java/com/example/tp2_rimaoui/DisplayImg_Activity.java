package com.example.tp2_rimaoui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayImg_Activity extends AppCompatActivity {
    ImageView imagetoBeSent ;
    String imageUriString ;
    Uri imageUri ;
    Button goback , btnSend  ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected_image);
        // initializing the image view :
        imagetoBeSent = findViewById(R.id.imagetoBeSent);
        // getting the
        imageUriString = getIntent().getStringExtra("imageUri");
        imageUri = Uri.parse(imageUriString);
        imagetoBeSent.setImageURI(imageUri);
        goback = findViewById(R.id.goBackbtn) ;
        btnSend = findViewById(R.id.btnSend) ;
    }
    public void onBackButtonClick(View view){
        finish();
    }
    public void onSendButtonClick(View view){
        // calling the function to send the image
        Toast.makeText(getApplicationContext(), "tesssssst", Toast.LENGTH_SHORT).show();
        //finish();
    }
}
