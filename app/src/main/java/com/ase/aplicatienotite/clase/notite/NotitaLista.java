package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "liste")
public class NotitaLista extends Notita{
    @Ignore
    private List<NotitaElementLista> elemente;

    public NotitaLista(String titlu,
                       String corp) {
        super(titlu, corp);
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
