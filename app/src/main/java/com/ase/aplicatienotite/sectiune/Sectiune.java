package com.ase.aplicatienotite.sectiune;

import com.ase.aplicatienotite.notite.Notita;
import com.ase.aplicatienotite.notite.NotitaElementLista;
import com.ase.aplicatienotite.notite.NotitaLista;
import com.ase.aplicatienotite.notite.NotitaReminder;
import com.ase.aplicatienotite.notite.TipNotita;

import java.util.ArrayList;
import java.util.List;

public class Sectiune {
    private int id_sectiune;
    private String denumireSectiune;
    private String culoareSectiune;
    private int numarOrdine;
    private List<Notita>notite;
    private List<NotitaReminder>remindere;
    private List<NotitaLista>liste;

    public Sectiune(int id_sectiune, String denumireSectiune, String culoareSectiune, int numarOrdine,
                    List<Notita> notite) {
//        , List<NotitaReminder> remindere, List<NotitaLista> liste
        this.id_sectiune = id_sectiune;
        this.denumireSectiune = denumireSectiune;
        this.culoareSectiune = culoareSectiune;
        this.numarOrdine = numarOrdine;
        this.notite = notite;
//        this.remindere = remindere;
//        this.liste = liste;
    }

    public int getId_sectiune() {
        return id_sectiune;
    }

    public void setId_sectiune(int id_sectiune) {
        this.id_sectiune = id_sectiune;
    }

    public String getDenumireSectiune() {
        return denumireSectiune;
    }

    public void setDenumireSectiune(String denumireSectiune) {
        this.denumireSectiune = denumireSectiune;
    }

    public String getCuloareSectiune() {
        return culoareSectiune;
    }

    public void setCuloareSectiune(String culoareSectiune) {
        this.culoareSectiune = culoareSectiune;
    }

    public int getNumarOrdine() {
        return numarOrdine;
    }

    public void setNumarOrdine(int numarOrdine) {
        this.numarOrdine = numarOrdine;
    }

    public List<Notita> getNotite() {
        return notite;
    }

    public void setNotite(List<Notita> notite) {
        this.notite = notite;
    }

    public void addElementNotita(Notita notita){
        if(this.notite==null){
            this.notite=new ArrayList<>();
        }
        if(notita!=null){
            this.notite.add(notita);
        }
    }

    public void removeNotita(Notita notita){
        this.notite.remove(notita);
    }

//    public List<NotitaReminder> getRemindere() {
//        return remindere;
//    }
//
//    public void setRemindere(List<NotitaReminder> remindere) {
//        this.remindere = remindere;
//    }
//
//    public void addElementReminder(NotitaReminder reminder){
//        if(this.notite==null){
//            this.notite=new ArrayList<>();
//        }
//        if(reminder!=null){
//            this.notite.add(reminder);
//        }
//    }
//
//    public void removeReminder(NotitaReminder reminder){
//        this.remindere.remove(reminder);
//    }
//
//    public List<NotitaLista> getListe() {
//        return liste;
//    }
//
//    public void setListe(List<NotitaLista> liste) {
//        this.liste = liste;
//    }
//
//    public void addElementLista(NotitaLista lista){
//        if(this.notite==null){
//            this.notite=new ArrayList<>();
//        }
//        if(lista!=null){
//            this.notite.add(lista);
//        }
//    }
//
//    public void removeLista(NotitaLista lista){
//        this.notite.remove(lista);
//    }

    @Override
    public String toString() {
        String deReturnat=denumireSectiune+"\n\n";
        for(int i=0;i<notite.size();i++){
            if(notite.get(i).getTip()== TipNotita.REMINDER){
                NotitaReminder reminder=(NotitaReminder) notite.get(i);
                deReturnat+=reminder.toString()+' '+reminder.getDataReminder()+'\n';
            }else{
                deReturnat+=notite.get(i).toString()+'\n';
            }
        }
        return deReturnat;
    }
}
