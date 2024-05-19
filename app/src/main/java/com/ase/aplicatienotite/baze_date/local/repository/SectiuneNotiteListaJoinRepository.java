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
    private LiveData<List<NotitaLista>> notiteLista;
    private final int idSectiune;

    public SectiuneNotiteListaJoinRepository(Application application, int idSectiune) {
        NotiteDB db=NotiteDB.getInstance(application);
        sectiuneNotiteListaJoinDao=db.getSectiuneNotiteListaJoinDao();
        notiteLista=sectiuneNotiteListaJoinDao.getNotiteListaPentruSectiuneLive(idSectiune);
        this.idSectiune=idSectiune;
    }

    public LiveData<List<NotitaLista>>getToateNotiteleListaSectiunii(int tipOrdonare){
        switch(tipOrdonare){
            case 0:{
                this.notiteLista=this.sectiuneNotiteListaJoinDao.getNotiteListaPentruSectiuneLive_Alfabetic_A_Z(this.idSectiune);
                break;
            }
            case 1:{
                this.notiteLista=this.sectiuneNotiteListaJoinDao.getNotiteListaPentruSectiuneLive_Alfabetic_Z_A(this.idSectiune);
                break;
            }
            case 2:{
                this.notiteLista=this.sectiuneNotiteListaJoinDao.getNotiteListaPentruSectiuneLive(this.idSectiune);
                break;
            }
        }
        return this.notiteLista;
    }

    public void insert(int idNotita,int idSectiune){
        SectiuneNotiteListaJoin legatura=new SectiuneNotiteListaJoin(idNotita,idSectiune);
        NotiteDB.databaseWriteExecutor.execute(()->{
            sectiuneNotiteListaJoinDao.insert(legatura);
        });
    }
}
