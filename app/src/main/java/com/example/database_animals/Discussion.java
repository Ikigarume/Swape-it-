package com.example.database_animals;

public class Discussion {
    private String otherChatter ;
    private String latestMsg ;

    public Discussion(String user , String message){
        this.otherChatter = user ;
        this.latestMsg = message ;
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
}
