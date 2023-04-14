package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.RecyclerView.AnnonceAdapter;
import com.example.RecyclerView.MyAdapter;
import com.example.database_animals.Annonce;
import com.example.database_animals.Pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home_page extends AppCompatActivity {

    private AnnonceAdapter annonceAdapter ;



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





    }
}