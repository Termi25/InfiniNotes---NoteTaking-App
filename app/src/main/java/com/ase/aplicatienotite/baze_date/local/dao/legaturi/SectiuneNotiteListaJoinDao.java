package com.ase.aplicatienotite.baze_date.local.dao.legaturi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;
import java.util.Map;

@Dao
public interface SectiuneNotiteListaJoinDao {
    @Insert
    void insert(SectiuneNotiteListaJoin legatura);
    @Query("SELECT * FROM liste INNER JOIN sectiune_notita_lista_join ON liste.notitaId=sectiune_notita_lista_join.notitaId WHERE sectiune_notita_lista_join.sectiuneId=:sectiuneId")
    List<NotitaLista> getNotiteListaPentruSectiune(final int sectiuneId);
    @Query("SELECT * FROM liste INNER JOIN sectiune_notita_lista_join ON liste.notitaId=sectiune_notita_lista_join.notitaId WHERE sectiune_notita_lista_join.sectiuneId=:sectiuneId")
    LiveData<List<NotitaLista>> getNotiteListaPentruSectiuneLive(final int sectiuneId);
    @Query("SELECT * FROM liste INNER JOIN sectiune_notita_lista_join ON liste.notitaId=sectiune_notita_lista_join.notitaId GROUP BY sectiune_notita_lista_join.sectiuneId")
    LiveData<Map<Sectiune,List<Notita>>> getNotiteListaPentruSectiuni();
    @Query("SELECT * FROM sectiune_notita_lista_join WHERE sectiune_notita_lista_join.notitaId=:idNotita")
    List<SectiuneNotiteListaJoin> getLegaturiCuNotitalista(int idNotita);
    @Query("SELECT sectiuneId FROM sectiune_notita_lista_join WHERE notitaId=:idNotita")
    int getIdSectiune(int idNotita);
    @Delete
    void deleteLegatura(SectiuneNotiteListaJoin legatura);
}
