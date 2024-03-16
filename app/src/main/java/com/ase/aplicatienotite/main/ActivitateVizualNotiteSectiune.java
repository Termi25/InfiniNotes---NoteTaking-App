package com.ase.aplicatienotite.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterNotita;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneNotiteJoinViewHolder;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteJoinViewModel;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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

            incarcareRecyclerView(adapter,idSectiune);
        }

        FloatingActionButton fab=findViewById(R.id.fActBtnInchidereVizual);
        fab.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    void incarcareRecyclerView(AdapterNotita adapter,int idSectiune){
        sectiuneNotiteJoinViewModel=new ViewModelProvider(this).get(SectiuneNotiteJoinViewModel.class);

        sectiuneNotiteJoinViewModel.getToateNotiteleSectiunii(idSectiune).observe(this,notite->{
            adapter.submitList(notite);
        });
    }
}
