package com.ase.aplicatienotite.baze_date.local.dao.legaturi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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
    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId ORDER BY LOWER(notite.titlu)")
    LiveData<List<Notita>> getNotitePentruSectiuneLive_Alfabetic_A_Z(final int sectiuneId);
    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId ORDER BY LOWER(notite.titlu) DESC")
    LiveData<List<Notita>> getNotitePentruSectiuneLive_Alfabetic_Z_A(final int sectiuneId);
    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId ORDER BY dataReminder")
    LiveData<List<Notita>> getNotitePentruSectiuneLive_Reminder_CRESC(final int sectiuneId);
    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId ORDER BY dataReminder DESC")
    LiveData<List<Notita>> getNotitePentruSectiuneLive_Reminder_DESC(final int sectiuneId);
    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId GROUP BY sectiune_notita_join.sectiuneId")
    LiveData<Map<Sectiune,List<Notita>>> getNotitePentruSectiuni();
    @Query("SELECT * FROM sectiune_notita_join WHERE sectiune_notita_join.notitaId=:idNotita")
    List<SectiuneNotiteJoin> getLegaturiCuNotita(int idNotita);
    @Query("SELECT * FROM sectiune_notita_join WHERE sectiune_notita_join.sectiuneId=:idSectiune")
    List<SectiuneNotiteJoin> getLegaturiCuSectiune(int idSectiune);
    @Query("SELECT sectiuneId FROM sectiune_notita_join WHERE notitaId=:idNotita")
    int getIdSectiune(int idNotita);
    @Query("SELECT * FROM sectiuni INNER JOIN sectiune_notita_join WHERE sectiuni.sectiuneId=sectiune_notita_join.sectiuneId AND sectiune_notita_join.notitaId=:idNotita")
    Sectiune getSectiune(int idNotita);
    @Delete
    void deleteLegatura(SectiuneNotiteJoin legatura);
}
