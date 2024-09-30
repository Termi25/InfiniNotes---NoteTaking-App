package com.ase.aplicatienotite.baze_date.local.repository;

import android.app.Application;

import com.ase.aplicatienotite.baze_date.local.dao.NotitaDao;
import com.ase.aplicatienotite.baze_date.local.dao.NotitaListaDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

public class NotiteListeRecenteRepository {
    private final NotitaDao notitaDao;
    private final NotitaListaDao notitaListaDao;
    private Notita notita;
    private NotitaLista lista;

    public NotiteListeRecenteRepository(Application application) {
        NotiteDB db=NotiteDB.getInstance(application);
        this.notitaDao = db.getNotitaDao();
        this.notitaListaDao=db.getNotitaListaDao();
        this.notita=this.notitaDao.getNotitaRecentaDupaDataAccesare();
        this.lista=this.notitaListaDao.getListaRecentaDupaDataAccesare();
    }

    public Notita getNotitaAccesataRecent(){
        return this.notita;
    }

    public NotitaLista getListaAccesataRecent(){
        return this.lista;
    }
}
