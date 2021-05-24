package com.example.weacher_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Application;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity
        extends AppCompatActivity
        implements WeatherDelegate {

    MyApplication mApp;
    // Иконка
    ImageView mIConImage;
    // Температура
    TextView mTemp;
    // Макс температура
    TextView mMaxTemperature;
    // Мин температура
    TextView mMinTemperature;
    // Скорость ветра
    TextView mWindSpeed;
    // Напраление ветра
    TextView mWindDeg;
    // Влажность
    TextView mVisibility;
    // Облачность
    TextView mClouds;
    // Кнопка обновления данных
    Button mBtnUpdate;

    private String nameCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = (MyApplication) getApplicationContext();

        setTitle(mApp.getSelectedCityFromTheList());

        mIConImage = findViewById(R.id.Icon);
        mTemp = findViewById(R.id.textViewTemp);
        mMaxTemperature = findViewById(R.id.textViewMaxTemperature);
        mMinTemperature = findViewById(R.id.textViewMinTemperature);
        mWindSpeed = findViewById(R.id.textViewWindSpeed);
        mWindDeg = findViewById(R.id.textViewWindDeg);
        mVisibility = findViewById(R.id.textViewVisibility);
        mClouds = findViewById(R.id.textViewClouds);
        mBtnUpdate = findViewById(R.id.btnUpdate);

        mApp.getWeather().setDelegate(this);

        View.OnClickListener updateListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onUpdatePress(v);
            }
        };

        mBtnUpdate.setOnClickListener(updateListener);
    }

    private void onUpdatePress(View view) {
        mApp.getWeather().update();
        onUpdate();
    }

    @Override
    public void onUpdate() {
        mTemp.setText(mApp.getWeather().currentTemperature + " °C");
        mMaxTemperature.setText(mApp.getWeather().maxTemperature + " °C");
        mMinTemperature.setText(mApp.getWeather().minTemperature + " °C");

        mWindSpeed.setText(mApp.getWeather().windSpeed + " m/c");
        mWindDeg.setText(mApp.getWeather().windDirection + " (" + mApp.getWeather().direction + "°)");

        mVisibility.setText(mApp.getWeather().visibility + "");
        mClouds.setText(mApp.getWeather().clouds + "%");

        Picasso.with(this).load(mApp.getWeather().icon).into(mIConImage);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Connection ERROR!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Cities:
                cities();
                return true;
            case R.id.About:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Открыть список городов
     */
    private void cities() {
        Intent intent = new Intent(getApplicationContext(), ListCities.class);
        startActivity(intent);
    }

    /**
     * Открыть окно About
     */
    private void about() {
        Intent intent = new Intent(getApplicationContext(), About.class);
        startActivity(intent);
    }
}