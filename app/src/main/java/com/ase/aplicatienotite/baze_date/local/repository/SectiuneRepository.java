package com.ase.aplicatienotite.baze_date.local.repository;

import static com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneViewHolder.context;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ase.aplicatienotite.baze_date.local.dao.SectiuneDao;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;
import java.util.Map;

public class SectiuneRepository {
    private final SectiuneDao sectiuneDao;
    private final LiveData<List<Sectiune>> sectiuni;
    private final LiveData<Map<Sectiune,List<Notita>>> sectiuniCuNotite;

    public SectiuneRepository(Application application) {
        LiveData<List<Sectiune>> sectiuni1;
        NotiteDB db=NotiteDB.getInstance(application);
        sectiuneDao=db.getSectiuneDao();
        sectiuni =sectiuneDao.selectToateSectiuni();
        sectiuniCuNotite=db.getSectiuneNotiteJoinDao().getNotitePentruSectiuni();
    }

    public LiveData<List<Sectiune>>getToateSectiuni(){
        return sectiuni;
    }
    public LiveData<Map<Sectiune,List<Notita>>>getToateSectiuniCuNotite(){return sectiuniCuNotite;}

    public void insert(Sectiune sectiune){
        NotiteDB.databaseWriteExecutor.execute(()->{
            sectiuneDao.insertSectiune(sectiune);
        });
    }
}
