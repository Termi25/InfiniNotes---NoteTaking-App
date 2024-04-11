package com.ase.aplicatienotite.clase.legaturi_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

@Entity(tableName = "notita_lista_join",
        primaryKeys = { "listaId", "notitaId" },
        foreignKeys = {
                @ForeignKey(entity = NotitaLista.class,
                        parentColumns = "notitaId",
                        childColumns = "listaId"),
                @ForeignKey(entity = ElementLista.class,
                        parentColumns = "notitaId",
                        childColumns = "notitaId")
        })
public class ListaNotiteJoin {
    public final int listaId;

    @ColumnInfo(index = true)
    public final int notitaId;

    public ListaNotiteJoin(int listaId, int notitaId) {
        this.listaId = listaId;
        this.notitaId = notitaId;
    }
}
