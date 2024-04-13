package com.ase.aplicatienotite.baze_date.local.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.dao.legaturi.ListaNotiteJoinDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;

import java.util.List;

public class ListaNotiteJoinRepository {
    private final ListaNotiteJoinDao listaNotiteJoinDao;
    private final LiveData<List<ElementLista>> elementeLista;

    public ListaNotiteJoinRepository(Application application, int idLista) {
        NotiteDB db=NotiteDB.getInstance(application);
        this.listaNotiteJoinDao=db.getListaNotiteJoinDao();
        this.elementeLista=this.listaNotiteJoinDao.getNotitePentruListaLive(idLista);
    }

    public LiveData<List<ElementLista>> getToateElementeleListei(){return this.elementeLista;}

    public void insert(int idLista,int idElement){
        ListaNotiteJoin legatura=new ListaNotiteJoin(idLista,idElement);
        NotiteDB.databaseWriteExecutor.execute(()->{
            this.listaNotiteJoinDao.insert(legatura);
        });
    }
}
