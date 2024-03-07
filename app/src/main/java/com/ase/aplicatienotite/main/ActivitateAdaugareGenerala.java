package com.ase.aplicatienotite.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class ActivitateAdaugareGenerala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    List<Sectiune> listaSectiuni=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_adauga);
        ImageButton btnAnulare=findViewById(R.id.btnAnulareAdaugare);
        btnAnulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
