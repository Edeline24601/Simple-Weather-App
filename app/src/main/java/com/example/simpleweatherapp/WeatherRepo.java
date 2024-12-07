package com.example.simpleweatherapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherRepo {
    static RequestQueue requestQueue;
    double lat;
    double lon;
    String apiKey = "your_api_key";
    static final String URL_Base = "https://api.openweathermap.org/data/3.0/onecall?lat=%s&lon=%s&appid=%s";
    public String URL;
    static final String Icon_URL_Base = "https://openweathermap.org/img/wn/%s@2x.png";

    public WeatherRepo(double lat, double lon, Context context){
        this.lat = lat;
        this.lon = lon;
        URL = String.format(URL_Base, lat, lon, apiKey);
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public WeatherRepo(double lat, double lon, String apiKey){
        this.lat = lat;
        this.lon = lon;
        this.apiKey = apiKey;
        URL = String.format(URL_Base, lon, lat, apiKey);
    }

    public Weather requestCurrentWeatherData() {
        Weather weather = new Weather();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    //TODO : json 형식의 response를 weather 클래스에 mapping 하기
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject current = jsonObject.getJSONObject("current");

                        weather.time = current.getInt("dt");
//                        Log.i("weather Request Response", String.valueOf(weather.time));
                        weather.weather_description = current.getJSONArray("weather").getJSONObject(0).getString("main");
//                        Log.i("Weather Request Response", weather.weather_description);
                        weather.weather_icon_url = String.format(Icon_URL_Base, current.getJSONArray("weather").getJSONObject(0).getString("icon"));
//                        Log.i("Weather Request Response", weather.weather_icon_url);
                        weather.temperature = current.getInt("temp");
//                        Log.i("Weather Request Response", String.valueOf(weather.temperature));
                        weather.feels_like = current.getInt("feels_like");
//                        Log.i("Weather Request Response", String.valueOf(weather.feels_like));
                        weather.humidity = current.getDouble("humidity");
//                        Log.i("Weather Request Response", String.valueOf(weather.humidity));
                    } catch (Exception e) {
                        // TODO : log 출력으로 변경?
                        e.printStackTrace();
                    }
                },
                error -> Log.i("WeatherRepo", "Current weather request error")
        ) {

        };

        request.setShouldCache(false);
        requestQueue.add(request);
        Log.i("WeatherRepo", "Request sent(current weather)");

        return weather;
    }

    public List<Weather> requestHourlyWeatherData(){
        List<Weather> weathers = new ArrayList<>();
        StringRequest request = new StringRequest(
            Request.Method.GET,
            URL,
            response ->{
                //TODO : json 형식의 response를 weather 클래스에 mapping 하기
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray hourlyArray = jsonObject.getJSONArray("hourly");

                    for (int i = 0; i < hourlyArray.length(); i++){
                        JSONObject hourly = hourlyArray.getJSONObject(i);
                        Weather weather = new Weather();

                        weather.time = hourly.getInt("dt");
                        weather.weather_description = hourly.getJSONArray("weather").getJSONObject(0).getString("main");
                        weather.weather_icon_url = String.format(Icon_URL_Base, hourly.getJSONArray("weather").getJSONObject(0).getString("icon"));
                        weather.temperature = hourly.getInt("temp");
                        weather.feels_like = hourly.getInt("feels_like");
                        weather.humidity= hourly.getDouble("humidity");

                        weathers.add(weather);
                    }

                } catch (Exception e){
                    //TODO : log 출력으로 바꾸기
                    e.printStackTrace();
                }
            },
            error -> Log.i("WeatherRepo", "Hourly weather request error")
        ){

        };

        request.setShouldCache(false);
        requestQueue.add(request);
        Log.i("WeatherRepo", "Request sent(hourly weather)");

        return weathers;
    }

    public List<Weather> requestDailyWeatherData(){
        List<Weather> weathers = new ArrayList<>();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
                response ->{
                    //TODO : json 형식의 response를 weather 클래스에 mapping 하기
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray dailyArray = jsonObject.getJSONArray("daily");

                        for(int i = 0; i < dailyArray.length(); i++){
                            JSONObject daily = dailyArray.getJSONObject(i);
                            Weather weather = new Weather();

                            weather.time = daily.getInt("dt");
                            weather.weather_description = daily.getJSONArray("weather").getJSONObject(0).getString("main");
                            weather.weather_icon_url = String.format(Icon_URL_Base, daily.getJSONArray("weather").getJSONObject(0).getString("icon"));
                            weather.temperature = daily.getInt("temp");
                            weather.feels_like = daily.getInt("feels_like");
                            weather.humidity= daily.getDouble("humidity");

                            weathers.add(weather);
                        }
                    } catch (Exception e){
                        //TODO : log 출력으로 바꾸기
                        e.printStackTrace();
                    }
                },
                error -> Log.i("WeatherRepo", "Daily weather request error")
        ){

        };

        request.setShouldCache(false);
        requestQueue.add(request);
        Log.i("WeatherRepo", "Request sent(daily weather)");

        return weathers;
    }
}