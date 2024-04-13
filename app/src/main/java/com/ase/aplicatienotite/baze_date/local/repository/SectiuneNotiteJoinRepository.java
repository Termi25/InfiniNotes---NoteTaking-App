package com.ase.aplicatienotite.baze_date.local.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.dao.legaturi.SectiuneNotiteJoinDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

public class SectiuneNotiteJoinRepository {
    private final SectiuneNotiteJoinDao sectiuneNotiteJoinDao;
    private final LiveData<List<Notita>> notite;

    public SectiuneNotiteJoinRepository(Application application,int idSectiune) {
        NotiteDB db=NotiteDB.getInstance(application);
        this.sectiuneNotiteJoinDao=db.getSectiuneNotiteJoinDao();
        this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive(idSectiune);
    }

    public LiveData<List<Notita>>getToateNotiteleSectiunii(){
        return this.notite;
    }

    public void insert(int idNotita,int idSectiune){
        SectiuneNotiteJoin legatura=new SectiuneNotiteJoin(idNotita,idSectiune);
        NotiteDB.databaseWriteExecutor.execute(()->{
            this.sectiuneNotiteJoinDao.insert(legatura);
        });
    }
}
