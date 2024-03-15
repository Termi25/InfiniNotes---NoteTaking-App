package com.ase.aplicatienotite.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ase.aplicatienotite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivitateVizualNotiteSectiune extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_vizual_notite);
        FloatingActionButton fab=findViewById(R.id.fActBtnInchidereVizual);
        fab.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}
