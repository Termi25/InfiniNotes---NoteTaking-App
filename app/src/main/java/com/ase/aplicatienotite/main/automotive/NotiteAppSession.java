package com.ase.aplicatienotite.main.automotive;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.car.app.Screen;
import androidx.car.app.Session;

public class NotiteAppSession extends Session {
    @NonNull
    @Override
    public Screen onCreateScreen(@NonNull Intent intent) {
        return new NotiteAppScreen(getCarContext());
    }
}
