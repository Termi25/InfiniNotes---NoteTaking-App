package com.ase.aplicatienotite.adaptoare;

import static com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneViewHolder.context;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;

import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.repository.SectiuneRepository;
import com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneViewHolder;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class AdapterSectiune extends ListAdapter<Sectiune, SectiuneViewHolder> {

    private SectiuniViewModel sectiuneViewModel;
    private List<Sectiune> listaSectiune=new ArrayList<>();

    public AdapterSectiune(@NonNull DiffUtil.ItemCallback<Sectiune> diffCallback) {
        super(diffCallback);
    }

    @Override
    public SectiuneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SectiuneViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SectiuneViewHolder holder, int position) {
        Sectiune curenta=getItem(position);
        holder.bind(curenta);
    }

    public static class SectiuneDiff extends DiffUtil.ItemCallback<Sectiune>{

        @Override
        public boolean areItemsTheSame(@NonNull Sectiune oldItem, @NonNull Sectiune newItem) {
            return oldItem.getSectiuneId() == newItem.getSectiuneId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Sectiune oldItem, @NonNull Sectiune newItem) {
            if(oldItem.getNotite()!=null && newItem.getNotite()!=null){
                return oldItem.getNotite().size()==newItem.getNotite().size();
            }else{
                return oldItem.getDenumireSectiune().equalsIgnoreCase(newItem.getDenumireSectiune());
            }
        }
    }


}
