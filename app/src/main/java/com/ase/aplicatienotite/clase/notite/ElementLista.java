package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;

@Entity(tableName = "elemente_liste",inheritSuperIndices = true)
public class ElementLista extends Notita{
    public ElementLista(String titlu, String corp) {
        super(titlu, corp);
    }
}
