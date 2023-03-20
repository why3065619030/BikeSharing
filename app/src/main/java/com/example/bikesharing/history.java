package com.example.bikesharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bikesharing.bean.WeatherBean;
import com.example.bikesharing.notuse.MiuiWeatherView;
import com.example.bikesharing.notuse.index;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        MiuiWeatherView weatherView = (MiuiWeatherView) findViewById(R.id.weather);

        weatherView.setData(index.data);

    }
}