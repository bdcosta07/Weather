package com.example.android.weather.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.format.Time;
import android.widget.AutoCompleteTextView;

import com.example.android.weather.R;
import com.example.android.weather.Settings.SettingsUtils;

import android.view.LayoutInflater;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

public class AppUtils {
    public static String formatTemperature(Context context, double temperature) {
        // Data fetched in Censius by default. If user prefers to see in Fahrenheit or Kelvin, convert the values here.
        String suffix = "";
        if (!SettingsUtils.PreferredTemperatureUnit(context).equals(context.getString(R.string.temperature_unit_value_k)))
            suffix = "\u00B0";

        if (SettingsUtils.PreferredTemperatureUnit(context).equals(context.getString(R.string.temperature_unit_value_f))) {
            temperature = (temperature * 1.8) + 32;
        } else if (SettingsUtils.PreferredTemperatureUnit(context).equals(context.getString(R.string.temperature_unit_value_k))) {
            temperature += 273.15;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        return String.format(context.getString(R.string.format_temperature), temperature, suffix);
    }

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return a user-friendly representation of the date.
     */
    public static String getFriendlyDayString(Context context, long dateInMillis) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"

        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(
                    formatId,
                    today,
                    getFormattedMonthDay(context, dateInMillis)));
        } else if (julianDay < currentJulianDay + 7) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, dateInMillis);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                     in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(AppUtils.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat = R.string.format_wind_kmh;
        if (SettingsUtils.PreferredSpeedUnit(context).equals(context.getString(R.string.speed_unit_kph))) {
            windFormat = R.string.format_wind_kmh;
        } else if (SettingsUtils.PreferredSpeedUnit(context).equals(context.getString(R.string.speed_unit_mph))) {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        } else if (SettingsUtils.PreferredSpeedUnit(context).equals(context.getString(R.string.speed_unit_mps))) {
            windFormat = R.string.format_wind_mph;
            windSpeed = .27777778f * windSpeed;
        }

        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        // You know what's fun, writing really long if/else statements with tons of possible
        // conditions.  Seriously, try it!
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

    public static int getArtResourceForYahooWeatherCondition(int weatherId){
        if (weatherId==3)
            return R.drawable.severelthunderstoms;
        if (weatherId==4)
            return R.drawable.thunderstorms;
        else if(weatherId==9)
            return R.drawable.drizzle;
        else if (weatherId==8)
            return R.drawable.freezingdrizzle;
        else if (weatherId==11)
            return R.drawable.showers;
        else if ((weatherId==27)||(weatherId==28))
            return R.drawable.mostlycloudy;
        else if (weatherId==26)
            return R.drawable.cloudy;
        else if (weatherId==32)
            return R.drawable.sunny;
        else if (weatherId==36)
            return R.drawable.hot;
        else if (weatherId==31)
            return R.drawable.clear;
        else if (weatherId==37)
            return R.drawable.isolatedthunderstorms;
        else if ((weatherId==38)||(weatherId==39))
            return R.drawable.scatteredthunderstorms;
        else if (weatherId==44)
            return R.drawable.partlycloudy;
        else if (weatherId==47)
            return R.drawable.isolatedthundersowers;
        else if (weatherId==35)
            return R.drawable.mixedrainandhail;
        else if ((weatherId==33)||(weatherId==34))
            return R.drawable.fair;
        else if ((weatherId==29)||(weatherId==30))
            return R.drawable.partlycloudy;
        else if ((weatherId==24))
            return R.drawable.windy;
        else if ((weatherId==22))
            return R.drawable.smoke;
        else if ((weatherId==23))
            return R.drawable.blustery;
        else if ((weatherId==21))
            return R.drawable.haze;
        else return R.drawable.icon_na;
    }

    public static String BuildYahooURL(String cityName) {
        StringBuilder sb = new StringBuilder(Constants.YAHOO_BASE_URL);
        String yql = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", cityName);
        try {
            sb.append("?q=" + URLEncoder.encode(yql, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("&format=json");

        return sb.toString();
    }

    public static String BuildOpenWeatherURL(String cityName, String type) {
        StringBuilder sb = new StringBuilder(Constants.OPENWEATHER_BASE_URL);
        sb.append(type);
        sb.append("?q=" + cityName);
        sb.append("&mode=json");
        sb.append("&units=metric");
        sb.append("&appid=" + Constants.OPENWEATHER_API_KEY);

        return sb.toString();
    }
}
