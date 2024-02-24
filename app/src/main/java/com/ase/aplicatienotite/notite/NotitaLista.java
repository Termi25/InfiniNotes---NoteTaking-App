package com.ase.aplicatienotite.notite;

import java.util.ArrayList;
import java.util.List;

public class NotitaLista extends Notita{
    private List<NotitaElementLista> elemente;

    public NotitaLista(int id_notita, int nrOrdine, String titlu,
                       String corp,TipNotita tip, List<NotitaElementLista> elemente) {
        super(id_notita, nrOrdine, titlu, corp,tip);
        this.elemente=elemente;
    }

    public List<NotitaElementLista> getElemente() {
        return elemente;
    }
    public void setElemente(List<NotitaElementLista> elemente) {
        this.elemente = elemente;
    }
    public void addElement(NotitaElementLista element){
        if(this.elemente==null){
            this.elemente=new ArrayList<>();
        }
        if(element!=null){
            this.elemente.add(element);
        }
    }

    public void remove(NotitaElementLista element){
        this.elemente.remove(element);
    }
}
