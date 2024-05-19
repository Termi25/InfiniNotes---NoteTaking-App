package com.ase.aplicatienotite.baze_date.local.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.repository.SectiuneNotiteJoinRepository;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

public class SectiuneNotiteJoinViewModel extends AndroidViewModel {

    private SectiuneNotiteJoinRepository sRepository;
    private Application application;
    private LiveData<List<Notita>> toateNotiteleSectiunii;
    public SectiuneNotiteJoinViewModel(@NonNull Application application) {
        super(application);
        this.application=application;

    }

    public LiveData<List<Notita>> getToateNotiteleSectiunii(int idSectiune,int tipOrdine) {
        sRepository=new SectiuneNotiteJoinRepository(application,idSectiune);
        toateNotiteleSectiunii=sRepository.getToateNotiteleSectiunii(tipOrdine);
        return toateNotiteleSectiunii;
    }
}
