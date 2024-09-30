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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class ViewWeather extends ConstraintLayout {
    double longitude;
    double latitude;

    public ViewWeather(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_vreme, this);
        incarcareVreme(context,attrs);
    }

    private void incarcareVreme(Context context,AttributeSet attrs){
        setareDataVreme();
        boolean preferences = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Conexiune la Internet",true);
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        LocationListener locationListener = location -> {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        TextView oras = findViewById(R.id.tvOrasTemperatura);
        if(preferences){
            try {
                TextView tvUnitateMasuraVreme=findViewById(R.id.tvUnitateMasuraTemperatura);
                tvUnitateMasuraVreme.setText("°C");
                TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ViewWeather);
                RequestQueue req = Volley.newRequestQueue(context);

                String url = null;
                if (location != null) {
                    url=obtinereLocatie(location, context,oras);
                } else {
                    url=setarePrognozaBackup(context, url, oras);
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url,
                        null,
                        (Response.Listener<JSONObject>) response -> setarePrognozaAPI(response,attrs),
                        error -> {
                            Toasty.error(context, "Eroare! Nu se poate accesa prognoza meteo.", Toast.LENGTH_LONG).show();
                            Log.e("Error", getContext().getString(R.string.error_view_weather_no_access));
                        });
                req.add(jsonObjectRequest);
                ta.recycle();

            }catch(Exception e){
                Log.e("Error", getContext().getString(R.string.error_view_weather_general_fail));
            }
        }else{
            if(location!=null){
                setareLocatie(location,context,oras);
            }
        }
    }

    private String obtinereLocatie(Location location, Context context, TextView oras) {
        setareLocatie(location,context,oras);
        String url = "https://api.openweathermap.org/data/3.0/onecall?lat=" + this.latitude + "&lon=" +
                this.longitude + "&exclude=hourly,daily,minutely,alerts&units=metric&appid=";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("cheie.txt")));
            url = url + reader.readLine();
        } catch (IOException e) {
            Log.e("Error", getContext().getString(R.string.error_view_weather_key_file_no_read));
        }
        return url;
    }

    private void setareLocatie(Location location, Context context, TextView oras){
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        Geocoder geocoder=new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            String localitate = obj.getLocality();
            oras.setText(localitate);

        } catch (IOException e) {
            Log.e("Error","Eroare obtinere localitate pentru coordonatele furnizat in ViewWeather");
        }
    }

    private String setarePrognozaBackup(Context context, String url,TextView oras) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("default.txt")));
            url = reader.readLine();
            oras.setText(R.string.bucuresti);
        } catch (IOException e) {
            Log.e("Error", getContext().getString(R.string.error_view_weather_key_file_no_read));
        }
        return url;
    }

    private void setarePrognozaAPI(JSONObject response, AttributeSet attrs) {
        try {
            JSONObject current = response.getJSONObject("current");
            JSONArray weather = current.getJSONArray("weather");
            String temp = current.getString("temp");
            String icon = weather.getJSONObject(0).getString("icon");
            incarcaImagine("https://openweathermap.org/img/wn/" + icon + "@2x.png", icon,attrs);
            TextView temperatura = findViewById(R.id.tvTemperatura);

            temperatura.setText(String.valueOf((int) Double.parseDouble(temp)));
        } catch (Exception e) {
            Log.e("Error", getContext().getString(R.string.error_view_weather_json_read_fail));
        }
    }

    private void setareDataVreme() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataFormata = df.format(c);
        TextView tvDataCurenta=findViewById(R.id.tvDataCurentaVreme);
        tvDataCurenta.setText(dataFormata);
    }

    private void incarcaImagine(String URL, String momentZi,AttributeSet attrs){
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
        imgV.setOnClickListener(v->{
            invalidate();
            incarcareVreme(getContext(),attrs);
            Toasty.info(getContext(),"Prognoză reîncărcată").show();
        });
    }

}
