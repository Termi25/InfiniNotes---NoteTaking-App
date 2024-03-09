package com.ase.aplicatienotite.baze_date.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;

@Dao
public interface SectiuneNotiteJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SectiuneNotiteJoin legatura);

    @Query("SELECT * FROM notite INNER JOIN sectiune_notita_join ON notite.notitaId=sectiune_notita_join.notitaId WHERE sectiune_notita_join.sectiuneId=:sectiuneId")
    List<Notita> getNotitePentruSectiune(final int sectiuneId);


    @Delete
    void deleteLegatura(SectiuneNotiteJoin legatura);
}
