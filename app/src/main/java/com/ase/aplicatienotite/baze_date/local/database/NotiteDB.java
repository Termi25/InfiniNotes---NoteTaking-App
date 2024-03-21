package com.ase.aplicatienotite.baze_date.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ase.aplicatienotite.baze_date.local.convertori.Convertori;
import com.ase.aplicatienotite.baze_date.local.dao.NotitaDao;
import com.ase.aplicatienotite.baze_date.local.dao.NotitaListaDao;
import com.ase.aplicatienotite.baze_date.local.dao.SectiuneDao;
import com.ase.aplicatienotite.baze_date.local.dao.SectiuneNotiteJoinDao;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaElementLista;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.notite.NotitaReminder;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Notita.class, NotitaElementLista.class,
        NotitaLista.class, NotitaReminder.class, Sectiune.class,SectiuneNotiteJoin.class},version=5,
        exportSchema = false)
@TypeConverters({Convertori.class})
public abstract class NotiteDB extends RoomDatabase {
    public static final String notiteDB="notite.db";
    private static NotiteDB instanta;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public synchronized static NotiteDB getInstance(Context context){
        if(instanta==null){
            instanta= Room.databaseBuilder(context,
                    NotiteDB.class,notiteDB)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instanta;
    }

    public abstract NotitaDao getNotitaDao();
    public abstract NotitaListaDao getNotitaListaDao();
    public abstract SectiuneDao getSectiuneDao();
    public abstract SectiuneNotiteJoinDao getSectiuneNotiteJoinDao();
}
