package com.ase.aplicatienotite.baze_date.local.view.model;

import static com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneViewHolder.context;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.repository.SectiuneRepository;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.List;
import java.util.Map;

public class SectiuniViewModel extends AndroidViewModel {
    private SectiuneRepository sRepository;
    private LiveData<List<Sectiune>>toateSectiunile;
    private final LiveData<Map<Sectiune,List<Notita>>> toateSectiunileCuNotite;
    public SectiuniViewModel(Application application) {
        super(application);
        this.sRepository=new SectiuneRepository(application);
        this.toateSectiunile=this.sRepository.getToateSectiuni(0);
        this.toateSectiunileCuNotite=sRepository.getToateSectiuniCuNotite();
    }

    public LiveData<List<Sectiune>> getToateSectiuni(int tipOrdonare){
        switch(tipOrdonare){
            case 0:{
                this.toateSectiunile=this.sRepository.getToateSectiuni(0);
                break;
            }
            case 1:{
                this.toateSectiunile=this.sRepository.getToateSectiuni(1);
                break;
            }
            case 2:{
                this.toateSectiunile=this.sRepository.getToateSectiuni(2);
                break;
            }
            case 3:{
                this.toateSectiunile=this.sRepository.getToateSectiuni(3);
                break;
            }
            case 4:{
                this.toateSectiunile=this.sRepository.getToateSectiuni(4);
                break;
            }
            default:{
            }
        }
        return this.toateSectiunile;
    }
    public LiveData<Map<Sectiune,List<Notita>>> getToateSectiunileCuNotite(){
        return toateSectiunileCuNotite;
    }

    public void insert(Sectiune sectiune){
        sRepository.insert(sectiune);
    }
}
