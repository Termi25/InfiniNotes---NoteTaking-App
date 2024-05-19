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
    private LiveData<List<Notita>> notite;
    private final int idSectiune;

    public SectiuneNotiteJoinRepository(Application application,int idSectiune) {
        NotiteDB db=NotiteDB.getInstance(application);
        this.sectiuneNotiteJoinDao=db.getSectiuneNotiteJoinDao();
        this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive(idSectiune);
        this.idSectiune=idSectiune;
    }

    public LiveData<List<Notita>>getToateNotiteleSectiunii(int tipOrdonare){
        switch(tipOrdonare){
            case 0:{
                this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive_Alfabetic_A_Z(this.idSectiune);
                break;
            }
            case 1:{
                this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive_Alfabetic_Z_A(this.idSectiune);
                break;
            }
            case 2:{
                this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive(this.idSectiune);
                break;
            }
            case 3:{
                this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive_Reminder_CRESC(this.idSectiune);
                break;
            }
            case 4:{
                this.notite=this.sectiuneNotiteJoinDao.getNotitePentruSectiuneLive_Reminder_DESC(this.idSectiune);
                break;
            }
        }
        return this.notite;
    }

    public void insert(int idNotita,int idSectiune){
        SectiuneNotiteJoin legatura=new SectiuneNotiteJoin(idNotita,idSectiune);
        NotiteDB.databaseWriteExecutor.execute(()->{
            this.sectiuneNotiteJoinDao.insert(legatura);
        });
    }
}
