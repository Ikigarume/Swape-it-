package com.example.database_animals;

public class Favorites {
    private String userName;
    private String title ;

    public Favorites(String un , String t){
        this.userName = un ;
        this.title = t ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
