package com.ase.aplicatienotite.baze_date.local.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ase.aplicatienotite.baze_date.local.repository.NotiteListeRecenteRepository;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

public class NotiteRecenteViewModel extends AndroidViewModel {

    private NotiteListeRecenteRepository nRepository;
    private Application application;
    private Notita notitaRecenta;
    private NotitaLista listaRecenta;

    public NotiteRecenteViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public Notita getNotitaAccesataRecent() {
        nRepository=new NotiteListeRecenteRepository(application);
        notitaRecenta =nRepository.getNotitaAccesataRecent();
        return notitaRecenta;
    }

    public NotitaLista getListaAccesataRecent() {
        nRepository=new NotiteListeRecenteRepository(application);
        listaRecenta =nRepository.getListaAccesataRecent();
        return listaRecenta;
    }
}
