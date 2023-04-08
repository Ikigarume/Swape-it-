package com.example.Users;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Users.User;

import java.util.List;

@Dao
public interface User_dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void adduser(User user);

    @Query("select * from users where login = :login")
    public List<User> getUser(String login);

    @Query("select * from users")
    public List<User> getUsers();

    @Delete
    public void deletuser(User user);

    @Update
    public void updatuser(User user);

}
