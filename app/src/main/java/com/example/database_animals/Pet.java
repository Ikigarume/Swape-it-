package com.example.database_animals;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets")
public class Pet {


    @PrimaryKey
    @NonNull
    private String label ;

    @ColumnInfo(name = "animal_name")
    private String name ;

    @ColumnInfo(name = "animal_img")
    private int img ;

    public Pet(String label, String name, int img ){
        this.label= label;
        this.name= name;
        this.img=img;           }



    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }

    public void setName(String name){this.name = name ;}



}

