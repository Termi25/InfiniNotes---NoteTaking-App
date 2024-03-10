package com.ase.aplicatienotite.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class ActivitateAdaugareGenerala extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    List<Sectiune> listaSectiuni=new ArrayList<>();

    public static final String EXTRA_REPLY = "com.example.android.sectiunelistsql.REPLY";

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

        EditText etNumeSectiune = findViewById(R.id.etNumeAdaugaSectiune);

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

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode()==RESULT_OK){
                Intent data=result.getData();
                if(data!=null){
                    Sectiune sectiune=new Sectiune(data.getStringExtra(ActivitateAdaugareGenerala.EXTRA_REPLY),
                            null);
                    NotiteDB.databaseWriteExecutor.execute(()->{
                        NotiteDB db=NotiteDB.getInstance(getApplicationContext());
                        Sectiune misc=new Sectiune("MISC",null);
                        db.getSectiuneDao().insertSectiune(misc);
                    });
                }
            }else{
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        ImageButton btnAdaugaNotita=findViewById(R.id.btnAdaugaNotita);
        btnAdaugaNotita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivitateAdaugaNotita.class);
                launcher.launch(intent);
            }
        });
    }
}
