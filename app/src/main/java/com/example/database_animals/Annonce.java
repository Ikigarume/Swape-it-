package com.example.database_animals;

public class Annonce {


    private String login;
    private String photo_de_profil ;
    private String chemin_image ;
    private String titre ;
    private String description ;
    private float note ;
    private int nbr_vote ;

    public Annonce(String login, String photo_de_profil, String chemin_image, String titre, String description,float note, int nbr_vote){
        this.description = description;
        this.photo_de_profil = photo_de_profil;
        this.chemin_image = chemin_image;
        this.login = login;
        this.note = note;
        this.titre = titre ;
        this.nbr_vote = nbr_vote;
    }

    public String getLogin_utilisateur(){
        return this.login;
    }
    public String getPhoto_de_profil() { return this.photo_de_profil;}
    public String getChemin_image() {return this.chemin_image;}
    public String getTitre(){
        return this.titre;
    }
    public String getDescription(){
        return this.description;
    }
    public float getNote(){
        return this.note;
    }
    public int getNbr_vote() {return this.nbr_vote;}





}
