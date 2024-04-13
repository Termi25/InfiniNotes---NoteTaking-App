package com.ase.aplicatienotite.main.activitati;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterElementeLista;
import com.ase.aplicatienotite.adaptoare.AdapterLista;
import com.ase.aplicatienotite.baze_date.local.view.model.ListaNotiteJoinViewModel;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteListaJoinViewModel;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivitateVizualElementeListaNotite extends AppCompatActivity {
    private ListaNotiteJoinViewModel listaNotiteListaJoinViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_elemente_lista);

        incarcareVizualListe();

        setareFabInchidere();
    }

    private void incarcareVizualListe() {
        Bundle extras=getIntent().getExtras();
        NotitaLista notitaLista;
        if(extras!=null){
            notitaLista= (NotitaLista) extras.getSerializable("notitaLista");

            RecyclerView rlv=findViewById(R.id.rlvElementeLista);
            final AdapterElementeLista adapter =new AdapterElementeLista(new AdapterElementeLista.ElementListaDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            if(notitaLista!=null){
                loadRecyclerView(adapter,notitaLista.getNotitaId());

                TextView tvNumeLista=findViewById(R.id.tvNumeLista);
                if(!notitaLista.getTitlu().isEmpty()){
                    if(notitaLista.getTitlu().toLowerCase().contains("lista")){
                        tvNumeLista.setText(notitaLista.getTitlu());
                    }else{
                        tvNumeLista.setText(String.format("Lista: %s", notitaLista.getTitlu()));
                    }
                }
            }
        }
    }

    private void loadRecyclerView(AdapterElementeLista adapter, int idLista) {
        listaNotiteListaJoinViewModel=new ViewModelProvider(this).get(ListaNotiteJoinViewModel.class);

        listaNotiteListaJoinViewModel.getToateElementeleListei(idLista).observe(this, adapter::submitList);
    }

    private void setareFabInchidere() {
        FloatingActionButton fActBtnInchidereVizualListe=findViewById(R.id.fActBtnInchidereVizualElementeLista);
        fActBtnInchidereVizualListe.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
}
