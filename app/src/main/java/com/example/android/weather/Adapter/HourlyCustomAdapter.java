package com.example.android.weather.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.weather.Data.HourlyWeather;
import com.example.android.weather.R;

import java.util.ArrayList;

/**
 * Created by Bridget on 8/5/2016.
 */
public class HourlyCustomAdapter extends ArrayAdapter {
    static private class ViewHolder{
        ImageView imgDailyWeather;
        TextView tvHourlyTime, tvHourlyDesc, tvHourlyHighTemp, tvHourlyLowTemp,tvhourlyIcon;
        Typeface weatherFont;
    }
    ArrayList<HourlyWeather> hourlyWeathertList;
    Context context;

    public HourlyCustomAdapter(Context context, ArrayList<HourlyWeather> hourlyWeathertList) {
        super(context, R.layout.hourly_row, hourlyWeathertList);
        this.hourlyWeathertList=hourlyWeathertList;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View rowView=convertView;

        if(rowView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView=inflater.inflate(R.layout.hourly_row, parent,false);

            viewHolder=new ViewHolder();
            viewHolder.tvHourlyTime=(TextView)rowView.findViewById(R.id.tvTime);
            viewHolder.tvHourlyDesc=(TextView)rowView.findViewById(R.id.tvDescription);
            viewHolder.tvHourlyHighTemp=(TextView)rowView.findViewById(R.id.tvHigh);
            viewHolder.tvHourlyLowTemp=(TextView)rowView.findViewById(R.id.tvLow);
            viewHolder.tvhourlyIcon=(TextView)rowView.findViewById(R.id.tvWeatherIcon);
            viewHolder.weatherFont = Typeface.createFromAsset(context.getAssets(), "fonts/weather.ttf");
            viewHolder.tvhourlyIcon.setTypeface(viewHolder.weatherFont);
            //viewHolder.imgForecastWeather=(ImageView) rowView.findViewById(R.id.forecastImg);

            rowView.setTag(viewHolder);
        }
        else viewHolder=(ViewHolder)rowView.getTag();

        String highTemp=String.valueOf(hourlyWeathertList.get(position).getHighTemp());
        String lowTemp=String.valueOf(hourlyWeathertList.get(position).getLowTemp());

        viewHolder.tvHourlyTime.setText(hourlyWeathertList.get(position).getTime());
        viewHolder.tvHourlyDesc.setText(hourlyWeathertList.get(position).getDescription());
        viewHolder.tvHourlyHighTemp.setText(highTemp+"° ↑");
        viewHolder.tvHourlyLowTemp.setText(lowTemp+"° ↓");
        viewHolder.tvhourlyIcon.setText(hourlyWeathertList.get(position).getWeatherIcon());

        return rowView;
    }
}
