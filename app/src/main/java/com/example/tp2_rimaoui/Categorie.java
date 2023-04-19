package com.example.tp2_rimaoui;

public class Categorie {

    private int Id ;
    private String Nom ;

    public Categorie(int Id, String Nom){
        this.Id = Id ;
        this.Nom = Nom ;
    }

    public int getId(){
        return this.Id;
    }
    public String getNom(){
        return this.Nom ;
    }

    public void setNom(String Nom){
        this.Nom = Nom ;
    }
}
