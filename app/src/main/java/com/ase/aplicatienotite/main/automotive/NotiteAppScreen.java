package com.ase.aplicatienotite.main.automotive;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.SectionedItemList;
import androidx.car.app.model.Template;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotiteAppScreen extends Screen {
    protected NotiteAppScreen(@NonNull CarContext carContext) {
        super(carContext);
    }
    @NonNull
    @Override
    public Template onGetTemplate() {
        ListTemplate.Builder templateBuilder=new ListTemplate.Builder();
        ItemList.Builder itemList=new ItemList.Builder();

        SimpleDateFormat dateFormat=new SimpleDateFormat(getCarContext().getString(R.string.pattern_data),
                Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",
                Locale.ENGLISH);
        Calendar calendar=Calendar.getInstance();

        try {
            for (Notita notita: NotiteDB.getInstance(getCarContext()).getNotitaDao().getToateNotiteleDupaDataReminder()) {
                if(notita.getDataReminder()!=null){
                    if(notita.getDataReminder().getTime()>calendar.getTimeInMillis()){
                        itemList.addItem(buildRow(notita.getTitlu()+" - "
                                +dateFormat.format(notita.getDataReminder())+" ora "
                                +timeFormat.format(notita.getDataReminder())));
                    }
                }
            }

            templateBuilder.addSectionedList(
                    SectionedItemList.create(itemList.build(), "Notite"));
        } catch (Exception e) {
            CarToast.makeText(getCarContext(), "There are none.", CarToast.LENGTH_SHORT).show();
        }

        return templateBuilder.setHeaderAction(Action.APP_ICON)
                .setSingleList(itemList.build())
                .setTitle("InfiniNotes - Upcoming reminders - "+dateFormat.format(calendar.getTime()))
                .build();
    }
    @NonNull
    private Row buildRow(String data) {
        return new Row.Builder()
                .setTitle(data)
                .setOnClickListener(this::invalidate)
                .build();
    }

}
