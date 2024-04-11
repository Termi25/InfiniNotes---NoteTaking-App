package com.ase.aplicatienotite.main.activitati;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.main.fragmente.FragmentSetari;

import java.io.File;
import java.util.ArrayList;
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

        imgBtnShareDB.setOnClickListener(v->{
                exportDatabase();
        });

        Button btnBackupFisiereDB=findViewById(R.id.btnBackupFisiereDB);
        btnBackupFisiereDB.setOnClickListener(v->{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                try{
                    db.backupDB(getApplicationContext());
                    runOnUiThread(()-> Toasty.success(getApplicationContext(), R.string.succes_activity_setari_backup_db));
                }catch (Exception e){
                    runOnUiThread(()-> Toasty.error(getApplicationContext(), R.string.error_activity_setari_backup_db));
                }
            });
        });

        Button btnRestaurareFisiereDB=findViewById(R.id.btnRestaurareFisiereDB);
        btnRestaurareFisiereDB.setOnClickListener(v->{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                try{
                    db.restaurareDB(getApplicationContext(),true);
                    runOnUiThread(()-> Toasty.success(getApplicationContext(), R.string.succes_activity_setari_restaurare_db));
                }catch (Exception e){
                    runOnUiThread(()-> Toasty.error(getApplicationContext(), R.string.error_activity_setari_restaurare_db));
                }

            });
        });

        ImageButton imgBtnSalavareSetari=findViewById(R.id.imgBtnSalvareSetari);
        imgBtnSalavareSetari.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
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
}
