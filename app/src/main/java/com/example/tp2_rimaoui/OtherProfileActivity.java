package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class OtherProfileActivity extends AppCompatActivity {

    private Button messageButton ;
    private SessionManager sessionManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        messageButton = findViewById(R.id.MessageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Message_Activity.class);
                startActivity(intent);
            }
        });

        RecyclerView rv =findViewById(R.id.offers_list);
        sessionManager = new SessionManager(this);



    }
}
