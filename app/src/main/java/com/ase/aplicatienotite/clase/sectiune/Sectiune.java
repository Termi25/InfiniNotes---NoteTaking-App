package com.ase.aplicatienotite.clase.sectiune;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "sectiuni",indices = {@Index(value = {"denumireSectiune"},
        unique = true)})
public class Sectiune implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int sectiuneId;
    private String denumireSectiune;
    private CuloriSectiune culoareSectiune;
    @Ignore
    private List<Notita>notite;
    @Ignore
    private List<NotitaLista> notiteListe;

    public Sectiune( String denumireSectiune, CuloriSectiune culoareSectiune) {
        this.denumireSectiune = denumireSectiune;
        this.culoareSectiune = culoareSectiune;
    }
    private Sectiune(){

    }

    public int getSectiuneId() {
        return sectiuneId;
    }

    public void setSectiuneId(int sectiuneId) {
        this.sectiuneId = sectiuneId;
    }

    public String getDenumireSectiune() {
        return denumireSectiune;
    }

    public void setDenumireSectiune(String denumireSectiune) {
        this.denumireSectiune = denumireSectiune;
    }

    public CuloriSectiune getCuloareSectiune() {
        return culoareSectiune;
    }

    public void setCuloareSectiune(CuloriSectiune culoareSectiune) {
        this.culoareSectiune = culoareSectiune;
    }

    public List<Notita> getNotite() {
        return notite;
    }

    public void setNotite(List<Notita> notite) {
        this.notite = new ArrayList<>();
        this.notite=notite;
    }

    public List<NotitaLista> getNotiteLista() {
        return notiteListe;
    }

    public void setNotiteLista(List<NotitaLista> notiteListe) {
        this.notiteListe = new ArrayList<>();
        this.notiteListe=notiteListe;
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

    @Override
    public String toString() {
        StringBuilder deReturnat= new StringBuilder(denumireSectiune + "\n\n");
        if(notite!=null){
            for(int i=0;i<notite.size();i++){
                deReturnat.append(notite.get(i).toString()).append('\n');
            }
        }
        return deReturnat.toString();
    }

}
