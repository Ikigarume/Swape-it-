package com.example.database_animals;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities={Pet.class}, version = 2, exportSchema = false)
public abstract class PetDatabase extends RoomDatabase {
    public abstract Pet_dao petdao() ;

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Ajoutez le code SQL nécessaire pour mettre à jour la base de données ici
        }
    };
}
