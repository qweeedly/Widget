package com.example.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import static com.example.widget.ConnectFetch.getIconUrl;
import static com.example.widget.StaticWeatherAnalyze.getCityField;
import static com.example.widget.StaticWeatherAnalyze.getDetailsField;
import static com.example.widget.StaticWeatherAnalyze.getLastUpdateTime;
import static com.example.widget.StaticWeatherAnalyze.getTemperatureField;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        updateWeatherData("Moscow");
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = ConnectFetch.getJSON(MainActivity.this, city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(MainActivity.this,
                                    city + "-информация не найдена",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json){
        try {


//            //            Ответы:
////                      json.getString("name") - название города
////                      json.getJSONObject("sys").getString("country") - название страны
////                      JSONObject details =  json.getJSONArray("weather").getJSONObject(0) - первый элемент массива метеорологических данных.
////                           details.getInt("id")  - идентификатор погоды\
////                           details.getString("description") - краткое описание погоды
//
////                      JSONObject main = json.getJSONObject("main"); - узел main
////                          main.getString("humidity")  - влажность
////                          main.getString("pressure")  - давление
////                            main.getDouble("temp")    - температура
//
////                      DateFormat df = DateFormat.getDateTimeInstance();
////                      String updatedOn = df.format(new Date(json.getLong("dt")*1000)); - время получения информации системой
//
////                      json.getJSONObject("sys").getLong("sunrise") - время восхода
////                      json.getJSONObject("sys").getLong("sunset") - время заката

            Glide
                    .with(this)
                    .load(getIconUrl(json))
                    .into((ImageView)findViewById(R.id.weather_icon));
            ((TextView)findViewById(R.id.city_field)).setText(getCityField(json));
            ((TextView)findViewById(R.id.updated_field)).setText(getLastUpdateTime(json));
            ((TextView)findViewById(R.id.details_field)).setText(getDetailsField(json));
            ((TextView)findViewById(R.id.current_temperature_field)).setText(getTemperatureField(json));

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
    }