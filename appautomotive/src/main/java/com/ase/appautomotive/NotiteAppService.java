package com.ase.appautomotive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.car.app.CarAppService;
import androidx.car.app.Screen;
import androidx.car.app.Session;
import androidx.car.app.validation.HostValidator;

public class NotiteAppService extends CarAppService {
    @NonNull
    @Override
    public HostValidator createHostValidator() {
        return null;
    }

    @NonNull
    @Override
    public Session onCreateSession() {
        return new NotiteAppSession();
    }
}
