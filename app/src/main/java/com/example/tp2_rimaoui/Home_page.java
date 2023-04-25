package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.RecyclerView.AnnonceAdapter;
import com.example.database_animals.Annonce;

import java.util.ArrayList;
import java.util.Arrays;

public class Home_page extends AppCompatActivity {

    private AnnonceAdapter annonceAdapter ;
    private Button new_post ;
    private SessionManager  sessionManager ;
    private ImageView profile_image ;



    private ArrayList Annonces = new ArrayList<>(Arrays.asList(
            new Annonce("Ahmed",3, "Nouveau chapeau","Chapeau encore neuf"),
            new Annonce("lila",2, "Nouvelle chaussure","Chapeau encore neuf"),
            new Annonce("Sara",4, "Nouveau monteau","Monteau encore neuf")
             ));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView rv = findViewById(R.id.list);
        sessionManager = new SessionManager(this);
        annonceAdapter = new AnnonceAdapter(this, Annonces);
        rv.setLayoutManager(new LinearLayoutManager(this));
        profile_image = findViewById(R.id.profile_image);
        rv.setAdapter(annonceAdapter);
        new_post = findViewById(R.id.newPost);


        if(sessionManager.isLogged()) {
            String id_user = sessionManager.getId();
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                //startActivity(intent);
            }
        });


        new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewPost_Activity.class);
                startActivity(intent);
            }
        });



    }
}