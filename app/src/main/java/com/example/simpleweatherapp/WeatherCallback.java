package com.example.simpleweatherapp;

import java.util.List;

public interface WeatherCallback {
    void onCurrentWeather(Weather w);
    void onCurrentWeatherError(Weather w);
    void onDailyWeather(List<Weather> daily);
    void onDailyWeatherError(List<Weather> daily);
    void onHourlyWeather(List<Weather> hourly);
    void onHourlyWeatherError(List<Weather> hourly);
}
