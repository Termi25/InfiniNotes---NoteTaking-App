package com.ase.aplicatienotite.main.activitati;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.main.receiver.AlarmBroadcastReceiver;

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
    private EditText etTitluNotita;
    private EditText etCorpNotita;
    private Spinner spinnerSectiuni;
    private Date dataReminder=null;
    private Calendar calendarDeTransmis=Calendar.getInstance();
    private Button btnReminderNotita;
    private Button btnOraReminderNotita;
    private ImageButton imgBtnBack;
    private ImageButton imgBtnShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_editeaza_notita);
        Intent intent=getIntent();

        etTitluNotita=findViewById(R.id.etNumeAdaugaNotita);
        etCorpNotita =findViewById(R.id.etCorpTextNotita);

        this.btnReminderNotita = findViewById(R.id.btnReminderEditeazaNotita);
        this.btnOraReminderNotita=findViewById(R.id.btnOraReminderEditeazaNotita);
        SimpleDateFormat dateFormat=new SimpleDateFormat(getString(R.string.pattern_data),
                Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",
                Locale.ENGLISH);

        seteazaBtnReminder(dateFormat);
        seteazaBtnOraReminder();

        if(intent.getExtras()!=null){
            Notita notita=(Notita)intent.getSerializableExtra("Notita");
            if(notita!=null){
                incarcareNotita(notita, dateFormat, timeFormat);
                setareButonPartajare(notita);
            }
        }

    }

    private void setareButonPartajare(Notita notita) {
        imgBtnShare=findViewById(R.id.imgBtnShareNotita);
        imgBtnShare.setOnClickListener(v->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, notita.getCorp());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, notita.getTitlu());
            startActivity(shareIntent);
        });
    }

    private void incarcareNotita(Notita notita, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat) {
        etTitluNotita.setText(notita.getTitlu());
        etCorpNotita.setText(notita.getCorp());
        if(notita.getDataReminder()!=null){
            btnReminderNotita.setText(dateFormat.format(notita.getDataReminder()));
            btnOraReminderNotita.setText(timeFormat.format(notita.getDataReminder()));
            dataReminder=notita.getDataReminder();
            calendarDeTransmis.setTime(notita.getDataReminder());
        }

        imgBtnBack=findViewById(R.id.btnAnulareEditareNotita);
        imgBtnBack.setOnClickListener(v->setareButonIntoarcere(notita));

        sectiuneViewModel=new ViewModelProvider(this).get(SectiuniViewModel.class);

        sectiuneViewModel.getToateSectiuni(4).observe(this,sectiuni->{
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
                    Sectiune sectiuneActuala=db.getSectiuneNotiteJoinDao().getSectiune(notita.getNotitaId());
                    runOnUiThread(()->spinnerSectiuni.setSelection(listaSpinnerSectiuni.indexOf(sectiuneActuala.getDenumireSectiune())));
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

    private void setareButonIntoarcere(Notita notita) {
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB db=NotiteDB.getInstance(getApplicationContext());

            int idSectiune=db.getSectiuneNotiteJoinDao().getIdSectiune(notita.getNotitaId());

            Sectiune sectiuneDeLegat=db.getSectiuneDao().getSectiuneCuId(idSectiune);
            try{
                notita.setTitlu(String.valueOf(etTitluNotita.getText()));
                notita.setCorp(String.valueOf(etCorpNotita.getText()));
                if(dataReminder!=null){
                    notita.setDataReminder(calendarDeTransmis.getTime());
                    setareAlarma(notita,sectiuneDeLegat,calendarDeTransmis);
                }
                db.getNotitaDao().updateNotita(notita);
            }catch (Exception e){
                Log.e("Error",getString(R.string.error_editeaza_notita_update_notita1));
            }
        });
        Toasty.success(getApplicationContext(),R.string.modificari_succes, Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    private void seteazaBtnReminder(SimpleDateFormat simpleDateFormat) {
        LocaleList locale = getResources().getConfiguration().getLocales();
        Locale.setDefault(locale.get(0));
        this.btnReminderNotita.setOnClickListener(v -> {
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
                        builderData.append(year1);
                        this.btnReminderNotita.setText(builderData.toString());

                        String dataString= dayOfMonth + " / " + (monthOfYear + 1) + " / " + year1;
                        try{
                            this.dataReminder=simpleDateFormat.parse(dataString);
                            if(this.dataReminder!=null){
                                this.calendarDeTransmis.setTime(dataReminder);
                            }
                        }catch (ParseException e){
                            Log.e("Error",getString(R.string.error_editeaza_notita_update_notita4));
                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void seteazaBtnOraReminder(){

        final Calendar calendar = Calendar.getInstance();
        if(this.dataReminder!=null){
            calendar.setTime(this.dataReminder);
        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        this.btnOraReminderNotita.setOnClickListener(v->{
            if(!this.btnReminderNotita.getText().toString().equalsIgnoreCase("data reminder")){
                TimePickerDialog timePickerDialog=new TimePickerDialog(ActivitateEditeazaNotita.this,(view, hourOfDay, minute1) -> {
                    StringBuilder builderData=new StringBuilder();
                    if(minute1>9){
                        builderData.append(hourOfDay).append(":").append(minute1);
                    }else{
                        builderData.append(hourOfDay).append(":").append(0).append(minute1);
                    }
                    this.btnOraReminderNotita.setText(builderData.toString());

                    this.calendarDeTransmis.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    this.calendarDeTransmis.set(Calendar.MINUTE,minute1);
                    this.calendarDeTransmis.set(Calendar.SECOND,0);
                },hour,minute,true);
                timePickerDialog.show();
            }else{
                Toasty.warning(getApplicationContext(),"Setați data reminderului mai întâi.").show();
            }
        });
    }

    private void setareAlarma(Notita notita, Sectiune sectiuneDeLegat, Calendar calendar) {
        Calendar calendarValidare=Calendar.getInstance();
        if(calendar.getTimeInMillis()>calendarValidare.getTimeInMillis()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 0);
                }
            }

            Intent intentToFire = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
            intentToFire.setAction(AlarmBroadcastReceiver.ACTION_ALARM);

            intentToFire.putExtra("notita",notita);
            intentToFire.putExtra("sectiune",sectiuneDeLegat);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    1, intentToFire, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                    getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmIntent);
        }
    }
}
