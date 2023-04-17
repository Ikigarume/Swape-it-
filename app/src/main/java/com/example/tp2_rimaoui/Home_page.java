package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.RecyclerView.AnnonceAdapter;
import com.example.database_animals.Annonce;

import java.util.ArrayList;
import java.util.Arrays;

public class Home_page extends AppCompatActivity {

    private AnnonceAdapter annonceAdapter ;
    private Button new_post ;



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
        annonceAdapter = new AnnonceAdapter(this, Annonces);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(annonceAdapter);
        new_post = findViewById(R.id.newPost);

        new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewPost_Activity.class);
                startActivity(intent);
            }
        });







    }
}