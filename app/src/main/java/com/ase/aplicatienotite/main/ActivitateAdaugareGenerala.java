package com.ase.aplicatienotite.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
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
                etNumeSectiune.setError(getString(R.string.warning_no_name_sectiune));
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
                        runOnUiThread(()->etNumeSectiune.setError(getString(R.string.error_adaugare_generala_sectiune_existenta)));
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
                etNumeLista.setError(getString(R.string.warning_no_name_listanotite));
            } else {
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    EditText etCorpListaNotite=findViewById(R.id.etContinutAdaugaLista);
                    NotitaLista listaNotite=new NotitaLista(etNumeLista.getText().toString(),
                            etCorpListaNotite.getText().toString());
                    try{
                        db.getNotitaListaDao().insertNotita((NotitaLista) listaNotite);
                        setResult(RESULT_OK);
                        finish();
                    }catch (Exception e){
                        runOnUiThread(()->etNumeLista.setError(getString(R.string.error_adaugare_generala_lista_notite_existenta)));
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
