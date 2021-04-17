package com.example.weacher_app;

import android.app.Application;
import java.util.ArrayList;

public class MyApplication extends Application {
    private ArrayList<String> mListOfCities;
    private Weather mWeather;


    public ArrayList<String> getListCities()
        { return mListOfCities; }

    public Weather getWeather()
        { return mWeather; }

    @Override
    public void onCreate(){
        super.onCreate();

        mListOfCities = new ArrayList<String>();
        mListOfCities.add("Нижний Тагил");

        mWeather = new Weather(this, mListOfCities.get(0),
                "a3e779f05934a6b63039eb3b2cf1b16f",
                "ru", 60000);
    }
}
