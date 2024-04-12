package com.ase.aplicatienotite.baze_date.local.view.holder;

import static androidx.core.content.ContextCompat.getDrawable;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;
import com.ase.aplicatienotite.main.activitati.ActivitateVizualNotiteSectiune;

import java.util.List;


public class SectiuneViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvNumeSectiune;
    private final TextView tvNumeNotita1;
    private final TextView tvNumeLista;
    private final Button btnVizualizareNotiteDinSectiune;
    private final Spinner spnCuloareSectiuneEditare;
    private final Button btnStergereSectiune;

    public static Context context;
    
    public SectiuneViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvNumeSectiune=itemView.findViewById(R.id.tvNumeSectiune);
        this.tvNumeNotita1=itemView.findViewById(R.id.tvNotita1);
        this.tvNumeLista=itemView.findViewById(R.id.tvLista);
        this.btnVizualizareNotiteDinSectiune=itemView.findViewById(R.id.btnVizualNotiteSectiune);
        this.spnCuloareSectiuneEditare=itemView.findViewById(R.id.spCuloareSectiuneEditare);
        this.btnStergereSectiune = itemView.findViewById(R.id.btnStergereSectiune);
    }
    public void bind(Sectiune sectiune){
        this.tvNumeSectiune.setText(sectiune.getDenumireSectiune());
        this.tvNumeNotita1.setText("");
        this.tvNumeLista.setText("");

        if(sectiune.getCuloareSectiune()==null){
            sectiune.setCuloareSectiune(CuloriSectiune.MARO);
        }

        incarcareSpinnerCulori(sectiune);

        incarcareCuloareSectiune(sectiune);

        incarcareUI(sectiune);

        setareButonVizualNotite(sectiune);

        setareButonStergereSectiune(sectiune);
    }

    private void setareButonStergereSectiune(Sectiune sectiune) {
        this.btnStergereSectiune.setOnClickListener(v->{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);

                List<SectiuneNotiteJoin> legaturiNotite=db.getSectiuneNotiteJoinDao()
                        .getLegaturiCuSectiune(sectiune.getSectiuneId());

                for(SectiuneNotiteJoin legatura:legaturiNotite){
                    SectiuneNotiteJoin copie=new SectiuneNotiteJoin(legatura.notitaId,legatura.sectiuneId);
                    copie.sectiuneId=db.getSectiuneDao().getSectiuneCuDenumire("MISC").getSectiuneId();
                    db.getSectiuneNotiteJoinDao().insert(copie);
                    db.getSectiuneNotiteJoinDao().deleteLegatura(legatura);
                }

                List<SectiuneNotiteListaJoin> legaturiNotiteLista=db.getSectiuneNotiteListaJoinDao()
                        .getLegaturiCuSectiune(sectiune.getSectiuneId());

                for(SectiuneNotiteListaJoin legatura:legaturiNotiteLista){
                    SectiuneNotiteListaJoin copie=new SectiuneNotiteListaJoin(legatura.notitaId,legatura.sectiuneId);
                    copie.sectiuneId=db.getSectiuneDao().getSectiuneCuDenumire("MISC").getSectiuneId();
                    db.getSectiuneNotiteListaJoinDao().insert(copie);
                    db.getSectiuneNotiteListaJoinDao().deleteLegatura(legatura);

                }

                db.getSectiuneDao().deleteSectiune(sectiune);
            });
        });
    }

    public static SectiuneViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sectiune,parent,false);
        context=parent.getContext();
        return new SectiuneViewHolder(view);
    }

    private void incarcareCuloareSectiune(Sectiune sectiune){
        switch (sectiune.getCuloareSectiune()){
            case MARO:{
                this.itemView.setBackground(getDrawable(context,R.drawable.border_lv_principal));
                break;
            }
            case ROSU:{
                this.itemView.setBackground(getDrawable(context,R.drawable.border_lv_red));
                break;
            }
            case ALBASTRU:{
                this.itemView.setBackground(getDrawable(context,R.drawable.border_lv_blue));
                break;
            }
            case VERDE:{
                this.itemView.setBackground(getDrawable(context,R.drawable.border_lv_green));
                break;
            }
            case VIOLET:{
                this.itemView.setBackground(getDrawable(context,R.drawable.border_lv_purple));
                break;
            }
            default:
                this.itemView.setBackground(getDrawable(context,R.drawable.border_lv_principal));
        }
    }

    private void incarcareSpinnerCulori(Sectiune sectiune) {
        this.spnCuloareSectiuneEditare.setAdapter(new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item, CuloriSectiune.values()));
        int pozitieSelectie=determinareCuloare(sectiune);
        this.spnCuloareSectiuneEditare.setSelection(pozitieSelectie);
        this.spnCuloareSectiuneEditare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectiune.setCuloareSectiune(CuloriSectiune.valueOf(spnCuloareSectiuneEditare.getSelectedItem().toString()));
                incarcareCuloareSectiune(sectiune);

                NotiteDB.databaseWriteExecutor.execute(()->{
                    NotiteDB db=NotiteDB.getInstance(context);
                    db.getSectiuneDao().updateSectiune(sectiune);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int determinareCuloare(Sectiune sectiune){
        int pozitieSelectie;
        switch (sectiune.getCuloareSectiune()){
            case ALBASTRU:{
                pozitieSelectie=1;
                break;
            }
            case ROSU:{
                pozitieSelectie=2;
                break;
            }
            case VERDE:{
                pozitieSelectie=3;
                break;
            }
            case VIOLET:{
                pozitieSelectie=4;
                break;
            }
            default:
                pozitieSelectie=0;
        }
        return pozitieSelectie;
    }

    private void incarcareUI(Sectiune sectiune){
        try{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                sectiune.setNotite(db.getSectiuneNotiteJoinDao()
                        .getNotitePentruSectiune(sectiune.getSectiuneId()));

                sectiune.setNotiteLista(db.getSectiuneNotiteListaJoinDao()
                        .getNotiteListaPentruSectiune(sectiune.getSectiuneId()));

                try{
                    if(sectiune.getNotite()!=null){
                        if(!sectiune.getNotite().isEmpty()){
                            this.tvNumeNotita1.setText(sectiune.getNotite().get(0).getTitlu());
                        }
                    }
                    if(sectiune.getNotiteLista()!=null){
                        if(!sectiune.getNotiteLista().isEmpty()){
                            this.tvNumeLista.setText(String.format("Lista: %s", sectiune.getNotiteLista().get(0).getTitlu()));
                        }
                    }
                }catch (Exception e){
                    Log.e("Error","Eroare incarcare preview sectiune in SectiuneViewHolder");
                }
            });
        }catch (Exception e){
            Log.e("Error","Eroare incarcare preview sectiune in SectiuneViewHolder");
        }
    }

    private void setareButonVizualNotite(Sectiune sectiune) {
        this.btnVizualizareNotiteDinSectiune.setOnClickListener(v -> {
            Intent intent=new Intent(context, ActivitateVizualNotiteSectiune.class);
            intent.putExtra("codSectiune",sectiune.getSectiuneId());
            startActivity(context,intent,null);
        });
    }

}
