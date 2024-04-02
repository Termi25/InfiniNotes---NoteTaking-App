package com.ase.aplicatienotite.clase.legaturi_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

@Entity(tableName = "sectiune_notita_lista_join",
        primaryKeys = { "notitaId", "sectiuneId" },
        foreignKeys = {
                @ForeignKey(entity = NotitaLista.class,
                        parentColumns = "notitaId",
                        childColumns = "notitaId"),
                @ForeignKey(entity = Sectiune.class,
                        parentColumns = "sectiuneId",
                        childColumns = "sectiuneId")
        })
public class SectiuneNotiteListaJoin {
    public final int notitaId;
    @ColumnInfo(index = true)
    public final int sectiuneId;

    public SectiuneNotiteListaJoin(final int notitaId, final int sectiuneId) {
        this.notitaId = notitaId;
        this.sectiuneId = sectiuneId;
    }
}
