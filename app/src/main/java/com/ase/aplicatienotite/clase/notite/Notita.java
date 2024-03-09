package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notite",foreignKeys = {})
public class Notita implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int notitaId;
    private String titlu;
    private String corp;


    public Notita(String titlu, String corp) {
        this.titlu = titlu;
        this.corp = corp;
    }

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

    @Override
    public String toString() {
        return  titlu + "\n" + corp;
    }
}
