package com.example.aplicacionnotas.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aplicacionnotas.Modelos.Notas;

@Database(entities = Notas.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public static RoomDB database;
    private static String DATABASE_NAME = "NoteApp";

    public synchronized static RoomDB getInstance(Context context){
    if(database == null){
        database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        }
        return database;
    }

    public abstract MainDAO mainDAO();
}
