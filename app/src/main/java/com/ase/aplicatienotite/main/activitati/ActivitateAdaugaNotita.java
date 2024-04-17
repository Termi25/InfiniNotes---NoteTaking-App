package com.ase.aplicatienotite.main.activitati;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.main.receiver.AlarmBroadcastReceiverReminderNotita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitateAdaugaNotita extends AppCompatActivity {
    private SectiuniViewModel sectiuneViewModel;
    private Spinner spinnerSectiuni;
    private Date dataReminder = null;
    private Calendar calendarDeTransmis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_adauga_notita);

        setareButonAnulare();

        setareSpinnerSectiuni();

        //TODO - creare buton cu dialog asociat pentru setarea orei reminderului (necesita adaugare si in editeaza notita)Edit

        setareBtnReminder();

        setareButonAdaugareNotita();
    }

    private void setareButonAdaugareNotita() {
        ImageButton btnAdaugareNotita = findViewById(R.id.btnAdaugaNotitaFinal);
        btnAdaugareNotita.setOnClickListener(v -> {
            EditText etTitluNotita = findViewById(R.id.etNumeAdaugaNotita);
            if (TextUtils.isEmpty(etTitluNotita.getText())) {
                etTitluNotita.setError(getString(R.string.titlu_este_necesar_pentru_salvare));
            } else {
                NotiteDB.databaseWriteExecutor.execute(() -> {
                    EditText etCorpNotita = findViewById(R.id.etCorpTextNotita);
                    Notita notitaNoua = new Notita(String.valueOf(etTitluNotita.getText()),
                            String.valueOf(etCorpNotita.getText()));

                    if (dataReminder != null) {
                        notitaNoua.setDataReminder(dataReminder);
                    }

                    NotiteDB db = NotiteDB.getInstance(getApplicationContext());
                    try {
                        db.getNotitaDao().insertNotita(notitaNoua);

                        notitaNoua.setNotitaId(db.getNotitaDao()
                                .getNotitaDupaTitlu(String
                                        .valueOf(etTitluNotita.getText())).getNotitaId());

                        Sectiune sectiuneDeLegat = db.getSectiuneDao().
                                getSectiuneCuDenumire(spinnerSectiuni
                                        .getSelectedItem().toString());

                        SectiuneNotiteJoin legaturaNoua = new SectiuneNotiteJoin(notitaNoua
                                .getNotitaId(), sectiuneDeLegat.getSectiuneId());
                        db.getSectiuneNotiteJoinDao().insert(legaturaNoua);

                        if(notitaNoua.getDataReminder()!=null){
                            setareAlarma(notitaNoua,sectiuneDeLegat,this.calendarDeTransmis);
                        }

                        setResult(RESULT_OK);
                        finish();
                    } catch (Exception e) {
                        runOnUiThread(() -> etTitluNotita
                                .setError("Titlu este deja utilizat, incercati alta denumire."));
                    }
                });
            }
        });
    }

    private void setareButonAnulare() {
        ImageButton btnAnulare = findViewById(R.id.btnAnulareEditareNotita);
        btnAnulare.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void setareSpinnerSectiuni() {
        sectiuneViewModel = new ViewModelProvider(this).get(SectiuniViewModel.class);

        sectiuneViewModel.getToateSectiuni(4).observe(this, sectiuni -> {
            List<String> listaSpinnerSectiuni = new ArrayList<>();
            for (int i = 0; i < sectiuni.size(); i++) {
                listaSpinnerSectiuni.add(sectiuni.get(i).getDenumireSectiune());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, listaSpinnerSectiuni);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSectiuni = (Spinner) findViewById(R.id.spSectiuneAdaugaNotita);
            spinnerSectiuni.setAdapter(adapter);
        });
    }

    private void setareBtnReminder() {
        Button btnReminderNotita = findViewById(R.id.btnReminderAdaugaNotita);
        LocaleList locale = getResources().getConfiguration().getLocales();
        Locale.setDefault(locale.get(0));
        this.calendarDeTransmis=Calendar.getInstance();
        btnReminderNotita.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ActivitateAdaugaNotita.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        btnReminderNotita.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year1);

                        String dataString = String.valueOf(dayOfMonth) +
                                '-' +
                                (monthOfYear + 1) +
                                '-' +
                                year1;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy",
                                Locale.ENGLISH);

                        try {
                            dataReminder = simpleDateFormat.parse(dataString);
                            if(dataReminder!=null){
                                this.calendarDeTransmis.setTime(dataReminder);
                                this.calendarDeTransmis.add(Calendar.HOUR_OF_DAY,8);
                                this.calendarDeTransmis.add(Calendar.MINUTE,0);
                            }
                        } catch (ParseException e) {
                            Log.e("Error",getString(R.string.error_view_adauga_notita_invalid_date));
                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void setareAlarma(Notita notita, Sectiune sectiuneDeLegat, Calendar calendar) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 0);
            }
        }

        Intent intentToFire = new Intent(getApplicationContext(), AlarmBroadcastReceiverReminderNotita.class);
        intentToFire.setAction(AlarmBroadcastReceiverReminderNotita.ACTION_ALARM);

        intentToFire.putExtra("notita",notita);
        intentToFire.putExtra("sectiune",sectiuneDeLegat);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                1, intentToFire, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmIntent);
    }
}
