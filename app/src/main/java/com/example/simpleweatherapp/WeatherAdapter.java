package com.example.simpleweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import java.util.Date;
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
        holder.setWeather(weather);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void setItems(List<Weather> weatherList){
        this.weatherList = weatherList;
    }

    public Weather getItem(int position) {
        return weatherList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherIcon;
        public TextView time;
        public TextView  temp;
        public TextView humidity;
        public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherIcon = itemView.findViewById(R.id.forecast_weather_icon);
            time = itemView.findViewById(R.id.forecast_weather_time);
            temp = itemView.findViewById(R.id.forecast_weather_temp);
            humidity = itemView.findViewById(R.id.forecast_weather_hum);
            description = itemView.findViewById(R.id.forecast_weather_desc);
        }

        public void setWeather(Weather weather){
            Date date = new Date(weather.time * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            Picasso.get().load(weather.weather_icon_url).resize(400, 400).into(weatherIcon);
            time.setText(sdf.format(date));
            temp.setText(String.valueOf(weather.temperature - 273)+"°C");
            humidity.setText("Humidity : " + String.valueOf(weather.humidity) + "%");
            description.setText(weather.weather_description);
        }
    }
}
