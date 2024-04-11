package com.ase.aplicatienotite.baze_date.local.dao.legaturi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.List;
import java.util.Map;

@Dao
public interface ListaNotiteJoinDao {
    @Insert
    void insert(ListaNotiteJoin legatura);
    @Query("SELECT * FROM elementeliste INNER JOIN notita_lista_join ON elementeliste.notitaId=notita_lista_join.notitaId WHERE notita_lista_join.listaId=:listaId")
    List<ElementLista> getNotitePentruLista(final int listaId);
    @Query("SELECT * FROM elementeliste INNER JOIN notita_lista_join ON elementeliste.notitaId=notita_lista_join.notitaId WHERE notita_lista_join.listaId=:listaId")
    LiveData<List<ElementLista>> getNotitePentruListaLive(final int listaId);
    @Query("SELECT * FROM notita_lista_join WHERE notita_lista_join.notitaId=:idNotita")
    List<ListaNotiteJoin> getLegaturiCuNotita(int idNotita);
    @Query("SELECT listaId FROM notita_lista_join WHERE notitaId=:idNotita")
    int getIdLista(int idNotita);
    @Delete
    void deleteLegatura(ListaNotiteJoin legatura);
}
