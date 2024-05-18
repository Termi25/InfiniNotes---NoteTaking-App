package com.ase.appautomotive;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;

public class NotiteAppScreen extends Screen {
    public NotiteAppScreen(@NonNull CarContext carContext) {
        super(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        Row row = new Row.Builder().setTitle("Hello AndroidX!").build();
        return new PaneTemplate.Builder(new Pane.Builder().addRow(row).build())
                .setHeaderAction(Action.APP_ICON)
                .build();
    }
}
