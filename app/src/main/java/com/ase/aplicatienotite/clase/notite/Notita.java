package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "notite",indices = {@Index(value = {"titlu"},
        unique = true)})
public class Notita implements Serializable {
    @PrimaryKey(autoGenerate = true)
    protected int notitaId;
    protected String titlu;
    protected String corp;
    protected Date dataReminder;
    protected boolean checked;
    protected Date dataAccesare;

    public Notita(String titlu, String corp) {
        this.titlu = titlu;
        this.corp = corp;
        this.dataAccesare=new Date();
    }

    private Notita(){}

    public int getNotitaId() {
        return notitaId;
    }

    public void setNotitaId(int notitaId) {
        this.notitaId = notitaId;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public Date getDataReminder() {
        return dataReminder;
    }

    public void setDataReminder(Date dataReminder) {
        this.dataReminder = dataReminder;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Date getDataAccesare() {
        return dataAccesare;
    }

    public void setDataAccesare(Date dataAccesare) {
        this.dataAccesare = dataAccesare;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd / MM / yyyy",
                Locale.ENGLISH);
        if(dataReminder!=null){
            return  titlu + "\n" + corp + "\n" + simpleDateFormat.format(dataReminder);
        }
        return  titlu + "\n" + corp;
    }

}
