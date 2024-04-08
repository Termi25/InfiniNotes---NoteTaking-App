package com.ase.aplicatienotite.main.activitati;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ActivitateEditeazaNotita extends AppCompatActivity {

    private SectiuniViewModel sectiuneViewModel;
    private Spinner spinnerSectiuni;
    private Date dataReminder=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_editeaza_notita);
        Intent intent=getIntent();

        EditText etTitluNotita=findViewById(R.id.etNumeAdaugaNotita);
        EditText etCorpNotita =findViewById(R.id.etCorpTextNotita);

        Button btnReminderNotita = findViewById(R.id.btnReminderAdaugaNotita);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(getString(R.string.pattern_data),
                Locale.ENGLISH);
        seteazaBtnReminder(btnReminderNotita,simpleDateFormat);

        if(intent.getExtras()!=null){
            Notita notita=(Notita)intent.getSerializableExtra("Notita");
            if(notita!=null){
                etTitluNotita.setText(notita.getTitlu());
                etCorpNotita.setText(notita.getCorp());
                if(notita.getDataReminder()!=null){
                    btnReminderNotita.setText(simpleDateFormat.format(notita.getDataReminder()));
                    dataReminder=notita.getDataReminder();
                }

                ImageButton imgBtnBack=findViewById(R.id.btnAnulareEditareNotita);
                imgBtnBack.setOnClickListener(v->{

                    NotiteDB.databaseWriteExecutor.execute(()->{
                        NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                        try{
                            notita.setTitlu(String.valueOf(etTitluNotita.getText()));
                            notita.setCorp(String.valueOf(etCorpNotita.getText()));
                            if(dataReminder!=null){
                                notita.setDataReminder(dataReminder);
                                Log.d("TEST",simpleDateFormat.format(dataReminder));
                            }
                            db.getNotitaDao().updateNotita(notita);
                        }catch (Exception e){
                            Log.e("Error",getString(R.string.error_editeaza_notita_update_notita1));
                        }
                    });
                    Toasty.success(getApplicationContext(),R.string.modificari_succes, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
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
                    NotiteDB.databaseWriteExecutor.execute(()->{
                        NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                        try{
                            spinnerSectiuni.setSelection(db.getSectiuneNotiteJoinDao().getIdSectiune(notita.getNotitaId())-1);
                        }catch (Exception e){
                            Log.e("Error",getString(R.string.error_editeaza_notita_update_notita2));
                        }
                    });

                    spinnerSectiuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            NotiteDB.databaseWriteExecutor.execute(()->{
                                NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                                try{
                                    int idSectiuneVeche=db.getSectiuneNotiteJoinDao().getIdSectiune(notita.getNotitaId());
                                    SectiuneNotiteJoin legaturaVeche=new SectiuneNotiteJoin(notita.getNotitaId(),
                                            idSectiuneVeche);
                                    db.getSectiuneNotiteJoinDao().deleteLegatura(legaturaVeche);

                                    Sectiune sectiuneDeLegat=db.getSectiuneDao().
                                            getSectiuneCuDenumire(spinnerSectiuni.getSelectedItem().toString());
                                    SectiuneNotiteJoin legaturaNoua=new SectiuneNotiteJoin(notita.getNotitaId(),
                                            sectiuneDeLegat.getSectiuneId());
                                    db.getSectiuneNotiteJoinDao().insert(legaturaNoua);
                                }catch (Exception e){
                                    Log.e("Error",getString(R.string.error_editeaza_notita_update_notita3));
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                });
            }
        }

    }

    private void seteazaBtnReminder(Button btnReminderNotita,SimpleDateFormat simpleDateFormat) {
        LocaleList locale = getResources().getConfiguration().getLocales();
        Locale.setDefault(locale.get(0));
        btnReminderNotita.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            if(dataReminder!=null){
                calendar.setTime(dataReminder);
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ActivitateEditeazaNotita.this,
                    (view, year1, monthOfYear, dayOfMonth) ->{
                        StringBuilder builderData=new StringBuilder();
                        if(dayOfMonth<10){
                            builderData.append("0");
                        }
                        builderData.append(dayOfMonth).append(" / ");

                        if(monthOfYear+1<10){
                            builderData.append("0");
                        }
                        builderData.append(monthOfYear+1).append(" / ");
                        builderData.append(year);
                        btnReminderNotita.setText(builderData.toString());

                        String dataString= dayOfMonth + " / " + (monthOfYear + 1) + " / " + year1;
                        try{
                            dataReminder=simpleDateFormat.parse(dataString);
                        }catch (ParseException e){
                            Log.e("Error",getString(R.string.error_editeaza_notita_update_notita4));
                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }
}
