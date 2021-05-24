package com.example.weacher_app;

import android.app.Application;
import java.util.ArrayList;

public class MyApplication extends Application {
    private ArrayList<String> mListOfCities;
    private Weather mWeather;
    private String SelectedCity = "Нижний Тагил";

    public ArrayList<String> getListCities()
        { return mListOfCities; }

    public Weather getWeather()
        { return mWeather; }

    public void setSelectedCityFromTheList(String newCity){
        SelectedCity = newCity;
    }

    public String getSelectedCityFromTheList(){
        return SelectedCity;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        mListOfCities = new ArrayList<String>();
        mListOfCities.add("Нижний Тагил");

        mWeather = new Weather(this, SelectedCity,
                "d330221ce2dd6f070fc00118289bf0f8",
                "ru", 60000);
    }
}
