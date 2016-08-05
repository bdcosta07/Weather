package com.example.android.weather.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.weather.Activity.ForecastActivity;
import com.example.android.weather.Adapter.ForecastCustomAdapter;
import com.example.android.weather.Adapter.HourlyCustomAdapter;
import com.example.android.weather.Data.Forecast;
import com.example.android.weather.Data.HourlyWeather;
import com.example.android.weather.R;
import com.example.android.weather.VollyAppController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HourlyWeatherFragment extends Fragment {

    private static final String ARG_PAGE = "page";
    // TODO: Rename and change types of parameters
    private int mPageNumber;
    ArrayList<HourlyWeather> hourlyWeatherList;
    HourlyCustomAdapter hourlyAdapter;
    ListView listView;
    TextView tvLocation;
    String url="http://api.openweathermap.org/data/2.5/forecast?q=Dhaka&mode=json&appid=3f7228abe9f7983448ac7d087fa3b1ac";

    public HourlyWeatherFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HourlyWeatherFragment newInstance(int pageNumber) {
        HourlyWeatherFragment fragment = new HourlyWeatherFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageNumber = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        listView=(ListView)rootView.findViewById(R.id.lvHourlyList);
        tvLocation=(TextView)rootView.findViewById(R.id.tvLocation);

        hourlyWeatherList=new ArrayList<>();
        hourlyAdapter=new HourlyCustomAdapter(getActivity(),hourlyWeatherList);
        getHourlyWeather();


        // Inflate the layout for this fragment
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public void getHourlyWeather() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject city=response.getJSONObject("city");
                    String cityName=city.getString("name");
                    tvLocation.setText(cityName);

                    JSONArray listArray = response.getJSONArray("list");

                    for (int i = 0; i < listArray.length(); i++) {
                        HourlyWeather hourlyWeather = new HourlyWeather();

                        JSONObject object = listArray.getJSONObject(i);
                        JSONObject main=object.getJSONObject("main");

                        double temperature =main.getDouble("temp");
                        double highTemp = main.getDouble("temp_min");
                        double lowTemp = main.getDouble("temp_max");
                        //String time=object.getString("dt");

                        JSONArray weatherArray=object.getJSONArray("weather");
                        for(int j=0;j<weatherArray.length();j++){
                            JSONObject weatherObj=weatherArray.getJSONObject(j);
                            String description=weatherObj.getString("description");

                            hourlyWeather.setDescription(description);
                        }

                        //hourlyWeather.setTime(time);
                        hourlyWeather.setHighTemp(highTemp);
                        hourlyWeather.setLowTemp(lowTemp);
                        hourlyWeather.setTemperature(temperature);

                        hourlyWeatherList.add(hourlyWeather);
                    }

                    listView.setAdapter(hourlyAdapter);
                    hourlyAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT);

                }
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private int arrayIndexOf(JSONArray array, String str){
        for(int i=0;i<array.length();i++) {
            try {
                if(str.equals(array.getString(i))) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
