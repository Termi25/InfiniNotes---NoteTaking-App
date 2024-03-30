package com.ase.aplicatienotite.extern.openweatherapi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

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
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class ViewWeather extends ConstraintLayout {
    private String fileName;
    double longitude;
    double latitude;

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    };

    public ViewWeather(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_vreme, this);
        try {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ViewWeather);
            fileName = ta.getString(R.styleable.ViewWeather_setDataProvider);
            RequestQueue req = Volley.newRequestQueue(context);
            String url = null;

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            while (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            TextView oras = findViewById(R.id.tvOrasTemperatura);
            if (location != null) {
                this.longitude = location.getLongitude();
                this.latitude = location.getLatitude();
                url = "https://api.openweathermap.org/data/3.0/onecall?lat=" + this.latitude + "&lon=" +
                        this.longitude + "&exclude=hourly,daily,minutely,alerts&units=metric&appid=";
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(context.getAssets().open("cheie.txt")));
                    url = url + reader.readLine();
                } catch (IOException e) {
                    Log.e("Error", getContext().getString(R.string.error_view_weather_key_file_no_read));
                }

                Geocoder geocoder=new Geocoder(context, Locale.ENGLISH);
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    Address obj = addresses.get(0);
                    String localitate = obj.getLocality();
                    oras.setText(localitate);

                } catch (IOException e) {
                    Log.e("Error","eroare obtinere localitate pentru coordonatele furnizat in ViewWeather");
                }
            } else {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(context.getAssets().open("default.txt")));
                    url = reader.readLine();
                } catch (IOException e) {
                    Log.e("Error", getContext().getString(R.string.error_view_weather_key_file_no_read));
                }
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url,
                        null,
                        (Response.Listener<JSONObject>) response -> {
                            try {
                                JSONObject current = response.getJSONObject("current");
                                JSONArray weather = current.getJSONArray("weather");
                                String temp = current.getString("temp");
                                String icon = weather.getJSONObject(0).getString("icon");
                                incarcaImagine("https://openweathermap.org/img/wn/" + icon + "@2x.png", context, icon);
                                TextView temperatura = findViewById(R.id.tvTemperatura);
                                oras.setText("București");
                                temperatura.setText(String.valueOf((int) Double.parseDouble(temp)));
                            } catch (Exception e) {
                                Log.e("Error", getContext().getString(R.string.error_view_weather_json_read_fail));
                            }
                        },
                        (Response.ErrorListener) error -> {
                            Toasty.error(context, "Eroare! Nu se poate accesa prognoza meteo.", Toast.LENGTH_LONG).show();
                            Log.e("Error", getContext().getString(R.string.error_view_weather_no_access));
                        });
                req.add(jsonObjectRequest);
                ta.recycle();
        }catch(Exception e){
            Log.e("Error", getContext().getString(R.string.error_view_weather_general_fail));
        }
        Button btnRefresh = findViewById(R.id.btnReincarcareVreme);
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
