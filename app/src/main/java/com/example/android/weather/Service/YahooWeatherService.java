package com.example.android.weather.Service;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Bridget on 8/3/2016.
 */
public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;

    public YahooWeatherService(WeatherServiceCallback callback) {
        this.callback = callback;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void refreshWeather(final String location){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String YQL= String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"Dhaka\")", location);
                String endPoint=String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys", Uri.encode(YQL));

                try {
                    URL url=new URL(endPoint);

                    URLConnection connection=url.openConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(location);
    }
}
