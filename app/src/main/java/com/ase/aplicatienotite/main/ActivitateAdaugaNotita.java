package com.ase.aplicatienotite.main;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivitateAdaugaNotita extends AppCompatActivity {
    private List<Sectiune> listaSectiuni=new ArrayList<>();
    private SectiuniViewModel sectiuneViewModel;
    private Spinner spinnerSectiuni;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_notita);

        ImageButton btnAnulare=findViewById(R.id.btnAnulareNotita);
        btnAnulare.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
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
            spinnerSectiuni = (Spinner) findViewById(R.id.spSectiuneAdaugaNotita);
            spinnerSectiuni.setAdapter(adapter);
        });

        Button btnReminderNotita = findViewById(R.id.btnReminderAdaugaNotita);
        LocaleList locale = getResources().getConfiguration().getLocales();
        Locale.setDefault(locale.get(0));
        btnReminderNotita.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ActivitateAdaugaNotita.this,
                    (view, year1, monthOfYear, dayOfMonth) -> btnReminderNotita.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        ImageButton btnAdaugareNotita=findViewById(R.id.btnAdaugaNotitaFinal);
        btnAdaugareNotita.setOnClickListener(v->{
                EditText etTitluNotita=findViewById(R.id.etNumeAdaugaNotita);
                if(TextUtils.isEmpty(etTitluNotita.getText())){
                    etTitluNotita.setError("Titlu este necesar pentru salvare.");
                }else{
                    if(btnReminderNotita.getText().equals("Data reminder")){
                                NotiteDB.databaseWriteExecutor.execute(()->{
                                    EditText etCorpNotita=findViewById(R.id.etCorpTextNotita);
                                    Notita notitaNoua=new Notita(String.valueOf(etTitluNotita.getText()),
                                            String.valueOf(etCorpNotita.getText()));

                                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                                    try{
                                        db.getNotitaDao().insertNotita(notitaNoua);

                                        notitaNoua.setNotitaId(db.getNotitaDao().getNotitaDupaTitlu(String.valueOf(etTitluNotita.getText())).getNotitaId());
                                        System.out.println(notitaNoua.getNotitaId());
                                        Sectiune sectiuneDeLegat=db.getSectiuneDao().
                                                getSectiuneCuDenumire(spinnerSectiuni.getSelectedItem().toString());

                                        SectiuneNotiteJoin legaturaNoua=new SectiuneNotiteJoin(notitaNoua.getNotitaId(),
                                                sectiuneDeLegat.getSectiuneId());
                                        db.getSectiuneNotiteJoinDao().insert(legaturaNoua);
                                        setResult(RESULT_OK);
                                        finish();
                                    }catch (Exception e){
                                        runOnUiThread(() -> etTitluNotita.setError("Titlu este deja utilizat, incercati alta denumire."));
                                    }
                                });
                    }
                }
        });
    }
}
