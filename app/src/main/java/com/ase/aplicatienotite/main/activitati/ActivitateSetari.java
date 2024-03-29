package com.ase.aplicatienotite.main.activitati;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        ImageButton imgBtnSalavareSetari=findViewById(R.id.imgBtnSalvareSetari);
        imgBtnSalavareSetari.setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }


}
