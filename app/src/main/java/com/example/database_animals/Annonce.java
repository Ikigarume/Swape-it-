package com.example.database_animals;

public class Annonce {


    private String login_utilisateur;
    private float note ;
    private int id_categorie ;
    private String titre ;
    private String description ;

    public Annonce(String login_utilisateur, float note, String titre, String description){
        this.description = description;
        this.login_utilisateur = login_utilisateur;
        this.note = note;
        this.titre = titre ;
    }

    public String getLogin_utilisateur(){
        return this.login_utilisateur;
    }

    public String getTitre(){
        return this.titre;
    }

    public String getDescription(){
        return this.description;
    }

    public float getNote(){
        return this.note;
    }





}
