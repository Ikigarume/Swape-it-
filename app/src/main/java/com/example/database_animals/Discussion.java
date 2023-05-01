package com.example.database_animals;

public class Discussion {

    private int otherChatterId ;
    private String otherChatter ;
    private String latestMsg ;
    private String img_rep ;



    public Discussion(int userId , String user , String message, String img_rep){
        this.otherChatterId = userId ;
        this.otherChatter = user ;
        this.latestMsg = message ;
        this.img_rep = img_rep ;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public String getOtherChatter() {
        return otherChatter;
    }

    public void setOtherChatter(String otherChatter) {
        this.otherChatter = otherChatter;
    }
    public String getImg_rep() {
        return img_rep;
    }

    public void setImg_rep(String img_rep) {
        this.img_rep = img_rep;
    }

    public int getOtherChatterId() {
        return otherChatterId;
    }

    public void setOtherChatterId(int otherChatterId) {
        this.otherChatterId = otherChatterId;
    }
}
