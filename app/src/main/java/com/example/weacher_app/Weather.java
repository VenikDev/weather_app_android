package com.example.weacher_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Weather {
    // протокол
    private final String PROTOCOL   = "https://";

    // хост сервиса
    private final String HOST       = "api.openweathermap.org";
    private final String HOST_IMAGE = "openweathermap.org";

    // путь запроса
    private final String
            REQUEST_PATH_CURRENT    = "/data/2.5/weather?";

    // -- ОБВЯЗКА --

    // родитель
    private Context mParent;
    // город
    private String mCity;
    // Ключ приложения
    private String mApiKey;
    // Язык
    private String mLang;
    // Очередь запросов
    private RequestQueue mRequestQueue;
    // Время обнавления
    private int mUpdateTime = 0;
    // таймер обновления
    private Timer mTimer = null;
    // делагат погоды
    private WeatherDelegate mDelegate = null;

    // -- ДАННЫЕ ПОГОДЫ --

    // текузая температура
    int currentTemperature = 0;
    //Иконка погоды
    String icon;
    // Ощущается как
    int feelingTemperature = 0;

    int maxTemperature = 0;
    int mimTemperature = 0;

    // Давдение
    int pressure = 0;
    // Влажность
    int humidity = 0;
    // Описание
    String description;
    // видимость
    int visibility = 0;
    // скорость ветра
    int windSpeed = 0;
    // Направленияе ветра
    int windDirection = 0;
    // облоко
    int clouds = 0;
    // Закат
    long sunSet = 0;
    // Рассвет
    long sunRise = 0;

    Weather(Context context, String city, String apiKey, String lang, int updateTime){
        mParent = context;
        mCity = city;
        mApiKey = apiKey;
        mLang = lang;

        if(updateTime < 50000)
            updateTime = 50000;
        else
            mUpdateTime = updateTime;

        init();
    }

    private void init() {
        // Создаем очередь запросов
        mRequestQueue = Volley.newRequestQueue(mParent);

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                update();
            }
        }, 0, mUpdateTime);
    }

    private void update() {
        // Создание запроса
        String url = PROTOCOL + HOST + REQUEST_PATH_CURRENT
                + "q=" + mCity + "&appid=" + mApiKey + "&lang=" + mLang;

        send(url);
    }

    private void send(String url) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                parse(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
                onError();
            }
        });

        mRequestQueue.add(jsonRequest);
    }

}
