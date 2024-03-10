package com.ase.aplicatienotite.baze_date.local.view.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.repository.SectiuneRepository;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;
import java.util.Map;

public class SectiuniViewModel extends AndroidViewModel {
    private SectiuneRepository sRepository;
    private final LiveData<List<Sectiune>>toateSectiunile;
    private final LiveData<Map<Sectiune,List<Notita>>> toateSectiunileCuNotite;
    public SectiuniViewModel(Application application) {
        super(application);
        sRepository=new SectiuneRepository(application);
        toateSectiunile=sRepository.getToateSectiuni();
        toateSectiunileCuNotite=sRepository.getToateSectiuniCuNotite();
    }

    public LiveData<List<Sectiune>> getToateSectiuni(){
        return toateSectiunile;
    }
    public LiveData<Map<Sectiune,List<Notita>>> getToateSectiunileCuNotite(){
        return toateSectiunileCuNotite;
    }

    public void insert(Sectiune sectiune){
        sRepository.insert(sectiune);
    }
}
