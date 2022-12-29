package com.demo.android.googlemapapidemo1256;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class WeatherActivity extends AppCompatActivity {

    TextView tvDate, tvTime, tvTemp, tvMax, tvMin, tvWindDer, tvWindSpeed, tvStatus;
    ImageView ivWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvTemp = findViewById(R.id.tvTemp);
        ivWeather = findViewById(R.id.ivWeather);
        tvMax = findViewById(R.id.tvMaxTemp);
        tvMin = findViewById(R.id.tvMinTemp);
        tvWindDer = findViewById(R.id.tvWindDirect);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvStatus = findViewById(R.id.tvStatus);

        Bundle b = getIntent().getExtras();
        double lat = b.getDouble("lat");
        double lng = b.getDouble("lng");
        new MyAsyncTask().execute();

    }



    // 建立方法：連結api，取得json格式的天氣資料
    private String fetchWeatherData() {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Taipei,tw&appid=1743e058adeff422909fb620fbc9feaa");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            byte[] cache = new byte[1024];
            is.read(cache);
//            JSONObject jsonObject = new JSONObject(new String(cache));
            return new String(cache);
        } catch (IOException e) {
            e.printStackTrace();
            return "failed!!";
        } catch (Exception e){
            return e.getMessage();
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
//            return datasource.fetchWeatherData();
            return fetchWeatherData();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String result) { // result 為 doInBackground return 回來的東西
            super.onPostExecute(result);
            Log.v("jena", result);

            try {
                JSONObject jObj = new JSONObject(result);

                // 取得當前時間 (格式化)
                long current = System.currentTimeMillis();
                String curDate = DateFormat.format("EEE. yyyy/MM/dd", current).toString();
                String curTime = DateFormat.format("hh:mm", current).toString();
                Log.v("jena", "curDate =" + curDate + ", curTime =" + curTime);

                // 取得位置資訊
                JSONObject jObjCoord = jObj.getJSONObject("coord");
                Double lon = jObjCoord.getDouble("lon");
                Double lat = jObjCoord.getDouble("lat");
                Log.v("jena", "lon=" + lon + ", lat=" + lat);

                // 取得天氣資訊
                JSONObject jObjWeather = jObj.getJSONArray("weather").getJSONObject(0);
                String mainWeather = jObjWeather.getString("main");
                String descWeather = jObjWeather.getString("description");
                Log.v("jena", "mainWeather=" + mainWeather + ", descWeather=" + descWeather);

                // 取得溫度資訊
                JSONObject jObjMain = jObj.getJSONObject("main");
                Double temp = jObjMain.getDouble("temp")-273.15;
                Double minTemp = jObjMain.getDouble("temp_min")-273.15;
                Double maxTemp = jObjMain.getDouble("temp_max")-273.15;
                Log.v("jena", "temp=" + temp + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp);

                // 取得風速風向資訊
                JSONObject jObjWind = jObj.getJSONObject("wind");
                Double windSpeed = jObjWind.getDouble("speed");
                int windDeg = jObjWind.getInt("deg");
                Log.v("jena", "windSpeed=" + windSpeed + ", windDeg=" + windDeg);

                tvDate.setText(curDate);
                tvTime.setText(curTime);
                tvMax.setText(String.format("Max: %2.0f℃",maxTemp));
                tvMin.setText(String.format("Min: %2.0f℃",minTemp));
                tvTemp.setText(String.format("%2.0f°",temp));
                tvWindSpeed.setText(String.format("Wind Speed: %2.0fkm/h",windSpeed));
                tvStatus.setText(descWeather);
                switch (mainWeather){
                    case "Clouds" :
                        ivWeather.setImageResource(R.drawable.partly_cloudy_day);
                        break;
                    case "Rain":
                        ivWeather.setImageResource(R.drawable.rain_day);
                        break;
                    case "Clear":
                        ivWeather.setImageResource(R.drawable.clear_day);
                        break;
                }
                if(windDeg>=45&&windDeg<135){
                    tvWindDer.setText("Wind Direct: East");
                }else if (windDeg>=135&&windDeg<225){
                    tvWindDer.setText("Wind Direct: South");
                }else if (windDeg>=225&&windDeg<315){
                    tvWindDer.setText("Wind Direct: West");
                }else{
                    tvWindDer.setText("Wind Direct: North");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}