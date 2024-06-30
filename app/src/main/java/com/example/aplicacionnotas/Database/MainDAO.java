package com.example.aplicacionnotas.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.aplicacionnotas.Modelos.Notas;

import java.util.List;

@Dao

public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Notas notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notas> getAll();

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id, String title, String notes);

    @Delete
    void delete(Notas notes);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
