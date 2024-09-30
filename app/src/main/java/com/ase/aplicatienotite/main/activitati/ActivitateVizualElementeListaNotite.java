package com.ase.aplicatienotite.main.activitati;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterElementeLista;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.model.ListaNotiteJoinViewModel;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class ActivitateVizualElementeListaNotite extends AppCompatActivity {
    private ListaNotiteJoinViewModel listaNotiteListaJoinViewModel;
    private NotitaLista notitaLista;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_elemente_lista);

        incarcareVizualListe();

        pregatireAdaugareElementLista();

        setareFloatingActionButtonInchidere();
    }

    private void incarcareVizualListe() {
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            notitaLista= (NotitaLista) extras.getSerializable("notitaLista");
            actualizareDataAccesareLista(notitaLista);

            RecyclerView rlv=findViewById(R.id.rlvElementeLista);
            final AdapterElementeLista adapter =new AdapterElementeLista(new AdapterElementeLista.ElementListaDiff());
            rlv.setAdapter(adapter);
            rlv.setLayoutManager(new LinearLayoutManager(this));

            if(notitaLista!=null){
                loadRecyclerView(adapter,notitaLista.getNotitaId());

                TextView tvNumeLista=findViewById(R.id.tvNumeLista);
                if(!notitaLista.getTitlu().isEmpty()){
                    if(notitaLista.getTitlu().toLowerCase().contains("lista")){
                        tvNumeLista.setText(notitaLista.getTitlu());
                    }else{
                        if(notitaLista.getTitlu().toLowerCase().contains("listă")){
                            tvNumeLista.setText(notitaLista.getTitlu());
                        }else{
                            tvNumeLista.setText(String.format("Lista: %s", notitaLista.getTitlu()));
                        }
                    }
                }
            }
        }
    }

    private void loadRecyclerView(AdapterElementeLista adapter, int idLista) {
        listaNotiteListaJoinViewModel=new ViewModelProvider(this).get(ListaNotiteJoinViewModel.class);

        listaNotiteListaJoinViewModel.getToateElementeleListei(idLista).observe(this, adapter::submitList);
    }

    private void pregatireAdaugareElementLista() {
        EditText etAdaugareElementLista=findViewById(R.id.etAdaugareElementLista);
        ImageButton imgBtnAdaugareElementLista=findViewById(R.id.imgBtnAdaugareElementLista);
        TextView tvNumeLista=findViewById(R.id.tvNumeLista);
        RecyclerView rlv=findViewById(R.id.rlvElementeLista);

        imgBtnAdaugareElementLista.setOnClickListener(v -> {
            try{
                int numarElement=0;
                if(rlv.getAdapter()!=null){
                    numarElement=rlv.getAdapter().getItemCount()+1;
                    ElementLista elementListaNou=new ElementLista(
                            tvNumeLista.getText().toString()+ String.valueOf(numarElement),
                            etAdaugareElementLista.getText().toString());
                    adaugaElementLista(elementListaNou);
                    etAdaugareElementLista.setText("");
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        });
    }

    private void adaugaElementLista(ElementLista elementListaNou) {
        NotiteDB.databaseWriteExecutor.execute(()-> {
            NotiteDB db = NotiteDB.getInstance(getApplicationContext());
            try {
                db.getElementListaDao().insertElementLista(elementListaNou);

                elementListaNou.setNotitaId(db.getElementListaDao()
                        .getElementListaDupaTitlu(elementListaNou.getTitlu()).getNotitaId());

                ListaNotiteJoin legaturaNoua = new ListaNotiteJoin(notitaLista.getNotitaId(),
                        elementListaNou.getNotitaId());
                db.getListaNotiteJoinDao().insert(legaturaNoua);
            } catch (Exception e) {
                Log.e("Error",getString(R.string.error_elementelistanotite_save_element_lista_nou));
            }
        });
    }

    private void actualizareDataAccesareLista(NotitaLista extras){
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB db = NotiteDB.getInstance(getApplicationContext());
            try {
                extras.setDataAccesare(new Date());
                db.getNotitaListaDao().updateNotitaLista(extras);
            } catch (Exception e) {
                Log.e("Error","Eroare actualizare data accesare lista");
            }
        });
    }

    private void setareFloatingActionButtonInchidere() {
        FloatingActionButton fActBtnInchidereVizualListe=findViewById(R.id.fActBtnInchidereVizualElementeLista);
        fActBtnInchidereVizualListe.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
}
