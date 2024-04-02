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
    void insertNotitaLista(NotitaLista notita);

    @Update
    void updateNotitaLista(NotitaLista notita);

    @Delete
    void deleteNotitaLista(NotitaLista notita);

    @Query("SELECT * FROM liste WHERE notitaId=:identificator")
    NotitaLista getNotitaListaDupaId(int identificator);

    @Query("SELECT * FROM liste WHERE corp=:corp")
    NotitaLista getNotitaListaDupaCorp(String corp);

    @Query("SELECT * FROM liste WHERE titlu=:titlu")
    NotitaLista getNotitaListaDupaTitlu(String titlu);

    @Query("SELECT * FROM liste")
    List<NotitaLista> getToateNotiteleLista();
}
