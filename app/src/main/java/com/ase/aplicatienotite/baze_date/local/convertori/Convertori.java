package com.ase.aplicatienotite.baze_date.local.convertori;

import androidx.room.TypeConverter;

import java.util.Date;

public class Convertori {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
