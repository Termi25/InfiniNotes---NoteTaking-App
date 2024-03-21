package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.List;

@Dao
public interface NotitaListaDao {
    @Insert
    void insertNotita(NotitaLista notita);

    @Update
    void updateNotita(NotitaLista notita);

    @Delete
    void deleteNotita(NotitaLista notita);

    @Query("SELECT * FROM liste WHERE notitaId=:identificator")
    Notita getNotitaDupaId(int identificator);

    @Query("SELECT * FROM liste WHERE corp=:corp")
    Notita getNotitaDupaCorp(String corp);

    @Query("SELECT * FROM liste WHERE titlu=:titlu")
    Notita getNotitaDupaTitlu(String titlu);

    @Query("SELECT * FROM liste")
    List<Notita> getToateNotitele();
}
