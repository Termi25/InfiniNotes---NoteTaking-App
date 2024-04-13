package com.ase.aplicatienotite.baze_date.local.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ase.aplicatienotite.baze_date.local.repository.ListaNotiteJoinRepository;
import com.ase.aplicatienotite.clase.notite.ElementLista;

import java.util.List;

public class ListaNotiteJoinViewModel extends AndroidViewModel {

    private ListaNotiteJoinRepository sRepository;
    private Application application;
    private LiveData<List<ElementLista>> toateElementeleListei;

    public ListaNotiteJoinViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<List<ElementLista>> getToateElementeleListei(int idLista) {
        if(this.toateElementeleListei==null){
            sRepository=new ListaNotiteJoinRepository(application,idLista);
            this.toateElementeleListei=sRepository.getToateElementeleListei();
        }
        return this.toateElementeleListei;
    }

}
