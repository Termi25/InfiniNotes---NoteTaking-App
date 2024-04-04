package com.ase.aplicatienotite.main.activitati;

import static android.view.Gravity.apply;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ActivitatePrincipala extends AppCompatActivity {
    private static ActivityResultLauncher<Intent> launcher;
    private SectiuniViewModel sectiuneViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);

        initializareToasty();

        RecyclerView rlv=findViewById(R.id.rlv_main);
        final AdapterSectiune adapter = new AdapterSectiune(new AdapterSectiune.SectiuneDiff());
        rlv.setAdapter(adapter);
        rlv.setLayoutManager(new LinearLayoutManager(this));

        incarcareRecyclerView(adapter);

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Toasty.success(getApplicationContext(),getString(R.string.modificari_succes),Toast.LENGTH_LONG).show();
                try{
                    rlv.getAdapter().notifyDataSetChanged();
                }catch (Exception e){
                    Log.e("Error","Eroare notifyDataSetChanged pentru RecyclerView in Activitate principala");
                }
            }else{
                Toasty.error(getApplicationContext(), getString(R.string.modificari_esec),Toasty.LENGTH_LONG).show();
            }
        });

        ImageButton btnSetari=findViewById(R.id.imgBtnSetari);
        btnSetari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivitateSetari.class);
                launcher.launch(intent);
            }
        });

        ImageButton btnAdauga=findViewById(R.id.imgBtnAdaugareGenerala);
        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivitateAdaugareGenerala.class);
                launcher.launch(intent);
            }
        });

    }

    void initializareToasty(){
        Toasty.Config.getInstance()
                .setToastTypeface(Objects.requireNonNull(ResourcesCompat.getFont(this, R.font.alata)))
                .apply();
    }

    void incarcareRecyclerView(AdapterSectiune adapter){
        sectiuneViewModel=new ViewModelProvider(this).get(SectiuniViewModel.class);

        sectiuneViewModel.getToateSectiuni().observe(this,sectiuni->{
            if(sectiuni.isEmpty()){
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    Sectiune misc=new Sectiune(getString(R.string.misc),null);
                    db.getSectiuneDao().insertSectiune(misc);
                });
            }
            adapter.submitList(sectiuni);
        });
    }

}