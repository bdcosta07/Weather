package com.example.android.weather.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.weather.Adapter.ForecastCustomAdapter;
import com.example.android.weather.Data.Forecast;
import com.example.android.weather.R;
import com.example.android.weather.VollyAppController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Forecast> forecastList;
    ForecastCustomAdapter forecastAdapter;
    String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Dhaka%22)&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        listView=(ListView)findViewById(R.id.lvForecastList);

        forecastList=new ArrayList<>();
        forecastAdapter=new ForecastCustomAdapter(this,forecastList);

        getForecastWeather();
    }

    public void getForecastWeather(){
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject query=response.getJSONObject("query");
                    JSONObject result=query.getJSONObject("results");
                    JSONObject channel=result.getJSONObject("channel");
                    JSONObject item=channel.getJSONObject("item");
                    JSONArray forecast=item.getJSONArray("forecast");

                    for (int i=0; i<forecast.length();i++){
                        JSONObject jsonObject=forecast.getJSONObject(i);

                        String day=jsonObject.getString("day");
                        int highTemp =Integer.parseInt(jsonObject.getString("high"));
                        int lowTemp =Integer.parseInt(jsonObject.getString("low"));
                        String description=jsonObject.getString("text");

                        Forecast forecastClass=new Forecast();
                        forecastClass.setDay(day);
                        forecastClass.setHigh(highTemp);
                        forecastClass.setLow(lowTemp);
                        forecastClass.setDescription(description);

                        forecastList.add(forecastClass);
                    }

                    listView.setAdapter(forecastAdapter);
                    forecastAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError){
                    Toast.makeText(ForecastActivity.this,"Check your internet connection",Toast.LENGTH_SHORT);

                }
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

}
