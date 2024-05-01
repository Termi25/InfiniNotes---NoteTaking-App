package com.ase.aplicatienotite.main.activitati;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.main.fragmente.FragmentSetari;
import com.ase.aplicatienotite.main.receiver.RegularDeleteReceiverNotite;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ActivitateSetari extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_setari);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, new FragmentSetari())
                .commit();

        ImageButton imgBtnShareDB=findViewById(R.id.imgBtnShareDB);

        imgBtnShareDB.setOnClickListener(v-> exportDatabase());

        Button btnBackupFisiereDB=findViewById(R.id.btnBackupFisiereDB);
        btnBackupFisiereDB.setOnClickListener(v-> backupDatabase());

        Button btnRestaurareFisiereDB=findViewById(R.id.btnRestaurareFisiereDB);
        btnRestaurareFisiereDB.setOnClickListener(v-> restoreDatabase());

        preferencesSetup();

        ImageButton imgBtnSalavareSetari=findViewById(R.id.imgBtnSalvareSetari);
        imgBtnSalavareSetari.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TEST","called preferencesSetup");
        preferencesSetup();
    }

    private void exportDatabase(){
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB instantaDb=NotiteDB.getInstance(getApplicationContext());

            File fisierDBPartajat=new File(Objects.requireNonNull(
                    instantaDb.getOpenHelper().getReadableDatabase().getPath()));

            File fisierDBWal = new File(fisierDBPartajat.getPath() + "-wal");

            File fisierDBShm =new File(fisierDBPartajat.getPath() + "-shm");

            Uri fisierDBUri = FileProvider.getUriForFile(getApplicationContext(),
                    getString(R.string.com_ase_aplicatienotite_fileprovider), fisierDBPartajat);

            Uri fisierDBWalUri=FileProvider.getUriForFile(getApplicationContext(),
                    getString(R.string.com_ase_aplicatienotite_fileprovider), fisierDBWal);

            Uri fisierDBShmUri=FileProvider.getUriForFile(getApplicationContext(),
                    getString(R.string.com_ase_aplicatienotite_fileprovider), fisierDBShm);

            Intent share = new Intent();
            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            share.setAction(Intent.ACTION_SEND_MULTIPLE);
            share.setType(getString(R.string.application_db_share_type));
            ArrayList<Uri> fisiereDB=new ArrayList<>();
            fisiereDB.add(fisierDBUri);
            fisiereDB.add(fisierDBWalUri);
            fisiereDB.add(fisierDBShmUri);
            share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fisiereDB);
            startActivity(Intent.createChooser(share, getString(R.string.message_export_db_infininotes)));
        });
    }

    private void backupDatabase() {
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB db=NotiteDB.getInstance(getApplicationContext());
            try{
                db.backupDB(getApplicationContext());
                runOnUiThread(()-> Toasty.success(getApplicationContext(), R.string.succes_activity_setari_backup_db));
            }catch (Exception e){
                runOnUiThread(()-> Toasty.error(getApplicationContext(), R.string.error_activity_setari_backup_db));
            }
        });
    }

    private void restoreDatabase() {
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB db=NotiteDB.getInstance(getApplicationContext());
            try{
                db.restaurareDB(getApplicationContext(),true);
                runOnUiThread(()-> Toasty.success(getApplicationContext(), R.string.succes_activity_setari_restaurare_db));
            }catch (Exception e){
                runOnUiThread(()-> Toasty.error(getApplicationContext(), R.string.error_activity_setari_restaurare_db));
            }

        });
    }

    private void preferencesSetup() {
        Map<String,?> preferences = PreferenceManager.getDefaultSharedPreferences(this).getAll();

        if((boolean)preferences.get(getResources().getString(R.string.pref_periodic_delete_key))){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 0);
                }
            }

            Intent intentToFire = new Intent(getApplicationContext(), RegularDeleteReceiverNotite.class);
            intentToFire.setAction(RegularDeleteReceiverNotite.ACTION_ALARM);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    1, intentToFire, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                    getSystemService(Context.ALARM_SERVICE);

            Calendar calendar=Calendar.getInstance();
            long periodicitate=60000;
            try{
                switch (Integer.parseInt((String) preferences.get(getResources().getString(R.string.pref_periodic_delete_duration)))){
                    case 1:{
                        periodicitate=86400000;
                        break;
                    }
                    case 7:{
                        periodicitate=86400000*7;
                        break;
                    }
                    case 30:{
                        periodicitate= 86400000L * 30;
                        break;
                    }
                }
            }catch (Exception e){
                Log.e("ERROR","Preference for periodic delete duration not set.");
            }
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),periodicitate,alarmIntent);
        }else{
            try{
                Intent intentToFire = new Intent(getApplicationContext(), RegularDeleteReceiverNotite.class);
                PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),
                        1, intentToFire, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.cancel(sender);
            }catch (Exception e){
                Log.e("ERROR","Error in disabling alarm for periodic delete");
            }
        }
    }
}
