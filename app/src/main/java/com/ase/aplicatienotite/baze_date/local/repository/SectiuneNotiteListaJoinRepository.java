package com.ase.aplicatienotite.baze_date.local.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.dao.legaturi.SectiuneNotiteListaJoinDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.List;

public class SectiuneNotiteListaJoinRepository {
    private final SectiuneNotiteListaJoinDao sectiuneNotiteListaJoinDao;
    private final LiveData<List<NotitaLista>> notiteLista;

    public SectiuneNotiteListaJoinRepository(Application application, int idSectiune) {
        NotiteDB db=NotiteDB.getInstance(application);
        sectiuneNotiteListaJoinDao=db.getSectiuneNotiteListaJoinDao();
        notiteLista=sectiuneNotiteListaJoinDao.getNotiteListaPentruSectiuneLive(idSectiune);
    }

    public LiveData<List<NotitaLista>>getToateNotiteleListaSectiunii(){
        return notiteLista;
    }

    public void insert(int idNotita,int idSectiune){
        SectiuneNotiteListaJoin legatura=new SectiuneNotiteListaJoin(idNotita,idSectiune);
        NotiteDB.databaseWriteExecutor.execute(()->{
            sectiuneNotiteListaJoinDao.insert(legatura);
        });
    }
}
