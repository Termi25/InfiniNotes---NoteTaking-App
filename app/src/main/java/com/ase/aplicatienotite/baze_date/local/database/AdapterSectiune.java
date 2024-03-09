package com.ase.aplicatienotite.baze_date.local.database;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.SectiuneViewHolder;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class AdapterSectiune extends ListAdapter<Sectiune, SectiuneViewHolder> {

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
        holder.bind(curenta.getDenumireSectiune());
    }

    public static class SectiuneDiff extends DiffUtil.ItemCallback<Sectiune>{

        @Override
        public boolean areItemsTheSame(@NonNull Sectiune oldItem, @NonNull Sectiune newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Sectiune oldItem, @NonNull Sectiune newItem) {
            return oldItem.getDenumireSectiune().equals(newItem.getDenumireSectiune());
        }
    }
}
