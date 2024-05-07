package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "remindere")
public class NotitaReminder extends Notita{
    private Date dataReminder;
    public NotitaReminder(String titlu, String corp,Date dataReminder) {
        super(titlu, corp);
        this.dataReminder=dataReminder;
    }
    public Date getDataReminder() {
        return dataReminder;
    }

    public void setDataReminder(Date dataReminder) {
        this.dataReminder = dataReminder;
    }
}
