package com.ase.aplicatienotite.main.activitati;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterLista;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteListaJoinViewModel;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ActivitateVizualListeSectiune extends AppCompatActivity {
    private SectiuneNotiteListaJoinViewModel sectiuneNotiteListaJoinViewModel;
    private RecyclerView rlv;
    private Spinner spOrdineListe;
    LiveData<List<NotitaLista>> dateListe;
    private Observer<List<NotitaLista>> observerLista;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_liste);

        spOrdineListe=findViewById(R.id.spOrdineListe);

        incarcareVizualListe();

        setareFabInchidere();
    }

    private void incarcareVizualListe() {
        Bundle extras=getIntent().getExtras();
        int idSectiune;
        if(extras!=null){
            idSectiune=extras.getInt("codSectiune");
            rlv=findViewById(R.id.rlvListe);
            final AdapterLista adapter =new AdapterLista(new AdapterLista.NotitaListaDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            incarcareSpinnerOrdineSectiuni(idSectiune);
            incarcareRecyclerView(idSectiune);
        }
    }

    void incarcareSpinnerOrdineSectiuni(int idSectiune){
        this.spOrdineListe.setAdapter(new ArrayAdapter<>
                (getApplicationContext(), R.layout.view_spinner, new String[]{"Alfabetic A-Z",
                        "Alfabetic Z-A","După dată creare"}));

        SharedPreferences sharedPrefs = getSharedPreferences("preferences.xml", MODE_PRIVATE);
        if(!sharedPrefs.contains("ordineListe"+idSectiune)){
            SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
            editor.putInt("ordineListe"+idSectiune,0);
            editor.apply();
        }
        int[] ordineListe = {sharedPrefs.getInt("ordineListe"+idSectiune, 0)};
        this.spOrdineListe.setSelection(ordineListe[0]);
        this.spOrdineListe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incarcareRecyclerView(idSectiune);
                SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
                editor.putInt("ordineListe"+idSectiune,position);
                editor.apply();
                ordineListe[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void incarcareRecyclerView(int idSectiune){
        sectiuneNotiteListaJoinViewModel=new ViewModelProvider(this).get(SectiuneNotiteListaJoinViewModel.class);
        int optiuneOrdonare=0;
        switch (this.spOrdineListe.getSelectedItemPosition()){
            case 0:{
                optiuneOrdonare=0;
                break;
            }
            case 1:{
                optiuneOrdonare=1;
                break;
            }
            case 2:{
                optiuneOrdonare=2;
                break;
            }
        }
        if(observerLista !=null){
            dateListe.removeObserver(observerLista);
        }
        dateListe = sectiuneNotiteListaJoinViewModel.getToateNotiteleListaSectiunii(idSectiune,optiuneOrdonare);
        observerLista =new Observer<List<NotitaLista>>() {
            @Override
            public void onChanged(List<NotitaLista> notite) {
                AdapterLista adapter = (AdapterLista) rlv.getAdapter();
                assert adapter != null;
                adapter.submitList(notite);
                if(!notite.isEmpty()){
                    TextView tvFaraNotite=findViewById(R.id.tvNuExistaNotite);
                    tvFaraNotite.setText("");
                }
            }
        };
        dateListe.observe(this, observerLista);

        try{
            int numar= rlv.getAdapter().getItemCount();
            if(numar==0){
                TextView tvFaraNotite=findViewById(R.id.tvNuExistaListe);
                tvFaraNotite.setText(R.string.nu_exista_liste_adaugate);
            }else{
                TextView tvFaraNotite=findViewById(R.id.tvNuExistaListe);
                tvFaraNotite.setText("");
            }

        }catch (Exception e){
            TextView tvFaraNotite=findViewById(R.id.tvNuExistaListe);
            tvFaraNotite.setText(R.string.nu_exista_liste_adaugate);
        }
    }

    private void setareFabInchidere() {
        FloatingActionButton fActBtnInchidereVizualListe=findViewById(R.id.fActBtnInchidereVizualListe);
        fActBtnInchidereVizualListe.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
}
