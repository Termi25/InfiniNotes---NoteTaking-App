package com.ase.aplicatienotite.baze_date.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaElementLista;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.notite.NotitaReminder;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

@Database(entities = {Notita.class, NotitaElementLista.class,
        NotitaLista.class, NotitaReminder.class, Sectiune.class},version=1,
        exportSchema = false)
@TypeConverters({Convertori.class})
public abstract class NotiteDB extends RoomDatabase {
    public static final String notiteDB="notite.db";
    private static NotiteDB instanta;

    public synchronized static NotiteDB getInstance(Context context){
        if(instanta==null){
            instanta= Room.databaseBuilder(context,
                    NotiteDB.class,notiteDB)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instanta;
    }

    public abstract NotiteDao getNotiteDao();
}
