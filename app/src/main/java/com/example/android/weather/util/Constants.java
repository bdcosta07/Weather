package com.example.android.weather.util;


import android.os.ParcelUuid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Constants {
    public static final String DEFAULT_CITY = "Dhaka";

    public static final String YAHOO_BASE_URL = "https://query.yahooapis.com/v1/public/yql";

    public static final String OPENWEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String OPENWEATHER_API_KEY = "3f7228abe9f7983448ac7d087fa3b1ac";

    public static final String OPENWEATHER_CALL_TYPE_FORECAST = "forecast";
    public static final String OPENWEATHER_CALL_TYPE_CURRENT = "weather";
}
