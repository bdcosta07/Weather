package com.example.android.weather.Data;

/**
 * Created by Bridget on 8/5/2016.
 */
public class HourlyWeather {
    int weatherIcon;
    double temperature;
    String time;
    double highTemp;
    double lowTemp;
    String description;


    public HourlyWeather() {
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
