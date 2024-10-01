package com.ase.aplicatienotite.main.activitati;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.NotiteRecenteViewModel;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ActivitatePrincipala extends AppCompatActivity {
    private static ActivityResultLauncher<Intent> launcher;
    private AdapterSectiune adapterSectiune;
    private TextView notitaRecenta;
    private TextView listaRecenta;
    private RecyclerView rlvSectiuni;
    private Spinner spOrdineSectiuni;
    private Observer<List<Sectiune>> observerListaSectiuni;
    LiveData<List<Sectiune>> dateSectiuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
        cererePermisiuniNecesare();

        initializareToasty();

        this.rlvSectiuni =findViewById(R.id.rlv_main);
        this.adapterSectiune = new AdapterSectiune(new AdapterSectiune.SectiuneDiff());
        this.rlvSectiuni.setAdapter(this.adapterSectiune);
        this.rlvSectiuni.setLayoutManager(new LinearLayoutManager(this));

        this.spOrdineSectiuni=findViewById(R.id.spOrdineSectiuni);
        incarcareSpinnerOrdineSectiuni();
        incarcareRecyclerViewSectiuni();
        notifyAdapterSectiuni();

        incarcareNotitaSiListaRecente();

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Toasty.success(getApplicationContext(),getString(R.string.modificari_succes),Toast.LENGTH_LONG).show();
                try{
                    notifyAdapterSectiuni();
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
        notifyAdapterSectiuni();
        
        
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
                    incarcareRecyclerViewSectiuni();
                }
                return false;
            }
        });
    }

    private void filtruCautare(String text) {
        List<Sectiune> filteredList = new ArrayList<Sectiune>();

        for (Sectiune item : this.adapterSectiune.getCurrentList()) {
            if (item.getDenumireSectiune().toLowerCase().contains(text.toLowerCase())) {
                System.out.println(item);
                filteredList.add(item);
            }
        }
        this.adapterSectiune.submitList(filteredList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        incarcareNotitaSiListaRecente();
    }

    @Override
    protected void onResume() {
        super.onResume();
        incarcareNotitaSiListaRecente();
        notifyAdapterSectiuni();
    }

    void initializareToasty(){
        Toasty.Config.getInstance()
                .setToastTypeface(Objects.requireNonNull(ResourcesCompat.getFont(this, R.font.alata)))
                .apply();
    }

    void incarcareSpinnerOrdineSectiuni(){
        this.spOrdineSectiuni.setAdapter(new ArrayAdapter<>
                (getApplicationContext(), R.layout.view_spinner, new String[]{"Alfabetic A-Z",
                        "Alfabetic Z-A","După notițe crescător","După notițe descrescător","După dată creare"}));

        SharedPreferences sharedPrefs = getSharedPreferences("preferences.xml", MODE_PRIVATE);
        if(!sharedPrefs.contains("ordineSectiuni")){
            SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
            editor.putInt("ordineSectiuni",0);
            editor.apply();
        }
        int[] ordineSectiuni = {sharedPrefs.getInt("ordineSectiuni", 0)};
        this.spOrdineSectiuni.setSelection(ordineSectiuni[0]);
        this.spOrdineSectiuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incarcareRecyclerViewSectiuni();
                SharedPreferences.Editor editor=getSharedPreferences("preferences.xml", MODE_PRIVATE).edit();
                editor.putInt("ordineSectiuni",position);
                editor.apply();
                ordineSectiuni[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void incarcareRecyclerViewSectiuni(){
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
        if(observerListaSectiuni!=null){
            dateSectiuni.removeObserver(observerListaSectiuni);
        }
        dateSectiuni = sectiuneViewModel.getToateSectiuni(optiuneOrdonare);
        observerListaSectiuni=new Observer<List<Sectiune>>() {
            @Override
            public void onChanged(List<Sectiune> sectiuni) {
                if(sectiuni.isEmpty()){
                    NotiteDB.databaseWriteExecutor.execute(()->{
                        NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                        Sectiune misc=new Sectiune(getString(R.string.misc), CuloriSectiune.MARO);
                        db.getSectiuneDao().insertSectiune(misc);
                    });
                }
                AdapterSectiune adapter = (AdapterSectiune) rlvSectiuni.getAdapter();
                assert adapter != null;
                adapter.submitList(sectiuni);
            }
        };
        dateSectiuni.observe(this, observerListaSectiuni);
    }

    void notifyAdapterSectiuni(){
        try{
            rlvSectiuni.getAdapter().notifyDataSetChanged();
        }catch (Exception e){
            Log.e("Error","Eroare adapter RecyclerView Sectiuni ActivitatePrincipala");
        }
    }

    private void incarcareNotitaSiListaRecente() {
        NotiteRecenteViewModel notiteRecenteViewModel = new ViewModelProvider(this).get(NotiteRecenteViewModel.class);
        Notita notita=notiteRecenteViewModel.getNotitaAccesataRecent();
        NotitaLista lista=notiteRecenteViewModel.getListaAccesataRecent();

        TextView tvDataAccesareNotita=findViewById(R.id.tvDataAccesareNotita);
        TextView tvDataAccesareLista=findViewById(R.id.tvDataAccesareLista);

        DateFormat df= new DateFormat();

        try{
            this.notitaRecenta=findViewById(R.id.tvNotitaRecenta);

            this.notitaRecenta.setText(notita.getTitlu());
            tvDataAccesareNotita.setText(String.valueOf(DateFormat.format("dd-MM-yyyy hh:mm", notita.getDataAccesare())));

            this.notitaRecenta.setOnClickListener(v -> {
                Intent intent=new Intent(this.getApplicationContext(), ActivitateEditeazaNotita.class);
                intent.putExtra("Notita",notita);
                this.startActivity(intent,null);
            });
        }catch (Exception e){
            this.notitaRecenta.setText("");
            Log.e("Error","Eroare incarcare notita recenta.");
        }
        try{
            this.listaRecenta=findViewById(R.id.tvNotitaListaRecenta);

            this.listaRecenta.setText(lista.getTitlu());
            tvDataAccesareLista.setText(String.valueOf(DateFormat.format("dd-MM-yyyy hh:mm", lista.getDataAccesare())));

            this.listaRecenta.setOnClickListener(v -> {
                Intent intent=new Intent(this.getApplicationContext(), ActivitateVizualElementeListaNotite.class);
                intent.putExtra("notitaLista",lista);
                this.startActivity(intent,null);
            });
        }catch (Exception e){
            this.listaRecenta.setText("");
            Log.e("Error","Eroare incarcare lista recenta.");
        }

    }

    void cererePermisiuniNecesare(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(ActivitatePrincipala.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
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
        AdapterSectiune adapterNou = (AdapterSectiune) this.rlvSectiuni.getAdapter();
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