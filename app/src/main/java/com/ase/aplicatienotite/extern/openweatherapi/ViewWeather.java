package com.ase.aplicatienotite.extern.openweatherapi;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ase.aplicatienotite.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ViewWeather extends ConstraintLayout {
    private String fileName;

    public ViewWeather(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context,R.layout.view_vreme,this);
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
                        try{
                            JSONObject current=response.getJSONObject("current");
                            JSONArray weather=current.getJSONArray("weather");
                            String temp=current.getString("temp");
                            String icon=weather.getJSONObject(0).getString("icon");
                            incarcaImagine("https://openweathermap.org/img/wn/"+icon+"@2x.png",context,icon);
                            TextView oras=findViewById(R.id.tvOrasTemperatura);
                            TextView temperatura=findViewById(R.id.tvTemperatura);
                            oras.setText("București");
                            temperatura.setText(String.valueOf((int)Double.parseDouble(temp)));

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
        Button btnRefresh=findViewById(R.id.btnReincarcareVreme);
        btnRefresh.setOnClickListener(v -> postInvalidate());
    }

    private void incarcaImagine(String URL,Context context,String momentZi){
        ImageView imgV=findViewById(R.id.imgVVremeIcon);
        if(momentZi.charAt(2)=='n'){
            imgV.setBackgroundResource(R.drawable.button_skin_blue);
            Picasso.get().load(URL)
                    .fit()
                    .placeholder(R.drawable.warning_white)
                    .error(R.drawable.no_wifi_white)
                    .into(imgV);
        }else{
            imgV.setBackgroundResource(R.drawable.button_skin_light_blue);
            Picasso.get().load(URL)
                    .fit()
                    .placeholder(R.drawable.warning)
                    .error(R.drawable.no_wifi)
                    .into(imgV);
        }
    }

}
