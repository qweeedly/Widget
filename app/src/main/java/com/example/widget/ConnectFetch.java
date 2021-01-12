package com.example.widget;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectFetch {
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    private static final String OPEN_WEATHER_ICON =
            "http://openweathermap.org/img/wn/%s@2x.png";


    public static String getIconUrl(JSONObject json)
    {
        try {

//          первый элемент массива метеорологических данных.
            JSONObject details =  json.getJSONArray("weather").getJSONObject(0) ;
            String icon = details.getString("icon");
            return String.format(OPEN_WEATHER_ICON, icon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject getJSON(Context context, String city){
        try {
            String urlString = String.format(OPEN_WEATHER_MAP_API, city,context.getString(R.string.weather_api_key));
            URL url = new URL(urlString);

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.weather_api_key));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();


            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}

