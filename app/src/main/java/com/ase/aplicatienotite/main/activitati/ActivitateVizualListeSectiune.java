package com.ase.aplicatienotite.main.activitati;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterLista;
import com.ase.aplicatienotite.adaptoare.AdapterNotita;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteJoinViewModel;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteListaJoinViewModel;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivitateVizualListeSectiune extends AppCompatActivity {
    private SectiuneNotiteListaJoinViewModel sectiuneNotiteListaJoinViewModel;
    private ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_liste);

        incarcareVizualListe();

        setareFabInchidere();
    }

    private void incarcareVizualListe() {
        Bundle extras=getIntent().getExtras();
        int idSectiune;
        if(extras!=null){
            idSectiune=extras.getInt("codSectiune");
            RecyclerView rlv=findViewById(R.id.rlvListe);
            final AdapterLista adapter =new AdapterLista(new AdapterLista.NotitaListaDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            loadRecyclerView(adapter,idSectiune);
        }
    }

    private void loadRecyclerView(AdapterLista adapter, int idSectiune) {
        sectiuneNotiteListaJoinViewModel=new ViewModelProvider(this).get(SectiuneNotiteListaJoinViewModel.class);

        sectiuneNotiteListaJoinViewModel.getToateNotiteleListaSectiunii(idSectiune).observe(this, adapter::submitList);
    }

    private void setareFabInchidere() {
        FloatingActionButton fActBtnInchidereVizualListe=findViewById(R.id.fActBtnInchidereVizualListe);
        fActBtnInchidereVizualListe.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
}
