package com.ase.aplicatienotite.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.notite.Notita;
import com.ase.aplicatienotite.notite.NotitaReminder;
import com.ase.aplicatienotite.notite.TipNotita;
import com.ase.aplicatienotite.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitatePrincipala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    List<Sectiune> listaSectiuni=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv=findViewById(R.id.lv);

        Sectiune sectiuneNoua=new Sectiune(1,"MISC",null,1,null);
        Notita notita=new Notita(1,1,"Titlu","corp text", TipNotita.NOTITA);
        NotitaReminder reminder=new NotitaReminder(1,2,"TitluReminder","corpReminder",
                TipNotita.REMINDER,new Date());
        sectiuneNoua.addElementNotita(notita);
        sectiuneNoua.addElementNotita(reminder);
        sectiuneNoua.addElementNotita(notita);
        sectiuneNoua.addElementNotita(reminder);
        listaSectiuni.add(sectiuneNoua);
        listaSectiuni.add(sectiuneNoua);
        listaSectiuni.add(sectiuneNoua);
        listaSectiuni.add(sectiuneNoua);
        listaSectiuni.add(sectiuneNoua);
        listaSectiuni.add(sectiuneNoua);
        AdapterSectiune adapter=new AdapterSectiune(getApplicationContext(),
                R.layout.view_sectiune,listaSectiuni,getLayoutInflater());
        lv.setAdapter(adapter);

        ImageButton btnAdauga=findViewById(R.id.btnAdaugareGenerala);

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivitateAdaugareGenerala.class);
                startActivity(intent);
            }
        });
    }
}