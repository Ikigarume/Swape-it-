package com.example.database_animals;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Pet_dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addpet(Pet pet);


    @Query("select * from pets")
    public List<Pet> getPets();

    @Delete
    public void deletPet(Pet pet);

    @Query("update pets set animal_name=:desc where label=:label")
    public void updatPet(String label, String desc);

}
