package com.example.openweatherd05k14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCity;
    private TextView tvTemperature, tvDescription;
    private ImageView imgWeather;
    private static String API_KEY = "928133397391e6af373468b74849e7ab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCity = findViewById(R.id.edtCity);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDescription = findViewById(R.id.tvDescription);
        imgWeather = findViewById(R.id.imgWeather);
    }

    private String buildURL(String city) {
        return String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi", city, API_KEY);
    }

    private String buildIconUrl(String icon) {
        return String.format("https://openweathermap.org/img/wn/%s@4x.png", icon);
    }

    public void getWeatherByCityName(View view) {
        // Buoc 1: Lay du lieu tu EditText va kiem tra
        String city = edtCity.getText().toString();
        if (city.isEmpty()) {
            edtCity.setError("Enter your city!");
            return;
        }
        // Buoc 2: Tao url
        String url = buildURL(city);

        // Buoc 3: Tao request openweather bang volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Lay du lieu thanh cong
                        try {
                            // Nhiet do
                            double temp = response.getJSONObject("main").getDouble("temp");
                            tvTemperature.setText(temp + "°C");
                            // Do am
                            double humidity = response.getJSONObject("main").getDouble("humidity");
//                            Toast.makeText(MainActivity.this, ""+humidity, Toast.LENGTH_SHORT).show();

                            // Mo ta
                            String description = response.getJSONArray("weather").getJSONObject(0).getString("description");
                            tvDescription.setText(description);
                            // Lay icon
                            String icon = response.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String iconUrl = buildIconUrl(icon);

                            Glide.with(MainActivity.this).load(iconUrl).into(imgWeather);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);


        // Buoc 4: Xem ket qua tra ve -> boc tach ket qua -> hien thi len view


    }
}