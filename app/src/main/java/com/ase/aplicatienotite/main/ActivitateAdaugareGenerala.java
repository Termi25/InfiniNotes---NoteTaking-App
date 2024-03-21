package com.ase.aplicatienotite.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.FactoryNotite;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.notite.TipuriNotite;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class ActivitateAdaugareGenerala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    private SectiuniViewModel sectiuneViewModel;
    private Spinner spinnerSectiuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_adauga);

        ImageButton btnAnulare=findViewById(R.id.btnAnulareAdaugare);
        btnAnulare.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        EditText etNumeSectiune = findViewById(R.id.etNumeAdaugaSectiune);

        final ImageButton btnSectiune = findViewById(R.id.btnAdaugaSectiune);
        btnSectiune.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(etNumeSectiune.getText())) {
                etNumeSectiune.setError("Nu s-a introdus o denumire pentru sectiune");
            } else {
                String numeSectiune = etNumeSectiune.getText().toString();
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    Sectiune sectiuneNoua=new Sectiune(numeSectiune,null);
                    try{
                        db.getSectiuneDao().insertSectiune(sectiuneNoua);
                        setResult(RESULT_OK);
                        finish();
                    }catch (Exception e){
                        runOnUiThread(()->etNumeSectiune.setError("Sectiunea deja exista, alegeti alta denumire"));
                    }
                });
            }
        });

        sectiuneViewModel=new ViewModelProvider(this).get(SectiuniViewModel.class);
        sectiuneViewModel.getToateSectiuni().observe(this,sectiuni->{
            List<String> listaSpinnerSectiuni =  new ArrayList<>();
            for(int i=0;i<sectiuni.size();i++){
                listaSpinnerSectiuni.add(sectiuni.get(i).getDenumireSectiune());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, listaSpinnerSectiuni);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSectiuni = (Spinner) findViewById(R.id.spSectiuneListaNoua);
            spinnerSectiuni.setAdapter(adapter);
        });

        EditText etNumeLista=findViewById(R.id.etNumeAdaugaLista);
        final ImageButton btnLista=findViewById(R.id.btnAdaugaLista);
        btnLista.setOnClickListener(view->{
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(etNumeLista.getText())) {
                etNumeLista.setError("Nu s-a introdus o denumire pentru lista");
            } else {
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    FactoryNotite factoryNotite=new FactoryNotite();
                    EditText etCorpListaNotite=findViewById(R.id.etContinutAdaugaLista);
                    Notita listaNotite=factoryNotite.creareNotite(TipuriNotite.NotitaLista,
                            etNumeLista.getText().toString(),
                            etCorpListaNotite.getText().toString(),
                            null);
                    try{
                        db.getNotitaListaDao().insertNotita((NotitaLista) listaNotite);
//                        listaNotite.setNotitaId(db.getNotitaListaDao().getNotitaDupaTitlu(String.valueOf(listaNotite.getTitlu())).getNotitaId());
//                        Sectiune sectiuneDeLegat=db.getSectiuneDao().
//                                getSectiuneCuDenumire(spinnerSectiuni.getSelectedItem().toString());
//
//                        SectiuneNotiteJoin legaturaNoua=new SectiuneNotiteJoin(listaNotite.getNotitaId(),
//                                sectiuneDeLegat.getSectiuneId());
//                        db.getSectiuneNotiteJoinDao().insert(legaturaNoua);
                        setResult(RESULT_OK);
                        finish();
                    }catch (Exception e){
                        runOnUiThread(()->etNumeLista.setError("Sectiunea deja exista, alegeti alta denumire"));
                    }
                });
            }
        });

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }else{
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        ImageButton btnAdaugaNotita=findViewById(R.id.btnAdaugaNotita);
        btnAdaugaNotita.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),ActivitateAdaugaNotita.class);
            launcher.launch(intent);
        });
    }
}
