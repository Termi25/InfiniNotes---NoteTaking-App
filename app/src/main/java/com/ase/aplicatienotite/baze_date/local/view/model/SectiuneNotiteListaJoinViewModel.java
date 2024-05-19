package com.ase.aplicatienotite.baze_date.local.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.repository.SectiuneNotiteJoinRepository;
import com.ase.aplicatienotite.baze_date.local.repository.SectiuneNotiteListaJoinRepository;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.List;

public class SectiuneNotiteListaJoinViewModel extends AndroidViewModel {
    private SectiuneNotiteListaJoinRepository sRepository;
    private Application application;
    private LiveData<List<NotitaLista>> toateNotiteleListaSectiunii;
    public SectiuneNotiteListaJoinViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }
    public LiveData<List<NotitaLista>> getToateNotiteleListaSectiunii(int idSectiune,int tipOrdonare) {
        sRepository=new SectiuneNotiteListaJoinRepository(application,idSectiune);
        toateNotiteleListaSectiunii=sRepository.getToateNotiteleListaSectiunii(tipOrdonare);
        return toateNotiteleListaSectiunii;
    }
}
