package com.ase.aplicatienotite.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class ActivitatePrincipala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    private SectiuniViewModel sectiuneViewModel;
    private List<Sectiune>listaSectiuni=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);

        RecyclerView rlv=findViewById(R.id.rlv_main);
        final AdapterSectiune adapter = new AdapterSectiune(new AdapterSectiune.SectiuneDiff());
        rlv.setAdapter(adapter);
        rlv.setLayoutManager(new LinearLayoutManager(this));

        incarcareRecyclerView(adapter);

        ImageButton btnAdauga=findViewById(R.id.btnAdaugareGenerala);
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Toast.makeText(getApplicationContext(), getString(R.string.modificari_succes),Toast.LENGTH_LONG).show();
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    List<Sectiune>sectiuni = db.getSectiuneDao().selectToateSectiuniNoLive();
                    for(int i = 0; i < sectiuni.size(); i++){
                        sectiuni.get(i).setNotite(db.getSectiuneNotiteJoinDao().
                                getNotitePentruSectiune(sectiuni.get(i).getSectiuneId()));
                    }
                    adapter.submitList(sectiuni);
                });
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.modificari_esec) ,Toast.LENGTH_LONG).show();
            }
        });

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivitateAdaugareGenerala.class);
                launcher.launch(intent);
            }
        });

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
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                for(int i=0;i<sectiuni.size();i++){
                    sectiuni.get(i).setNotite(db.getSectiuneNotiteJoinDao().
                            getNotitePentruSectiune(sectiuni.get(i).getSectiuneId()));
                }
            });
            adapter.submitList(sectiuni);
        });
    }
}