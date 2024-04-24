package com.ase.aplicatienotite.main.activitati;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ActivitatePrincipala extends AppCompatActivity {
    private static ActivityResultLauncher<Intent> launcher;
    private AdapterSectiune adapter;
    private RecyclerView rlv;
    private Spinner spOrdineSectiuni;
    private List<Sectiune> initialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
        cererePermisiuniNecesare();

        initializareToasty();

        this.rlv=findViewById(R.id.rlv_main);
        this.adapter = new AdapterSectiune(new AdapterSectiune.SectiuneDiff());
        this.rlv.setAdapter(this.adapter);
        this.rlv.setLayoutManager(new LinearLayoutManager(this));

        this.spOrdineSectiuni=findViewById(R.id.spOrdineSectiuni);
        incarcareSpinnerOrdineSectiuni();
        incarcareRecyclerView();
        notifyAdapter();

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Toasty.success(getApplicationContext(),getString(R.string.modificari_succes),Toast.LENGTH_LONG).show();
                try{
                    notifyAdapter();
                }catch (Exception e){
                    Log.e("Error","Eroare notifyDataSetChanged pentru RecyclerView in Activitate principala");
                }
            }else{
                Toasty.error(getApplicationContext(), getString(R.string.modificari_esec),Toasty.LENGTH_LONG).show();
            }
        });

        ImageButton btnSetari=findViewById(R.id.imgBtnSetari);
        btnSetari.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),ActivitateSetari.class);
            launcher.launch(intent);
        });

        ImageButton btnAdauga=findViewById(R.id.imgBtnAdaugareGenerala);
        btnAdauga.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),ActivitateAdaugareGenerala.class);
            launcher.launch(intent);
        });
        notifyAdapter();
        
        
        setareCautareSectiuni();
    }

    private void setareCautareSectiuni() {
        SearchView svSectiuni=findViewById(R.id.svSectiuni);
        svSectiuni.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtruCautare(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    incarcareRecyclerView();
                }
                return false;
            }
        });
    }

    private void filtruCautare(String text) {
        List<Sectiune> filteredList = new ArrayList<Sectiune>();

        for (Sectiune item : this.initialList) {
            if (item.getDenumireSectiune().toLowerCase().contains(text.toLowerCase())) {
                System.out.println(item);
                filteredList.add(item);
            }
        }
        this.adapter.submitList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyAdapter();
    }

    void initializareToasty(){
        Toasty.Config.getInstance()
                .setToastTypeface(Objects.requireNonNull(ResourcesCompat.getFont(this, R.font.alata)))
                .apply();
    }

    void incarcareSpinnerOrdineSectiuni(){
        this.spOrdineSectiuni.setAdapter(new ArrayAdapter<>
                (getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Alfabetic A-Z",
                        "Alfabetic Z-A","După număr notițe crescător","După număr notițe descrescător","După dată creare"}));

        SharedPreferences sharedPrefs = getSharedPreferences("preferences.xml", MODE_PRIVATE);
        if(!sharedPrefs.contains("ordineSectiuni")){
            SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
            editor.putInt("ordineSectiuni",0);
            editor.apply();
        }
        final int[] ordineSectiuni = {sharedPrefs.getInt("ordineSectiuni", 0)};
        this.spOrdineSectiuni.setSelection(ordineSectiuni[0]);
        Log.d("TEST", String.valueOf(ordineSectiuni[0]));
        this.spOrdineSectiuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incarcareRecyclerView();
                SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
                editor.putInt("ordineSectiuni",position);
                editor.apply();
                ordineSectiuni[0] =position;
                Log.d("TEST", String.valueOf(ordineSectiuni[0]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void incarcareRecyclerView(){
        SectiuniViewModel sectiuneViewModel = new ViewModelProvider(this).get(SectiuniViewModel.class);
        int optiuneOrdonare=0;
        switch (this.spOrdineSectiuni.getSelectedItemPosition()){
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
        sectiuneViewModel.getToateSectiuni(optiuneOrdonare).observe(this, sectiuni->{
            if(sectiuni.isEmpty()){
                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    Sectiune misc=new Sectiune(getString(R.string.misc), CuloriSectiune.MARO);
                    db.getSectiuneDao().insertSectiune(misc);
                });
            }
            this.initialList=new ArrayList<>();
            this.initialList.addAll(sectiuni);
            this.adapter.submitList(sectiuni);
        });
    }

    void notifyAdapter(){
        try{
            incarcareRecyclerView();
        }catch (Exception e){
            Log.e("Error","Eroare adapter recyclerview ActivitatePrincipala");
        }
    }

    void cererePermisiuniNecesare(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ActivitatePrincipala.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivitatePrincipala.this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 0);
            }
        }
        NotificationManager notificationManager= (NotificationManager) getApplicationContext().getSystemService(
                Context.NOTIFICATION_SERVICE);

        if(notificationManager.areNotificationsEnabled()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(ActivitatePrincipala.this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 0);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AdapterSectiune adapterNou = (AdapterSectiune) this.rlv.getAdapter();
        if(adapterNou!=null){
            List<Sectiune> listaSectiuni = new ArrayList<>(adapterNou.getCurrentList());
            NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                    for(Sectiune sectiune:listaSectiuni){
                        db.getSectiuneDao().updateSectiune(sectiune);
                    }
                });
        }
    }
}