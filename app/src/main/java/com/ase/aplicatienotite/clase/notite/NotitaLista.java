package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "liste",inheritSuperIndices = true)
public class NotitaLista extends Notita{
    @Ignore
    private List<Notita> elemente;

    public NotitaLista(String titlu,
                       String corp) {
        super(titlu, corp);
        elemente=new ArrayList<>();
    }
    public List<Notita> getElemente() {
        return elemente;
    }
    public void setElemente(List<Notita> elemente) {
        this.elemente.addAll(elemente);
    }

    public void addElement(Notita element){
        if(this.elemente==null){
            this.elemente=new ArrayList<>();
        }
        if(element!=null){
            this.elemente.add(element);
        }
    }

    public void remove(Notita element){
        this.elemente.remove(element);
    }
}
