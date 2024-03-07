package com.ase.aplicatienotite.baze_date.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;

@Dao
public interface NotiteDao {
    @Insert
    void insertSectiune(Sectiune sectiune);
    @Delete
    void deleteSectiune(Sectiune sectiune);

    @Query("SELECT * FROM sectiuni")
    List<Sectiune> selectToateSectiuni();
}
