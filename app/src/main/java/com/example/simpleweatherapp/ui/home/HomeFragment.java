package com.example.simpleweatherapp.ui.home;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleweatherapp.R;
import com.example.simpleweatherapp.Weather;
import com.example.simpleweatherapp.WeatherAdapter;
import com.example.simpleweatherapp.WeatherCallback;
import com.example.simpleweatherapp.WeatherRepo;
import com.example.simpleweatherapp.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RecyclerView recyclerView;

//    WeatherCallback weatherCallback = new WeatherCallback() {
//        @Override
//        public void onCurrentWeather(Weather w) {
//
//        }
//
//        @Override
//        public void onCurrentWeatherError(Weather w) {
//
//        }
//
//        @Override
//        public void onDailyWeather(List<Weather> daily) {
//
//        }
//
//        @Override
//        public void onDailyWeatherError(List<Weather> daily) {
//
//        }
//
//        @Override
//        public void onHourlyWeather(List<Weather> hourly) {
//
//        }
//
//        @Override
//        public void onHourlyWeatherError(List<Weather> hourly) {
//
//        }
//    }
    private FragmentHomeBinding binding;

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.weatherRecyclerView);
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        List<Weather> weatherList = new ArrayList<>();
        // TODO : change to current location, currently hardcoded to seoul station
        WeatherRepo weatherRepo = new WeatherRepo(37, 127, getContext());
//        weatherList = weatherRepo.requestHourlyWeatherData();
        WeatherAdapter adapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(adapter);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    weatherRepo.setLocation(location.getLatitude(), location.getLongitude());
                } else{
                    Log.d(HomeFragment.class.getName(), "Location is null");
                }
            }
        });

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Weather current_weather = weatherRepo.requestCurrentWeatherData(new WeatherCallback(){
                    @Override
                    public void onCurrentWeather(Weather weather) {
                        Log.d(HomeFragment.class.getName() + ".onCurrentWeather", weather == null ? "null" : weather.toString());
                        ImageView weatherIcon = root.findViewById(R.id.currentWeatherIcon);
                        TextView weatherDescription = root.findViewById(R.id.currentWeatherDescription);
                        TextView temp = root.findViewById(R.id.temp);
                        TextView humidity = root.findViewById(R.id.humidity);
                        try {
                            Picasso.get().load(weather.weather_icon_url).resize(400, 400).into(weatherIcon);
                            weatherDescription.setText(weather.weather_description);
                            temp.setText(String.valueOf(weather.temperature - 273)+"°C");
                            humidity.setText("Humidity : " + String.valueOf(weather.humidity) + "%");
                        } catch (Exception e){
                            Log.e("Current Weather error", e.toString());
                        }

                    }

                    @Override
                    public void onCurrentWeatherError(Weather w) {

                    }

                    @Override
                    public void onDailyWeather(List<Weather> daily) {

                    }

                    @Override
                    public void onDailyWeatherError(List<Weather> daily) {

                    }

                    @Override
                    public void onHourlyWeather(List<Weather> hourly) {

                    }

                    @Override
                    public void onHourlyWeatherError(List<Weather> hourly) {

                    }
                });

                //update forecast based on checked radio button
                RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
                int checkRadioButton = radioGroup.getCheckedRadioButtonId();
                if(checkRadioButton == R.id.hourly){
                    List<Weather> weatherList = weatherRepo.requestHourlyWeatherData(new WeatherCallback(){
                        @Override
                        public void onCurrentWeather(Weather weather) {}

                        @Override
                        public void onCurrentWeatherError(Weather w) {

                        }

                        @Override
                        public void onDailyWeather(List<Weather> daily) {

                        }

                        @Override
                        public void onDailyWeatherError(List<Weather> daily) {

                        }

                        @Override
                        public void onHourlyWeather(List<Weather> hourly) {
                            adapter.setItems(hourly);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onHourlyWeatherError(List<Weather> hourly) {

                        }
                    });
                }
                else if(checkRadioButton == R.id.daily){
                    List<Weather> weatherList = weatherRepo.requestDailyWeatherData(new WeatherCallback(){

                        @Override
                        public void onCurrentWeather(Weather w) {

                        }

                        @Override
                        public void onCurrentWeatherError(Weather w) {

                        }

                        @Override
                        public void onDailyWeather(List<Weather> daily) {
                            adapter.setItems(daily);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onDailyWeatherError(List<Weather> daily) {

                        }

                        @Override
                        public void onHourlyWeather(List<Weather> hourly) {

                        }

                        @Override
                        public void onHourlyWeatherError(List<Weather> hourly) {

                        }
                    });
                }
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}