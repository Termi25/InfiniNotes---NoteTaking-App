package com.ase.aplicatienotite.clase.notite;

import java.util.Date;

public class FactoryNotite {
    public Notita creareNotite(TipuriNotite tip, String titlu, String corp, Date dataReminder){
        switch (tip){
            case NotitaSimpla:return new Notita(titlu,corp);
            case NotitaReminder:return new NotitaReminder(titlu,corp,dataReminder);
            case NotitaLista:return new NotitaLista(titlu,corp);
            case NotitaElementLista:return new NotitaElementLista(titlu,corp);
            default:return null;
        }
    }
}
