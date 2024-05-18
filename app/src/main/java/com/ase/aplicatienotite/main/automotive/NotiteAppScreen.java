package com.ase.aplicatienotite.main.automotive;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.GridItem;
import androidx.car.app.model.GridTemplate;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.OnClickListener;
import androidx.car.app.model.Row;
import androidx.car.app.model.SectionedItemList;
import androidx.car.app.model.Template;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.clase.sectiune.culori.CuloriSectiune;

import java.text.SimpleDateFormat;
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

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat(getCarContext().getString(R.string.pattern_data),
                    Locale.ENGLISH);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",
                    Locale.ENGLISH);

            for (Notita notita: NotiteDB.getInstance(getCarContext()).getNotitaDao().getToateNotitele()) {
                if(notita.getDataReminder()!=null){
                    itemList.addItem(buildRow(notita.getTitlu()+" - reminder: "
                            +dateFormat.format(notita.getDataReminder())+" la ora "
                            +timeFormat.format(notita.getDataReminder())));
                }else{
                    itemList.addItem(buildRow(notita.getTitlu()));
                }
                CarToast.makeText(getCarContext(), notita.toString(), CarToast.LENGTH_SHORT).show();
            }

            templateBuilder.addSectionedList(
                    SectionedItemList.create(itemList.build(), "Notite"));
        } catch (Exception e) {
            CarToast.makeText(getCarContext(), "No more", CarToast.LENGTH_SHORT).show();
        }

        return templateBuilder.setHeaderAction(Action.APP_ICON)
                .setSingleList(itemList.build())
                .setTitle("InfiniNotes")
                .build();
    }

    @NonNull
    private Row buildRow(String data) {
        return new Row.Builder()
                .setTitle(data)
                .build();
    }

}
