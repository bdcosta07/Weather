package com.example.android.weather.Data;

import org.json.JSONObject;

/**
 * Created by Bridget on 8/3/2016.
 */
public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public void Populate(JSONObject data) {
        temperature=data.optString("temperature");
    }
}
