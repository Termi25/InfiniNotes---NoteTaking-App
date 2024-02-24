package com.ase.aplicatienotite.notite;

public class NotitaElementLista extends Notita{
    private boolean checked;
    public NotitaElementLista(int id_notita, int nrOrdine, String titlu, String corp,TipNotita tip) {
        super(id_notita, nrOrdine, titlu, corp,tip);
        checked=false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
