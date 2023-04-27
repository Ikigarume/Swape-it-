package com.example.database_animals;

public class Annonce {


    private int id_annonce;
    private String login;
    private String photo_de_profil ;
    private String chemin_image ;
    private String titre ;
    private String description ;
    private float note ;
    private int nbr_vote ;
    private String id_categories;
    private String number ;
    private int etat ;

    public Annonce(int id_annonce, String login, String photo_de_profil, String chemin_image, String titre, String description,float note, int nbr_vote, String id_categories,String number, int etat){
        this.id_annonce = id_annonce;
        this.description = description;
        this.photo_de_profil = photo_de_profil;
        this.chemin_image = chemin_image;
        this.login = login;
        this.note = note;
        this.titre = titre ;
        this.nbr_vote = nbr_vote;
        this.id_categories = id_categories;
        this.number = number ;
        this.etat = etat ;
    }

    public int getId_annonce(){ return this.id_annonce ; }
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
    public String getId_categories(){return this.id_categories;}
    public String getNumber() {return this.number;}
    public int getEtat(){return this.etat;}




}
