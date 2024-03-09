package com.ase.aplicatienotite.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterSectiune;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuniViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ActivitatePrincipala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    private SectiuniViewModel sectiuneViewModel;
    private List<Sectiune>listaSectiuni=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rlv=findViewById(R.id.rlv_main);
        final AdapterSectiune adapter = new AdapterSectiune(new AdapterSectiune.SectiuneDiff());
        rlv.setAdapter(adapter);
        rlv.setLayoutManager(new LinearLayoutManager(this));

        sectiuneViewModel=new ViewModelProvider(this).get(SectiuniViewModel.class);
        sectiuneViewModel.getToateSectiuni().observe(this,sectiuni->{
            Notita a=new Notita("ceva","altceva");
            List<Notita>lista=new ArrayList<>();
            lista.add(a);
            for (final ListIterator<Sectiune> i = sectiuni.listIterator(); i.hasNext();) {
                final Sectiune element = i.next();
//                    element.setNotite(notitePerSectiuni.get(element));
                element.setNotite(lista);
                i.set(element);
            }
            List<Sectiune>listaSectiuni=new ArrayList<>();
            for(int i=0;i<sectiuni.size();i++){
                Sectiune noua=sectiuni.get(i);
                noua.setNotite(lista);
                listaSectiuni.add(noua);
            }
            adapter.submitList(listaSectiuni);
        });

        ImageButton btnAdauga=findViewById(R.id.btnAdaugareGenerala);
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Intent data=result.getData();
                if(data!=null){
                    Sectiune sectiune=new Sectiune(data.getStringExtra(ActivitateAdaugareGenerala.EXTRA_REPLY),
                            null);
                    sectiuneViewModel.insert(sectiune);
                }
            }else{
                Toast.makeText(getApplicationContext(), R.string.no_save_sectiunea,Toast.LENGTH_LONG).show();
            }
        });

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivitateAdaugareGenerala.class);
                launcher.launch(intent);
            }
        });
    }
}