package com.ase.aplicatienotite.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public static final String EXTRA_REPLY = "com.example.android.sectiunelistsql.REPLY";

    private EditText etNumeSectiune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_adauga);
        ImageButton btnAnulare=findViewById(R.id.btnAnulareAdaugare);
        btnAnulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        etNumeSectiune = findViewById(R.id.etNumeAdaugaSectiune);

        final ImageButton button = findViewById(R.id.btnAdaugaSectiune);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(etNumeSectiune.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = etNumeSectiune.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, word);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
