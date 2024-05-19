package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

@Dao
public interface NotitaDao {
    @Insert
    void insertNotita(Notita notita);

    @Update
    void updateNotita(Notita notita);

    @Delete
    void deleteNotita(Notita notita);

    @Query("SELECT * FROM notite WHERE titlu=:titlu")
    Notita getNotitaDupaTitlu(String titlu);

    @Query("SELECT * FROM notite WHERE checked=1")
    List<Notita> getNotiteChecked();

    @Query("SELECT * FROM notite ORDER BY dataReminder")
    List<Notita> getToateNotitele();
}
