package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.List;

@Dao
public interface NotitaListaDao {
    @Insert
    void insertNotitaLista(NotitaLista notita);

    @Update
    void updateNotitaLista(NotitaLista notita);

    @Delete
    void deleteNotitaLista(NotitaLista notita);

    @Query("SELECT * FROM liste WHERE titlu=:titlu")
    NotitaLista getNotitaListaDupaTitlu(String titlu);

    @Query("SELECT * FROM liste WHERE checked=1")
    List<NotitaLista> getNotiteListaChecked();

    @Query("DELETE FROM liste WHERE notitaId=:id")
    void deleteNotitaListaDupaId(int id);

    @Query("SELECT * FROM liste ORDER BY dataAccesare DESC LIMIT 1")
    NotitaLista getListaRecentaDupaDataAccesare();
}
