package com.example.android.weather.FragmentActivity;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.weather.AppController;
import com.example.android.weather.Data.Channel;
import com.example.android.weather.R;
import com.example.android.weather.Service.WeatherServiceCallback;
import com.example.android.weather.Service.YahooWeatherService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bridget on 8/2/2016.
 */
public class WeatherPageFragment extends Fragment implements WeatherServiceCallback {
    TextView tvTemperature, tvLocation, tvDescription,tvCurrentDate,tvCurrentDay;
    TextView tvHighTemp, tvLowTemp,tvWind,tvHumidity,tvLat,tvLong, tvSunrise, tvSunset, tvCelFar;
    ImageView imgWeather;
    YahooWeatherService yahooService;
    ProgressDialog dialog;
    String url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Dhaka%22)&format=json";

    public static final String ARG_PAGE = "page";
    TextView text1;

    private int mPageNumber;

    public static WeatherPageFragment create(int pageNumber) {
        WeatherPageFragment fragment = new WeatherPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherPageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_weather_page, container, false);

        tvTemperature=(TextView)rootView.findViewById(R.id.tvTemperature);
        tvLocation=(TextView)rootView.findViewById(R.id.tvLocation);
        tvDescription=(TextView)rootView.findViewById(R.id.tvDescription);
        tvCurrentDay=(TextView)rootView.findViewById(R.id.tvDay);
        tvCurrentDate=(TextView)rootView.findViewById(R.id.tvDate);
        tvHighTemp=(TextView)rootView.findViewById(R.id.tvHighTemp);
        tvLowTemp=(TextView)rootView.findViewById(R.id.tvLowTemp);
        tvLat=(TextView)rootView.findViewById(R.id.tvLat);
        tvLong=(TextView)rootView.findViewById(R.id.tvLong);
        tvSunrise=(TextView)rootView.findViewById(R.id.tvSunrise);
        tvSunset=(TextView)rootView.findViewById(R.id.tvSunset);
        tvWind=(TextView)rootView.findViewById(R.id.tvWind);
        tvHumidity=(TextView)rootView.findViewById(R.id.tvHumidity);
        tvCelFar=(TextView)rootView.findViewById(R.id.tvF_C);
        imgWeather=(ImageView) rootView.findViewById(R.id.weatherImg);

        getCurrentWeather();

        /*yahooService=new YahooWeatherService(this);
        yahooService.refreshWeather("Bangladesh, Dhaka");*/

        /*dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();*/

        //tvTemperature.setText("524 nikhjh");

        return rootView;
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        dialog.hide();
    }

    @Override
    public void ServiceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(getActivity(),"Sorry",Toast.LENGTH_SHORT).show();

    }

    public int getPageNumber() {
        return mPageNumber;
    }


    //get current weather data (JSON)
    public void getCurrentWeather(){
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject query = response.getJSONObject("query");
                    JSONObject results = query.getJSONObject("results");
                    JSONObject channel = results.getJSONObject("channel");
                    JSONObject unit=channel.getJSONObject("units");
                    String celFar=unit.getString("temperature");

                    JSONObject item = channel.getJSONObject("item");
                    JSONObject condition=item.getJSONObject("condition");
                    String temperature = condition.getString("temp");
                    String text=condition.getString("text");
                    String date=condition.getString("date");

                    JSONObject location=channel.getJSONObject("location");
                    String city=location.getString("city");

                    JSONObject wind=channel.getJSONObject("wind");
                    String speed=wind.getString("speed");

                    tvCelFar.setText(celFar);
                    tvTemperature.setText(temperature);
                    tvDescription.setText(text);
                    tvCurrentDate.setText(date);
                    tvLocation.setText(city);
                    tvWind.setText(speed);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError){
                    Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_SHORT);

                }
            }
        }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

}
