package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

@Dao
public interface ElementListaDao {
    @Insert
    void insertElementLista(ElementLista notita);

    @Update
    void updateElementLista(ElementLista notita);

    @Delete
    void deleteElementLista(ElementLista notita);

    @Query("SELECT * FROM elemente_liste WHERE notitaId=:identificator")
    ElementLista getElementListaDupaId(int identificator);

    @Query("SELECT * FROM elemente_liste WHERE titlu=:titlu")
    ElementLista getElementListaDupaTitlu(String titlu);

    @Query("SELECT * FROM elemente_liste")
    List<ElementLista> getToateElementeleListelor();
}
