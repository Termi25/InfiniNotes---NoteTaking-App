package com.ase.aplicatienotite.baze_date.local.view.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.repository.SectiuneRepository;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;

public class SectiuniViewModel extends AndroidViewModel {

    private SectiuneRepository sRepository;
    private final LiveData<List<Sectiune>>toateSectiunile;
    public SectiuniViewModel(Application application) {
        super(application);
        sRepository=new SectiuneRepository(application);
        toateSectiunile=sRepository.getToateSectiuni();
    }

    public LiveData<List<Sectiune>> getToateSectiuni(){
        return toateSectiunile;
    }

    public void insert(Sectiune sectiune){
        sRepository.insert(sectiune);
    }
}
