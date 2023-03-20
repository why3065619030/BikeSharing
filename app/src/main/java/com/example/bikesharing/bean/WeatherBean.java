package com.example.bikesharing.bean;


import java.io.Serializable;

public class WeatherBean implements Serializable {

    public String weather;  //取值
    public Float temperature; //温度值
    public String temperatureStr; //温度的描述值
    public String time; //时间值

    public WeatherBean(Float temperature, String time) {

        this.temperature = temperature;
        this.time = time;
        this.temperatureStr = temperature + "r/s";
    }
}