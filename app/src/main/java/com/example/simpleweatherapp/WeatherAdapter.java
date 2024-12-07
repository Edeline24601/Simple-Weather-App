package com.example.simpleweatherapp;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<Weather> weatherList;
    public WeatherAdapter(List<Weather> weatherList){
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.temp.setText(String.valueOf(weather.temperature));
        holder.humidity.setText(String.valueOf(weather.humidity));
        holder.description.setText(weather.weather_description);

        try{
            URL url = new URL(weather.weather_icon_url);
            holder.weatherIcon.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
            Log.e("WeatherAdapter", "Error loading image");
            return;
        }
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void setWeatherList(List<Weather> weatherList){
        this.weatherList = weatherList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherIcon;
        public TextView  temp;
        public TextView humidity;
        public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherIcon = itemView.findViewById(R.id.forecast_weather_icon);
            temp = itemView.findViewById(R.id.forecast_weather_temp);
            humidity = itemView.findViewById(R.id.forecast_weather_hum);
            description = itemView.findViewById(R.id.forecast_weather_desc);
        }
    }
}
