package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;

@Dao
public interface SectiuneDao {
    @Insert
    void insertSectiune(Sectiune sectiune);
    @Update
    void updateSectiune(Sectiune sectiune);
    @Query("SELECT * FROM sectiuni")
    LiveData<List<Sectiune>> selectToateSectiuni();
    @Query("SELECT * FROM sectiuni ORDER BY LOWER(denumireSectiune)")
    LiveData<List<Sectiune>> selectToateSectiuniAlfabeticA_Z();
    @Query("SELECT * FROM sectiuni ORDER BY LOWER(denumireSectiune) DESC")
    LiveData<List<Sectiune>> selectToateSectiuniAlfabeticZ_A();
    @Query("SELECT * FROM sectiuni ORDER BY (SELECT COUNT(sectiuneId) FROM sectiune_notita_join WHERE sectiune_notita_join.sectiuneId=sectiuni.sectiuneId)")
    LiveData<List<Sectiune>> selectToateSectiuniNumarNotiteCRESC();
    @Query("SELECT * FROM sectiuni ORDER BY (SELECT COUNT(sectiuneId) FROM sectiune_notita_join WHERE sectiune_notita_join.sectiuneId=sectiuni.sectiuneId) DESC")
    LiveData<List<Sectiune>> selectToateSectiuniNumarNotiteDESC();
    @Query("SELECT * FROM sectiuni WHERE denumireSectiune=:denumire")
    Sectiune getSectiuneCuDenumire(String denumire);
    @Query("SELECT * FROM sectiuni WHERE sectiuneId=:idSectiune")
    Sectiune getSectiuneCuId(int idSectiune);
    @Query("DELETE FROM sectiuni  WHERE sectiuneId=:id")
    void deleteSectiuneDupaId(int id);
}
