package com.example.android.weather.Data;

/**
 * Created by Bdjobs on 03-Aug-16.
 */
public class Forecast {
    private int imageId;
    private int code;
    private int high;
    private int low;
    private String description;
    private String day;

    public Forecast(int imageId, String day, int code, int high, int low, String description) {
        this.day = day;
        this.code = code;
        this.high = high;
        this.low = low;
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Forecast() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
