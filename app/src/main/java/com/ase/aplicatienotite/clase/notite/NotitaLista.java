package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "liste",inheritSuperIndices = true)
public class NotitaLista extends Notita implements Serializable {
    @Ignore
    private List<ElementLista> elemente;

    public NotitaLista(String titlu,
                       String corp) {
        super(titlu, corp);
        elemente=new ArrayList<>();
    }
    public List<ElementLista> getElemente() {
        return elemente;
    }
    public void setElemente(List<ElementLista> elemente) {
        this.elemente.addAll(elemente);
    }

    public void addElement(ElementLista element){
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
