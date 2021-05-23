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
                "d330221ce2dd6f070fc00118289bf0f8",
                "ru", 60000);
    }
}
