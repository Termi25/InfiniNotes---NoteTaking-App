package com.ase.aplicatienotite.main.activitati;

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
import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivitateAdaugareGenerala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    private Spinner spinnerSectiuni;
    private Spinner spinnerCuloriSectiune;

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
            adaugareSectiune(etNumeSectiune,btnSectiune);
        });

        incarcareSpinnerLista();

        spinnerCuloriSectiune=findViewById(R.id.spCuloareSectiune);
        incarcareSpinnerCulori(spinnerCuloriSectiune);

        EditText etNumeLista=findViewById(R.id.etNumeAdaugaLista);
        final ImageButton btnLista=findViewById(R.id.btnAdaugaLista);
        btnLista.setOnClickListener(view->{
            adaugareLista(etNumeLista,btnLista);
        });

        pregatireLauncherAdaugaNotita();

        ImageButton btnAdaugaNotita=findViewById(R.id.btnAdaugaNotita);
        btnAdaugaNotita.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),ActivitateAdaugaNotita.class);
            launcher.launch(intent);
        });
    }

    private void incarcareSpinnerLista() {
        SectiuniViewModel sectiuneViewModel = new ViewModelProvider(this).get(SectiuniViewModel.class);
        sectiuneViewModel.getToateSectiuni(4).observe(this,sectiuni->{
            List<String> listaSpinnerSectiuni =  new ArrayList<>();
            for(int i=0;i<sectiuni.size();i++){
                listaSpinnerSectiuni.add(sectiuni.get(i).getDenumireSectiune());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, R.layout.view_spinner, listaSpinnerSectiuni);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSectiuni = (Spinner) findViewById(R.id.spSectiuneListaNoua);
            spinnerSectiuni.setAdapter(adapter);
        });
    }

    private void incarcareSpinnerCulori(Spinner spinnerCuloriSectiune) {
        spinnerCuloriSectiune.setAdapter(new ArrayAdapter<CuloriSectiune>
                (this, R.layout.view_spinner, CuloriSectiune.values()));
    }

    private void adaugareSectiune(EditText etNumeSectiune, ImageButton btnSectiune){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(etNumeSectiune.getText())) {
            etNumeSectiune.setError(getString(R.string.warning_no_name_sectiune));
        } else {
            String numeSectiune = etNumeSectiune.getText().toString();
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                Sectiune sectiuneNoua=new Sectiune(numeSectiune,CuloriSectiune.valueOf(spinnerCuloriSectiune.getSelectedItem().toString()));
                try{
                    db.getSectiuneDao().insertSectiune(sectiuneNoua);
                    setResult(RESULT_OK);
                    finish();
                }catch (Exception e){
                    runOnUiThread(()->etNumeSectiune.setError(getString(R.string.error_adaugare_generala_sectiune_existenta)));
                }
            });
        }
    }

    private void adaugareLista(EditText etNumeLista, ImageButton btnLista){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(etNumeLista.getText())) {
            etNumeLista.setError(getString(R.string.warning_no_name_listanotite));
        } else {
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                EditText etCorpListaNotite=findViewById(R.id.etContinutAdaugaLista);
                NotitaLista listaNotite=new NotitaLista(etNumeLista.getText().toString(),
                        etCorpListaNotite.getText().toString());

                adaugareListaGoala(db, etNumeLista, listaNotite);

                try{
                    List<String> listaLiniiCorpLista=new ArrayList<>();
                    listaLiniiCorpLista= Arrays.asList(listaNotite.getCorp().split("\n"));
                    String titluElementLista=listaNotite.getTitlu();

                    for(int i=0;i<listaLiniiCorpLista.size();i++){
                        ElementLista elementNotita=new ElementLista(titluElementLista+String.valueOf(i+1),listaLiniiCorpLista.get(i));

                        db.getElementListaDao().insertElementLista(elementNotita);

                        elementNotita.setNotitaId(db.getElementListaDao()
                                .getElementListaDupaTitlu(titluElementLista+String.valueOf(i+1)).getNotitaId());

                        ListaNotiteJoin legaturaNoua=new ListaNotiteJoin(listaNotite.getNotitaId(),
                                elementNotita.getNotitaId());
                        db.getListaNotiteJoinDao().insert(legaturaNoua);
                    }

                    setResult(RESULT_OK);
                    finish();
                }catch (Exception e){

                    runOnUiThread(()->etCorpListaNotite.setError(getString(R.string.error_adaugare_generala_lista_notite_adaugare_notite_esuata)));
                }
            });
        }
    }

    private void adaugareListaGoala(NotiteDB db, EditText etNumeLista, NotitaLista listaNotite){
        try{
            db.getNotitaListaDao().insertNotitaLista((NotitaLista) listaNotite);
            listaNotite.setNotitaId(db.getNotitaListaDao()
                    .getNotitaListaDupaTitlu(String.valueOf(etNumeLista.getText())).getNotitaId());
            Sectiune sectiuneDeLegat=db.getSectiuneDao().
                    getSectiuneCuDenumire(spinnerSectiuni.getSelectedItem().toString());

            SectiuneNotiteListaJoin legaturaNoua=new SectiuneNotiteListaJoin(listaNotite.getNotitaId(),
                    sectiuneDeLegat.getSectiuneId());
            db.getSectiuneNotiteListaJoinDao().insert(legaturaNoua);
        }catch (Exception e){
            runOnUiThread(()->etNumeLista.setError(getString(R.string.error_adaugare_generala_lista_notite_existenta)));
        }
    }

    private void pregatireLauncherAdaugaNotita(){
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }else{
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
