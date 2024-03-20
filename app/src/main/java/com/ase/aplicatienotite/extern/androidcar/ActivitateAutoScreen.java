package com.ase.aplicatienotite.extern.androidcar;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;

public class ActivitateAutoScreen extends Screen {

    private String rowTitle="TEST";
    protected ActivitateAutoScreen(@NonNull CarContext carContext) {
        super(carContext);
    }
    @NonNull
    @Override
    public Template onGetTemplate() {
        Row row=new Row.Builder()
                .setTitle("Hello World!")
                .setOnClickListener(()->{
                    rowTitle="How are you doing?";
                    invalidate();
                })
                .build();
        ItemList lista=new ItemList.Builder().addItem(row).build();
        return new ListTemplate.Builder()
                .setSingleList(lista)
                .setTitle("Testare")
                .build();
    }
}
