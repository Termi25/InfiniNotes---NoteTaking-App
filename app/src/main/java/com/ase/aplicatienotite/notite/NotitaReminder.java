package com.ase.aplicatienotite.notite;

import java.util.Date;

public class NotitaReminder extends Notita{
    private Date dataReminder;
    public NotitaReminder(int id_notita, int nrOrdine, String titlu, String corp,TipNotita tip,Date dataReminder) {
        super(id_notita, nrOrdine, titlu, corp,tip);
        this.dataReminder=dataReminder;
    }

    public Date getDataReminder() {
        return dataReminder;
    }

    public void setDataReminder(Date dataReminder) {
        this.dataReminder = dataReminder;
    }
}
