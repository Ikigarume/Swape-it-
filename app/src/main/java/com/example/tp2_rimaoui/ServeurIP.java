package com.example.tp2_rimaoui;

import android.app.Application;

import com.example.database_animals.Annonce;

import java.util.ArrayList;

public class ServeurIP extends Application {
    private String IPV4_serveur = "10.1.31.146";
    private ArrayList<Annonce> AnnoncesHP ;

    public String getIPV4_serveur() {
        return IPV4_serveur;
    }

    public void setAnnoncesHP(ArrayList<Annonce> Annonces){ this.AnnoncesHP = Annonces ; }

    public ArrayList<Annonce> getAnnoncesHP() { return AnnoncesHP;}


}
