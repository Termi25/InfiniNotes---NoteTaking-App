package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;
import java.util.Map;

@Dao
public interface SectiuneNotiteJoinDao {
    @Insert
    void insert(SectiuneNotiteJoin legatura);

    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId")
    List<Notita> getNotitePentruSectiune(final int sectiuneId);

    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId")
    LiveData<List<Notita>> getNotitePentruSectiuneLive(final int sectiuneId);

    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId GROUP BY sectiune_notita_join.sectiuneId")
    LiveData<Map<Sectiune,List<Notita>>> getNotitePentruSectiuni();

    @Delete
    void deleteLegatura(SectiuneNotiteJoin legatura);
}
