package com.ase.aplicatienotite.notite;

import java.io.Serializable;

public class Notita implements Serializable {
    private int id_notita;
    private int nrOrdine;
    private String titlu;
    private String corp;
    private TipNotita tip;

    public Notita(int id_notita, int nrOrdine, String titlu, String corp, TipNotita tip) {
        this.id_notita = id_notita;
        this.nrOrdine = nrOrdine;
        this.titlu = titlu;
        this.corp = corp;
        this.tip = tip;
    }

    public int getId_notita() {
        return id_notita;
    }

    public void setId_notita(int id_notita) {
        this.id_notita = id_notita;
    }

    public int getNrOrdine() {
        return nrOrdine;
    }

    public void setNrOrdine(int nrOrdine) {
        this.nrOrdine = nrOrdine;
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

    public TipNotita getTip() {
        return tip;
    }

    public void setTip(TipNotita tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return  titlu + "\n" + corp;
    }
}
