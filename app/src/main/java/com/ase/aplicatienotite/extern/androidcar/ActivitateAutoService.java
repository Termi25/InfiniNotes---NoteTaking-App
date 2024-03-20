package com.ase.aplicatienotite.extern.androidcar;

import androidx.annotation.NonNull;
import androidx.car.app.CarAppService;
import androidx.car.app.Session;
import androidx.car.app.validation.HostValidator;

public class ActivitateAutoService extends CarAppService {
    @NonNull
    @Override
    public HostValidator createHostValidator() {
        return new HostValidator.Builder(getApplicationContext())
                .addAllowedHosts(androidx.car.app.R.array.hosts_allowlist_sample)
                .build();
    }

    @NonNull
    @Override
    public Session onCreateSession() {
        return new ActivitateAutoSession();
    }
}
