package com.example.simpleweatherapp.ui.home;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.weatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        List<Weather> weatherList = new ArrayList<>();
        // TODO : change to current location, currently hardcoded to seoul station
        WeatherRepo weatherRepo = new WeatherRepo(37.64, 127.07, getContext());
//        weatherList = weatherRepo.requestHourlyWeatherData();
        WeatherAdapter adapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(adapter);

        //TODO : set button click listener
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

                        weatherDescription.setText(weather.weather_description);
                        temp.setText(String.valueOf(weather.temperature));
                        humidity.setText(String.valueOf(weather.humidity));
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

                }
                else if(checkRadioButton == R.id.daily){

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