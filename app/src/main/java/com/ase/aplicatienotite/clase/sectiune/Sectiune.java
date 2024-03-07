package com.ase.aplicatienotite.clase.sectiune;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ase.aplicatienotite.clase.notite.Notita;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "sectiuni")
public class Sectiune {
    @PrimaryKey(autoGenerate = true)
    private int id_sectiune;
    private String denumireSectiune;
    private String culoareSectiune;
    @Ignore
    private List<Notita>notite;

    public Sectiune( String denumireSectiune, String culoareSectiune) {
        this.denumireSectiune = denumireSectiune;
        this.culoareSectiune = culoareSectiune;
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

    @Override
    public String toString() {
        StringBuilder deReturnat= new StringBuilder(denumireSectiune + "\n\n");
        for(int i=0;i<notite.size();i++){
            deReturnat.append(notite.get(i).toString()).append('\n');
        }
        return deReturnat.toString();
    }
}
