package com.ase.aplicatienotite.clase.legaturi_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

@Entity(tableName = "sectiune_notita_join",
        primaryKeys = { "notitaId", "sectiuneId" },
        foreignKeys = {
                @ForeignKey(entity = Notita.class,
                        parentColumns = "notitaId",
                        childColumns = "notitaId"),
                @ForeignKey(entity = Sectiune.class,
                        parentColumns = "sectiuneId",
                        childColumns = "sectiuneId")
        })
public class SectiuneNotiteJoin {
    public final int notitaId;
    @ColumnInfo(index = true)
    public final int sectiuneId;

    public SectiuneNotiteJoin(final int notitaId, final int sectiuneId) {
        this.notitaId = notitaId;
        this.sectiuneId = sectiuneId;
    }
}
