package com.ase.aplicatienotite.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.LocaleList;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivitateEditeazaNotita extends AppCompatActivity {

    private SectiuniViewModel sectiuneViewModel;
    private Spinner spinnerSectiuni;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_editeaza_notita);
        Intent intent=getIntent();

        EditText etTitluNotita=findViewById(R.id.etNumeAdaugaNotita);
        EditText etCorpNotita =findViewById(R.id.etCorpTextNotita);
        if(intent.getExtras()!=null){
            Notita notita=(Notita)intent.getSerializableExtra("Notita");
            if(notita!=null){
                etTitluNotita.setText(notita.getTitlu());
                etCorpNotita.setText(notita.getCorp());

                ImageButton imgBtnBack=findViewById(R.id.btnAnulareEditareNotita);
                imgBtnBack.setOnClickListener(v->{

                    NotiteDB.databaseWriteExecutor.execute(()->{
                        NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                        try{
                            notita.setTitlu(String.valueOf(etTitluNotita.getText()));
                            notita.setCorp(String.valueOf(etCorpNotita.getText()));
                            db.getNotitaDao().updateNotita(notita);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                    setResult(RESULT_OK);
                    finish();
                });
            }
        }

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
                    ActivitateEditeazaNotita.this,
                    (view, year1, monthOfYear, dayOfMonth) -> btnReminderNotita.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year1),
                    year, month, day);
            datePickerDialog.show();
        });
    }
}
