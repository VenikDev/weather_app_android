package com.example.weacher_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Application;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements WeatherDelegate {

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
    // Кнопка обновления данных
    Button mBtnUpdate;

    private String nameCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(nameCity);

        mApp = (MyApplication) getApplicationContext();

        mIConImage = findViewById(R.id.Icon);
        mTemp = findViewById(R.id.textViewTemp);
        mMaxTemperature = findViewById(R.id.textViewMaxTemperature);
        mMinTemperature = findViewById(R.id.textViewMinTemperature);
        mWindSpeed = findViewById(R.id.textViewWindSpeed);
        mWindDeg = findViewById(R.id.textViewWindDeg);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
        mWindDeg.setText(mApp.getWeather().windDirection + "°");

        Picasso.with(this).load(mApp.getWeather().icon).into(mIConImage);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Connection ERROR!", Toast.LENGTH_LONG).show();
    }
}