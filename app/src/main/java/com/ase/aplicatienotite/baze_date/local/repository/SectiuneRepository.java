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
    private LiveData<List<Sectiune>> sectiuni;
    private final LiveData<Map<Sectiune,List<Notita>>> sectiuniCuNotite;

    public SectiuneRepository(Application application) {
        NotiteDB db=NotiteDB.getInstance(application);
        this.sectiuneDao=db.getSectiuneDao();
        this.sectiuni =this.sectiuneDao.selectToateSectiuni();
        this.sectiuniCuNotite=db.getSectiuneNotiteJoinDao().getNotitePentruSectiuni();
    }

    public LiveData<List<Sectiune>>getToateSectiuni(int tipOrdonare){
        switch(tipOrdonare){
            case 0:{
                this.sectiuni=this.sectiuneDao.selectToateSectiuniAlfabeticA_Z();
                break;
            }
            case 1:{
                this.sectiuni=this.sectiuneDao.selectToateSectiuniAlfabeticZ_A();
                break;
            }
            case 2:{
                this.sectiuni=this.sectiuneDao.selectToateSectiuniNumarNotiteCRESC();
                break;
            }
            case 3:{
                this.sectiuni=this.sectiuneDao.selectToateSectiuniNumarNotiteDESC();
                break;
            }
            case 4:{
                this.sectiuni=this.sectiuneDao.selectToateSectiuni();
                break;
            }
            default:{
            }
        }
        return this.sectiuni;
    }
    public LiveData<Map<Sectiune,List<Notita>>>getToateSectiuniCuNotite(){return sectiuniCuNotite;}

    public void insert(Sectiune sectiune){
        NotiteDB.databaseWriteExecutor.execute(()->{
            sectiuneDao.insertSectiune(sectiune);
        });
    }
}
