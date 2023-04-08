package com.example.Users;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.Users.User;
import com.example.Users.User_dao;

@Database(entities={User.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract User_dao mydao() ;
}
