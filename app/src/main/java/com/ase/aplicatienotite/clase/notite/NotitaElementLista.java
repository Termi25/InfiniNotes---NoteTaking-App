package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;

@Entity(tableName = "elemente_liste")
public class NotitaElementLista extends Notita{
    private boolean checked;
    public NotitaElementLista( String titlu, String corp) {
        super(titlu, corp);
        checked=false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
