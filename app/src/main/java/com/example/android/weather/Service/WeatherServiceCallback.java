package com.example.android.weather.Service;

import com.example.android.weather.Data.Channel;

/**
 * Created by Bridget on 8/3/2016.
 */
public interface WeatherServiceCallback {
    void ServiceSuccess(Channel channel);
    void ServiceFailure(Exception exception);
}
