package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

@Dao
public interface NotitaDao {
    @Insert
    void insertNotita(Notita notita);

    @Query("SELECT * FROM notite WHERE notitaId=:identificator")
    Notita getNotitaDupaId(int identificator);

    @Query("SELECT * FROM notite WHERE corp=:corp")
    Notita getNotitaDupaCorp(String corp);

    @Query("SELECT * FROM notite WHERE titlu=:titlu")
    Notita getNotitaDupaTitlu(String titlu);

    @Query("SELECT * FROM notite")
    List<Notita> getToateNotitele();
}
