package com.example.weacher_app;

public interface WeatherDelegate {
    // произошло обновление погоды
    void onUpdate();

    // Произошла ошибка
    void onError();
}
