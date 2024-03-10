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
        setContentView(R.layout.activity_main);

        RecyclerView rlv=findViewById(R.id.rlv_main);
        final AdapterSectiune adapter = new AdapterSectiune(new AdapterSectiune.SectiuneDiff());
        rlv.setAdapter(adapter);
        rlv.setLayoutManager(new LinearLayoutManager(this));

        sectiuneViewModel=new ViewModelProvider(this).get(SectiuniViewModel.class);

        sectiuneViewModel.getToateSectiuni().observe(this,sectiuni->{
            boolean existaMisc=false;
            for(int i=0;i<sectiuni.size();i++){
                if(sectiuni.get(i).getDenumireSectiune().equals("MISC")){
                    existaMisc=true;
                }
            }
            if(!existaMisc){
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    Sectiune misc=new Sectiune(getString(R.string.misc),null);
                    db.getSectiuneDao().insertSectiune(misc);
                });
            }
            adapter.submitList(sectiuni);
        });

        ImageButton btnAdauga=findViewById(R.id.btnAdaugareGenerala);
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Intent data=result.getData();
                if(data!=null){
                    Toast.makeText(getApplicationContext(), getString(R.string.modificari_succes),Toast.LENGTH_LONG).show();
                }
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
}