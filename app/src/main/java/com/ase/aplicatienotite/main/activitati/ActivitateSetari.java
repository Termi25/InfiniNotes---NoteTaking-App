package com.ase.aplicatienotite.main.activitati;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.main.fragmente.FragmentSetari;

public class ActivitateSetari extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_setari);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, new FragmentSetari())
                .commit();

        ImageButton imgBtnShareDB=findViewById(R.id.imgBtnShareDB);
        imgBtnShareDB.setOnClickListener(v->{

        });

        ImageButton imgBtnSalavareSetari=findViewById(R.id.imgBtnSalvareSetari);
        imgBtnSalavareSetari.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }

    private static void exportDatabase(Context context,String filename){

    }
}
