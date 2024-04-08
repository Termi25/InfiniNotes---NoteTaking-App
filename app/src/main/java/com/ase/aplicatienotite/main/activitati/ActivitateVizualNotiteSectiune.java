package com.ase.aplicatienotite.main.activitati;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterNotita;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteJoinViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;

public class ActivitateVizualNotiteSectiune extends AppCompatActivity {
    private SectiuneNotiteJoinViewModel sectiuneNotiteJoinViewModel;
    private ActivityResultLauncher<Intent>launcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_notite);

        Button btnVizualListe=findViewById(R.id.btnVizualListe);
        Bundle extras=getIntent().getExtras();
        int idSectiune;
        if(extras!=null){
            idSectiune=extras.getInt("codSectiune");
            RecyclerView rlv=findViewById(R.id.rlvNotite);
            final AdapterNotita adapter = new AdapterNotita(new AdapterNotita.NotiteDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            loadRecyclerView(adapter,idSectiune);

            launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
                Log.d("Test","Testare launcher ptr activitate liste");
            });
            btnVizualListe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),ActivitateVizualListeSectiune.class);
                    intent.putExtra("codSectiune",idSectiune);
                    launcher.launch(intent);
                }
            });
        }else{
            btnVizualListe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toasty.error(getApplicationContext(),"Nu exista liste asociate acestei secțiuni.").show();
                }
            });
        }

        FloatingActionButton fab=findViewById(R.id.fActBtnInchidereVizualNotite);
        fab.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    void loadRecyclerView(AdapterNotita adapter,int idSectiune){
        sectiuneNotiteJoinViewModel=new ViewModelProvider(this).get(SectiuneNotiteJoinViewModel.class);

        sectiuneNotiteJoinViewModel.getToateNotiteleSectiunii(idSectiune).observe(this, adapter::submitList);
    }
}
