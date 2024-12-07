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
import com.example.simpleweatherapp.WeatherRepo;
import com.example.simpleweatherapp.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

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
        weatherList = weatherRepo.requestHourlyWeatherData();
        WeatherAdapter adapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(adapter);

        //TODO : set button click listener
        binding.updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Weather current_weather = weatherRepo.requestCurrentWeatherData();
                if (current_weather != null){
                    Log.i("HomeFragment", current_weather.weather_description);
                    return;
                }
                List<Weather> weatherList = new ArrayList<>();
                //update current weather
                ImageView weatherIcon = root.findViewById(R.id.currentWeatherIcon);
                TextView weatherDescription = root.findViewById(R.id.currentWeatherDescription);
                TextView temp = root.findViewById(R.id.temp);
                TextView humidity = root.findViewById(R.id.humidity);

                //TODO: set image by url
//                weatherIcon = current_weather.weather_icon_url;;
                weatherDescription.setText(current_weather.weather_description);
                temp.setText(String.valueOf(current_weather.temperature));
                humidity.setText(String.valueOf(current_weather.humidity));


                //update forecast based on checked radio button
                RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
                int checkRadioButton = radioGroup.getCheckedRadioButtonId();
                if(checkRadioButton == R.id.hourly){
                    weatherList = weatherRepo.requestHourlyWeatherData();
                }
                else if(checkRadioButton == R.id.daily){
                    weatherList = weatherRepo.requestDailyWeatherData();
                }
                adapter.setWeatherList(weatherList);
                adapter.notifyDataSetChanged();
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