package com.ase.aplicatienotite.main.activitati;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ase.aplicatienotite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivitateVizualListeSectiune extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vizual_liste);

        FloatingActionButton fActBtnInchidereVizualListe=findViewById(R.id.fActBtnInchidereVizualListe);
        fActBtnInchidereVizualListe.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
}
