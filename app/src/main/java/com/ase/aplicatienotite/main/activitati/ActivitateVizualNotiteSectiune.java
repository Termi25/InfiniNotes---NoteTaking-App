package com.ase.aplicatienotite.main.activitati;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterNotita;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteJoinViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivitateVizualNotiteSectiune extends AppCompatActivity {
    private SectiuneNotiteJoinViewModel sectiuneNotiteJoinViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_notite);

        Bundle extras=getIntent().getExtras();
        int idSectiune;
        if(extras!=null){
            idSectiune=extras.getInt("codSectiune");
            RecyclerView rlv=findViewById(R.id.rlvNotite);
            final AdapterNotita adapter = new AdapterNotita(new AdapterNotita.NotiteDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            loadRecyclerView(adapter,idSectiune);
        }

        FloatingActionButton fab=findViewById(R.id.fActBtnInchidereVizual);
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
