package com.ase.aplicatienotite.baze_date.local.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.dao.SectiuneDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;

public class SectiuneRepository {
    private final SectiuneDao sectiuneDao;
    private final LiveData<List<Sectiune>> sectiuni;

    public SectiuneRepository(Application application) {
        NotiteDB db=NotiteDB.getInstance(application);
        sectiuneDao=db.getSectiuneDao();
        sectiuni=sectiuneDao.selectToateSectiuni();
    }

    public LiveData<List<Sectiune>>getToateSectiuni(){
        return sectiuni;
    }

    public void insert(Sectiune sectiune){
        NotiteDB.databaseWriteExecutor.execute(()->{
            sectiuneDao.insertSectiune(sectiune);
        });
    }
}
