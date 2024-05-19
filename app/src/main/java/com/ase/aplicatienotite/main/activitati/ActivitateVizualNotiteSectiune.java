package com.ase.aplicatienotite.main.activitati;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterNotita;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteJoinViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ActivitateVizualNotiteSectiune extends AppCompatActivity {
    private SectiuneNotiteJoinViewModel sectiuneNotiteJoinViewModel;
    private ActivityResultLauncher<Intent>launcher;
    private Observer<List<Notita>> observerListaNotite;
    private RecyclerView rlv;
    private Spinner spOrdineNotite;
    LiveData<List<Notita>> dateNotite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_notite);

        spOrdineNotite=findViewById(R.id.spOrdineNotite);

        incarcareUI();

        setareFabInchidere();
    }

    private void incarcareUI() {
        Button btnVizualListe=findViewById(R.id.btnVizualListe);
        Bundle extras=getIntent().getExtras();
        int idSectiune;
        if(extras!=null){
            idSectiune=extras.getInt("codSectiune");
            this.rlv=findViewById(R.id.rlvNotite);
            final AdapterNotita adapter = new AdapterNotita(new AdapterNotita.NotiteDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            incarcareSpinnerOrdineSectiuni(idSectiune);

            incarcareRecyclerView(idSectiune);

            launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
                Log.d("Test","Testare launcher ptr activitate liste");
            });
            btnVizualListe.setOnClickListener(v -> {
                Intent intent=new Intent(getApplicationContext(),ActivitateVizualListeSectiune.class);
                intent.putExtra("codSectiune",idSectiune);
                launcher.launch(intent);
            });
        }else{
            btnVizualListe.setOnClickListener(v -> Toasty.error(getApplicationContext(),"Nu exista liste asociate acestei secțiuni.").show());
        }
    }

    void incarcareSpinnerOrdineSectiuni(int idSectiune){
        this.spOrdineNotite.setAdapter(new ArrayAdapter<>
                (getApplicationContext(), R.layout.view_spinner, new String[]{"Alfabetic A-Z",
                        "Alfabetic Z-A","După dată creare","După reminder crescător","După reminder descrescător"}));

        SharedPreferences sharedPrefs = getSharedPreferences("preferences.xml", MODE_PRIVATE);
        if(!sharedPrefs.contains("ordineNotite"+idSectiune)){
            SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
            editor.putInt("ordineNotite"+idSectiune,0);
            editor.apply();
        }
        int[] ordineNotite = {sharedPrefs.getInt("ordineNotite"+idSectiune, 0)};
        this.spOrdineNotite.setSelection(ordineNotite[0]);
        this.spOrdineNotite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incarcareRecyclerView(idSectiune);
                SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
                editor.putInt("ordineNotite"+idSectiune,position);
                editor.apply();
                ordineNotite[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void incarcareRecyclerView(int idSectiune){
        sectiuneNotiteJoinViewModel=new ViewModelProvider(this).get(SectiuneNotiteJoinViewModel.class);
        int optiuneOrdonare=0;
        switch (this.spOrdineNotite.getSelectedItemPosition()){
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
            case 3:{
                optiuneOrdonare=3;
                break;
            }
            case 4:{
                optiuneOrdonare=4;
                break;
            }
        }
        if(observerListaNotite !=null){
            dateNotite.removeObserver(observerListaNotite);
        }
        dateNotite = sectiuneNotiteJoinViewModel.getToateNotiteleSectiunii(idSectiune,optiuneOrdonare);
        observerListaNotite =new Observer<List<Notita>>() {
            @Override
            public void onChanged(List<Notita> notite) {
                AdapterNotita adapter = (AdapterNotita) rlv.getAdapter();
                assert adapter != null;
                adapter.submitList(notite);
            }
        };
        dateNotite.observe(this, observerListaNotite);

        try{
            int numar= rlv.getAdapter().getItemCount();
            if(numar==0){
                TextView tvFaraNotite=findViewById(R.id.tvNuExistaNotite);
                tvFaraNotite.setText(R.string.nu_exista_notite_adaugate);
            }else{
                TextView tvFaraNotite=findViewById(R.id.tvNuExistaNotite);
                tvFaraNotite.setText("");
            }

        }catch (Exception e){
            TextView tvFaraNotite=findViewById(R.id.tvNuExistaNotite);
            tvFaraNotite.setText(R.string.nu_exista_notite_adaugate);
        }
    }

    private void setareFabInchidere() {
        FloatingActionButton fab=findViewById(R.id.fActBtnInchidereVizualNotite);
        fab.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}
