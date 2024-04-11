package com.ase.aplicatienotite.baze_date.local.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ase.aplicatienotite.baze_date.local.convertori.Convertori;
import com.ase.aplicatienotite.baze_date.local.dao.ElementListaDao;
import com.ase.aplicatienotite.baze_date.local.dao.ListaNotiteJoinDao;
import com.ase.aplicatienotite.baze_date.local.dao.NotitaDao;
import com.ase.aplicatienotite.baze_date.local.dao.NotitaListaDao;
import com.ase.aplicatienotite.baze_date.local.dao.SectiuneDao;
import com.ase.aplicatienotite.baze_date.local.dao.SectiuneNotiteJoinDao;
import com.ase.aplicatienotite.baze_date.local.dao.SectiuneNotiteListaJoinDao;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;

@Database(entities = {Notita.class, NotitaLista.class,
        Sectiune.class,SectiuneNotiteJoin.class,
        SectiuneNotiteListaJoin.class,ListaNotiteJoin.class,
        ElementLista.class},version=11,
        exportSchema = false)
@TypeConverters({Convertori.class})
public abstract class NotiteDB extends RoomDatabase {
    public static final String notiteDB="notite.db";
    private static NotiteDB instanta;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
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
    public abstract SectiuneNotiteListaJoinDao getSectiuneNotiteListaJoinDao();
    public abstract ListaNotiteJoinDao getListaNotiteJoinDao();
    public abstract ElementListaDao getElementListaDao();

    public void backupDB(Context context){
        if(instanta==null){
            Log.e("TEST","Instanta baza de date lipsa pentru copiere");
            return;
        }

         File fisierDB=context.getDatabasePath(notiteDB);
         File fisierDBWal = new File(fisierDB.getPath() + "-wal");
         File fisierDBShm =new File(fisierDB.getPath() + "-shm");
         File fisierDBBackup = new File(fisierDB.getPath() + "-bkp");
         File fisierDBWalBkp = new File(fisierDBBackup.getPath() + "-wal");
         File fisierDBShmBkp = new File(fisierDBBackup.getPath() + "-shm");
         if (fisierDBBackup.exists()){
             fisierDBBackup.delete();
         }
         if (fisierDBWalBkp.exists()){
             fisierDBWalBkp.delete();
         }
         if (fisierDBShmBkp.exists()){
             fisierDBShmBkp.delete();
         }
        try {
            Files.copy(fisierDB.toPath(), fisierDBBackup.toPath());
            if (fisierDBWal.exists()){
                Files.copy(fisierDBWal.toPath(), fisierDBWalBkp.toPath());
            }
            if (fisierDBShm.exists()){
                Files.copy(fisierDBShm.toPath(), fisierDBShmBkp.toPath());
            }
            checkpoint();
        } catch (IOException e) {
            Log.e("TEST", "Eroare la salvarea de backup de fisiere de baza de date. Mesaj eroare: "+e.toString());
        }
    }

    public void restaurareDB(Context context ,
                             Boolean restart){
        if(!Files.exists(
                Paths.get(
                        context.getDatabasePath(notiteDB).getPath()
                                + "-bkp"))){
            Log.e("TEST","Nu exista fisier backup pentru efectuarea restaurarii.");
        }
        if(instanta==null){
            Log.e("TEST","Instanta baza de date lipsa pentru copiere");
            return;
        }

        File fisierDB= new File(Objects.requireNonNull(instanta.getOpenHelper().getReadableDatabase().getPath()));
        File fisierDBWal = new File(fisierDB.getPath() + "-wal");
        File fisierDBShm =new File(fisierDB.getPath() + "-shm");
        File fisierDBBackup = new File(fisierDB.getPath() + "-bkp");
        File fisierDBWalBkp = new File(fisierDBBackup.getPath() + "-wal");
        File fisierDBShmBkp = new File(fisierDBBackup.getPath() + "-shm");
        try {
            Files.copy(fisierDB.toPath(), fisierDBBackup.toPath());
            if (fisierDBWal.exists()){
                Files.copy(fisierDBWal.toPath(), fisierDBWalBkp.toPath());
            }
            if (fisierDBShm.exists()){
                Files.copy(fisierDBShm.toPath(), fisierDBShmBkp.toPath());
            }
            checkpoint();
        } catch (IOException e) {
            Log.e("TEST", "Eroare la salvarea de backup de fisiere de baza de date. Mesaj eroare: "+e.toString());
        }

        if(restart){
            Intent intent=context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());
            if(intent!=null){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                System.exit(0);
            }
        }
    }

    private void checkpoint() {
        SupportSQLiteDatabase db = this.getOpenHelper().getWritableDatabase();
        db.query("PRAGMA wal_checkpoint(FULL);");
    }
}
