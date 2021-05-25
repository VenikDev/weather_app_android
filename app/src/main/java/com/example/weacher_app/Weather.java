package com.example.weacher_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Weather {
    // протокол
    private final String PROTOCOL = "https://";

    // хост сервиса
    private final String HOST = "api.openweathermap.org";
    private final String HOST_IMAGE = "openweathermap.org";

    // путь запроса
    private final String
            REQUEST_PATH_CURRENT = "/data/2.5/weather?";

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
    // Список делегатов
    ArrayList<WeatherDelegate> mDelegates;

    // -- ДАННЫЕ ПОГОДЫ --

    // текузая температура
    int currentTemperature = 0;
    //Иконка погоды
    String icon;
    // Ощущается как
    int feelingTemperature = 0;

    int maxTemperature = 0;
    int minTemperature = 0;

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
    String windDirection;
    // облоко
    int clouds = 0;
    // Закат
    long sunSet = 0;
    // Рассвет
    long sunRise = 0;

    int direction = 0;

    private String prepareWeatherIconURL(String icon) {
        return PROTOCOL + HOST_IMAGE + "/img/wn/" + icon + "@4x.png";
    }

    private int convertKelvinToCel(double value) {
        return (int) Math.round(value - 273.15);
    }

    Weather(Context context, String city, String apiKey, String lang, int updateTime) {
        mParent = context;
        mCity = city;
        mApiKey = apiKey;
        mLang = lang;

        if (updateTime < 50000) {
            updateTime = 50000;
            mUpdateTime = updateTime;
        } else
            mUpdateTime = updateTime;

        init();
    }

    private void init() {
        mDelegates = new ArrayList<WeatherDelegate>();
        // Создаем очередь запросов
        mRequestQueue = Volley.newRequestQueue(mParent);

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, mUpdateTime);
    }

    public void update() {
        // Создание запроса
        String url = PROTOCOL + HOST + REQUEST_PATH_CURRENT
                + "q=" + mCity + "&appid=" + mApiKey + "&lang=" + mLang;
//        String url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a&lang=en-US";

        send(url);
    }

    private void send(String url) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                onError();
            }
        });

        mRequestQueue.add(jsonRequest);
    }

    private void parse(JSONObject response) {
        try {

            // Парсии иконку
            JSONArray weather = response.getJSONArray("weather");
            JSONObject current = weather.getJSONObject(0);
            icon = prepareWeatherIconURL(current.getString("icon"));

            // Данные по температуре
            JSONObject main = response.getJSONObject("main");
            currentTemperature = convertKelvinToCel(main.getDouble("temp"));
            feelingTemperature = convertKelvinToCel(main.getDouble("feels_like"));
            maxTemperature = convertKelvinToCel(main.getDouble("temp_min"));
            minTemperature = convertKelvinToCel(main.getDouble("temp_max"));

            // Данные о влажности
            visibility = response.getInt("visibility");

            // Данные о ветре
            JSONObject windData = response.getJSONObject("wind");
            windSpeed = windData.getInt("speed");
            direction = windData.getInt("deg");
            windDirection = convertingWindDirection(direction);

//            pressure = main.getInt("pressure");
//            humidity = main.getInt("humidity");
//
//            // Данные о влажности
//            JSONObject visibilityData = new JSONObject(response);
//            visibility = visibilityData.getInt("visibility");
//
            // данные об облаке
            JSONObject cloudsData = response.getJSONObject("clouds");
            clouds = cloudsData.getInt("all");
//
//            // данные о рассвете и закате
//            JSONObject sunRiseAndSet = response.getJSONObject("sys");
//            sunSet  = windData.getLong("sunset");
//            sunRise = windData.getLong("sunrise");

        } catch (JSONException e) {
            e.printStackTrace();
            onError();
        }
    }

    private String convertingWindDirection(int windDeg) {
        if (windDeg >= 335 && windDeg <= 25)
            return "С";
        else if (windDeg > 25 && windDeg <= 70)
            return "С-В";
        else if (windDeg > 70 && windDeg <= 115)
            return "В";
        else if (windDeg > 115 && windDeg <= 160)
            return "Ю-В";
        else if (windDeg > 160 && windDeg <= 205)
            return "Ю";
        else if (windDeg > 205 && windDeg <= 250)
            return "Ю-З";
        else if (windDeg > 250 && windDeg <= 295)
            return "З";
        else
            return "C-З";
    }

    private void onError() {
        for(WeatherDelegate item : mDelegates){
            item.onError();
        }
        if (mTimer != null)
            mTimer.cancel();
    }

    public void setCity(String city) {
        if (!city.isEmpty()) {
            mCity = city;

            update();
        }
    }

    public String getCity() {
        return mCity;
    }

    public void setDelegate(WeatherDelegate delegate) {
        mDelegates.add(delegate);
    }
}
