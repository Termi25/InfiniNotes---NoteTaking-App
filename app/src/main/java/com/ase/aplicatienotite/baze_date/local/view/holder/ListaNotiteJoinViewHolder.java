package com.ase.aplicatienotite.baze_date.local.view.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ListaNotiteJoinViewHolder extends RecyclerView.ViewHolder {

    private static Context context;
    private final TextView tvCorpElementNotita;
    private final Button btnStergereElementNotita;
    private final CheckBox cbCheckedElementNotita;

    public ListaNotiteJoinViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCorpElementNotita = itemView.findViewById(R.id.tvCorpElementLista);
        btnStergereElementNotita = itemView.findViewById(R.id.btnStergereElementLista);
        cbCheckedElementNotita = itemView.findViewById(R.id.cbElementLista);
    }

    public void bind(ElementLista elementLista){
        if(!elementLista.getCorp().isEmpty()){
            tvCorpElementNotita.setText(elementLista.getCorp());
        }

        setareChecked(elementLista);

        setareButonStergere(elementLista);
    }

    public static ListaNotiteJoinViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_element_lista,parent,false);
        context=parent.getContext();
        return new ListaNotiteJoinViewHolder(view);
    }

    private void setareChecked(ElementLista elementLista) {
        this.cbCheckedElementNotita.setChecked(elementLista.isChecked());
        this.cbCheckedElementNotita.setOnCheckedChangeListener((buttonView, isChecked) -> {
            elementLista.setChecked(isChecked);
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                db.getElementListaDao().updateElementLista(elementLista);
            });
        });
    }

    private void setareButonStergere(ElementLista elementLista) {
        this.btnStergereElementNotita.setOnClickListener(v->{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                List<ListaNotiteJoin> listaLegaturi=db.getListaNotiteJoinDao()
                        .getLegaturiCuElementulLista(elementLista.getNotitaId());

                for(int i=0;i<listaLegaturi.size();i++){
                    db.getListaNotiteJoinDao().deleteLegatura(listaLegaturi.get(i));
                }
                db.getNotitaDao().deleteNotita(elementLista);
            });
            Toasty.success(context,R.string.modificari_succes, Toast.LENGTH_LONG).show();
        });
    }
}
