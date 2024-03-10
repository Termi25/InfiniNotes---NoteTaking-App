package com.ase.aplicatienotite.main;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ase.aplicatienotite.R;

import java.util.Calendar;

public class ActivitateAdaugaNotita extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_notita);

        ImageButton btnAnulare=findViewById(R.id.btnAnulareNotita);
        btnAnulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        Button btnReminderNotita = findViewById(R.id.btnReminderAdaugaNotita);
        btnReminderNotita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ActivitateAdaugaNotita.this,
                        (view, year1, monthOfYear, dayOfMonth) -> btnReminderNotita.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1),
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
