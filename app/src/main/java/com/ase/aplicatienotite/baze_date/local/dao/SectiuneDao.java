package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
    @Delete
    void deleteSectiune(Sectiune sectiune);

    @Query("SELECT * FROM sectiuni")
    LiveData<List<Sectiune>> selectToateSectiuni();
    @Query("SELECT * FROM sectiuni")
    List<Sectiune> selectToateSectiuniNoLive();
    @Query("SELECT * FROM sectiuni WHERE denumireSectiune=:denumire")
    Sectiune getSectiuneCuDenumire(String denumire);
    @Query("SELECT * FROM sectiuni WHERE sectiuneId=:idSectiune")
    Sectiune getSectiuneCuId(int idSectiune);
}
