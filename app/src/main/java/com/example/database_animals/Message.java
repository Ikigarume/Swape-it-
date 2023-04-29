package com.example.database_animals;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity(tableName = "Message")
public class Message {

    @ColumnInfo(name = "message")
    String message;
    @ColumnInfo(name = "id_sender")
    int idSender;
    @ColumnInfo(name = "id_receiver")
    int idReceiver;
    @ColumnInfo(name = "messages_date ")
    String dateMessage; //createdAt

    public Message ( int idsender , int idreciever ,String message, String dateMessage){
        this.message = message ;
        this.idReceiver = idreciever ;
        this.idSender = idsender ;
        this.dateMessage = dateMessage ;
    }

    public String getMessage() {
        return this.message;
    }

    public int getIdReceiver() {
        return this.idReceiver;
    }

    public int getIdSender() {
        return this.idSender;
    }

    public String getDateMessage(){ return this.dateMessage ; }


    public void setMessage(String message){this.message = message ;}
    public void setIdReceiver(int id){this.idReceiver = id ;}
    public void setIdSender(int id){this.idSender = id ;}
    public void setDateMessage(String date){this.dateMessage = date ;}

}


