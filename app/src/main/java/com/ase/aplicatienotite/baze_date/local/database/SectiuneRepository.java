package com.ase.aplicatienotite.baze_date.local.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.dao.SectiuneDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;

public class SectiuneRepository {
    private SectiuneDao sectiuneDao;
    private LiveData<List<Sectiune>> sectiuni;

    public SectiuneRepository(Application application) {
        NotiteDB db=NotiteDB.getInstance(application);
        sectiuneDao=db.getSectiuneDao();
        sectiuni=sectiuneDao.selectToateSectiuni();
    }

    LiveData<List<Sectiune>>getToateSectiuni(){
        return sectiuni;
    }

    void insert(Sectiune sectiune){
        NotiteDB.databaseWriteExecutor.execute(()->{
            sectiuneDao.insertSectiune(sectiune);
        });
    }
}
