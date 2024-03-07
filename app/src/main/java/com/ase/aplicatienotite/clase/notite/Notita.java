package com.ase.aplicatienotite.clase.notite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notite")
public class Notita implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id_notita;
    private String titlu;
    private String corp;

    public Notita(String titlu, String corp) {
        this.titlu = titlu;
        this.corp = corp;
    }

    public int getId_notita() {
        return id_notita;
    }

    public void setId_notita(int id_notita) {
        this.id_notita = id_notita;
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
