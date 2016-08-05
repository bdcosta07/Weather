/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.weather.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;

import com.example.android.weather.R;

/**
 * Utilities and constants related to app settings_prefs.
 */
public class SettingsUtils {

    //private static final String TAG = makeLogTag(SettingsUtils.class);

    public static final String PREF_LOCATION = "pref_location";
    public static final String PREF_TEMPERATURE_UNIT = "pref_temperature_unit";
    public static final String PREF_LENGTH_UNIT = "pref_length_unit";
    public static final String PREF_SPEED_UNIT = "pref_speed_unit";
    public static final String PREF_PRESSURE_UNIT = "pref_pressure_unit";
    public static final String PREF_WIND_DIRECTION_FORMAT = "pref_wind_direction_format";

    /**
     * Helper method to register a settings_prefs listener. This method does not automatically handle
     * {@code unregisterOnSharedPreferenceChangeListener() un-registering} the listener at the end
     * of the {@code context} lifecycle.
     *
     * @param context  Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @param listener Listener to register.
     */
    public static void registerOnSharedPreferenceChangeListener(final Context context,
                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Helper method to un-register a settings_prefs listener typically registered with
     * {@code registerOnSharedPreferenceChangeListener()}
     *
     * @param context  Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @param listener Listener to un-register.
     */
    public static void unregisterOnSharedPreferenceChangeListener(final Context context,
                                                                  SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static String PreferredLocation(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_LOCATION, context.getResources().getString(R.string.pref_location_default));
    }

    public static String PreferredTemperatureUnit(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_TEMPERATURE_UNIT, context.getResources().getString(R.string.pref_temperature_unit_default));
    }

    public static String PreferredLengthUnit(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_LENGTH_UNIT, context.getResources().getString(R.string.pref_temperature_unit_default));
    }

    public static String PreferredSpeedUnit(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_SPEED_UNIT, context.getResources().getString(R.string.pref_speed_unit_default));
    }

    public static String PreferredPressureUnit(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_PRESSURE_UNIT, context.getResources().getString(R.string.pref_pressure_unit_default));
    }

    public static String PreferredWindDirectionFormat(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_WIND_DIRECTION_FORMAT, context.getResources().getString(R.string.pref_wind_direction_format_default));
    }
}
