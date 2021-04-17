package com.example.weacher_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements WeatherDelegate {

    MyApplication mApp;

    ImageView mIConImage;
    TextView mTemp;
    Button mBtnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = (MyApplication)getApplicationContext();

        mIConImage  = findViewById(R.id.Icon);
        mTemp       = findViewById(R.id.textViewTemp);
        mBtnUpdate  = findViewById(R.id.btnUpdate);

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
    }

    @Override
    public void onUpdate() {
        mTemp.setText(mApp.getWeather().currentTemperature + " C");
        Picasso.with(this).load(mApp.getWeather().icon).into(mIConImage);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Connection ERROR!", Toast.LENGTH_LONG).show();
    }
}