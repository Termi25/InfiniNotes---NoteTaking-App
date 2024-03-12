package com.ase.aplicatienotite.extern.openweatherapi;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.main.ActivitatePrincipala;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ViewWeather extends View {
    private String fileName;

    public ViewWeather(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        try{
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ViewWeather);
            fileName = ta.getString(R.styleable.ViewWeather_setDataProvider);
            RequestQueue req= Volley.newRequestQueue(context);
            String url = null;
            try{
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(context.getAssets().open("chei.txt")));
                url=reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                    url,
                    null,
                    (Response.Listener<JSONObject>) response->{
                        String vreme;
                        try{
                            JSONObject current=response.getJSONObject("current");
                            JSONArray weather=current.getJSONArray("weather");
                            vreme=weather.getJSONObject(0).getString("main");
                            HorizontalScrollView hsv=new HorizontalScrollView(context);
                            LinearLayout linLayout=new LinearLayout(context);
                            linLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView tv=new TextView(context);
                            tv.setText(vreme);
                            linLayout.addView(tv);
                            hsv.addView(linLayout);
                            //creeaza un fisier layout care sa il setezi cu inflate aici => acces la campurile dorite

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    },
                    (Response.ErrorListener) error -> {
                        Toast.makeText(context, "Eroare! Nu se poate accesa prognoza meteo.", Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "incarcarePrognoza error: ${error.localizedMessage}");
                    } );
            req.add(jsonObjectRequest);
            ta.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
