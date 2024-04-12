package com.ase.aplicatienotite.baze_date.local.view.holder;

import static androidx.core.content.ContextCompat.getDrawable;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;
import com.ase.aplicatienotite.main.activitati.ActivitateVizualNotiteSectiune;


public class SectiuneViewHolder extends RecyclerView.ViewHolder {
    static ActivityResultLauncher<Intent> launcher;
    private final TextView tvNumeSectiune;
    private final TextView tvNumeNotita1;
    private final TextView tvNumeLista;
    private final Button btnVizualizareNotiteDinSectiune;
    private final Spinner spnCuloareSectiuneEditare;

    public static Context context;
    
    public SectiuneViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvNumeSectiune=itemView.findViewById(R.id.tvNumeSectiune);
        this.tvNumeNotita1=itemView.findViewById(R.id.tvNotita1);
        this.tvNumeLista=itemView.findViewById(R.id.tvLista);
        this.btnVizualizareNotiteDinSectiune=itemView.findViewById(R.id.btnVizualNotiteSectiune);
        this.spnCuloareSectiuneEditare=itemView.findViewById(R.id.spCuloareSectiuneEditare);
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
        try{
            incarcareUI(sectiune);
        }catch (Exception e){
            e.printStackTrace();
        }

        this.btnVizualizareNotiteDinSectiune.setOnClickListener(v -> {
            Intent intent=new Intent(context, ActivitateVizualNotiteSectiune.class);
            intent.putExtra("codSectiune",sectiune.getSectiuneId());
            startActivity(context,intent,null);
        });
    }

    public static SectiuneViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sectiune,parent,false);
        context=parent.getContext();
        return new SectiuneViewHolder(view);
    }

    private void incarcareUI(Sectiune sectiune){
        try{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                sectiune.setNotite(db.getSectiuneNotiteJoinDao()
                        .getNotitePentruSectiune(sectiune.getSectiuneId()));

                sectiune.setNotiteLista(db.getSectiuneNotiteListaJoinDao()
                        .getNotiteListaPentruSectiune(sectiune.getSectiuneId()));

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
            });
        }catch (Exception e){
            e.printStackTrace();
        }
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
        spnCuloareSectiuneEditare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

}
