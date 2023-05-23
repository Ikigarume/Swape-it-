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
    private int favorite ;
    private int id_user ;
    private double longitude ;
    private double latitude ;

    public Annonce(int id_annonce, String login, String photo_de_profil, String chemin_image, String titre, String description,float note, int nbr_vote, String id_categories,String number, int etat, int favorite){
        this.id_user = 0 ;
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
        this.favorite = favorite;
    }

    public Annonce(int id_annonce, int id_user, String login, String photo_de_profil, String chemin_image, String titre, String description,float note, int nbr_vote, String id_categories,String number, int etat, int favorite){
        this.id_user = id_user ;
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
        this.favorite = favorite;
    }



    // constructor that uses the latitude, longitude
    public Annonce(int id_annonce, int id_user, String login, String photo_de_profil, String chemin_image, String titre, String description,float note, int nbr_vote, String id_categories,String number, int etat, int favorite, double longitude, double latitude){
        this.id_user = id_user ;
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
        this.favorite = favorite;
        this.latitude = latitude ;
        this.longitude = longitude ;
    }

    public int getId_user(){return this.id_user;}
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
    public int getFavorite(){return this.favorite;}
    public void setFavorite(int fav){this.favorite = fav;}
    public double getLatitude() {return latitude;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public double getLongitude() {return longitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}


}
