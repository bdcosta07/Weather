package com.example.android.weather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.weather.Data.Forecast;
import com.example.android.weather.FragmentActivity.ForecastPageFragment;
import com.example.android.weather.R;

import java.util.ArrayList;

/**
 * Created by Bridget on 8/2/2016.
 */
public class ForecastCustomAdapter extends ArrayAdapter {

    static private class ViewHolder{
        ImageView imgForecastWeather;
        TextView tvForecastDay, tvForecastDesc, tvForecastHighTemp, tvForecastLowTemp;

    }

    ArrayList<Forecast> dailyForecastList;
    Context context;


    public ForecastCustomAdapter(Context context, ArrayList<Forecast> dailyForecastList) {
        super(context, R.layout.daily_forecast_row, dailyForecastList);
        this.dailyForecastList=dailyForecastList;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View rowView=convertView;

        if(rowView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView=inflater.inflate(R.layout.daily_forecast_row, parent,false);

            viewHolder=new ViewHolder();
            viewHolder.tvForecastDay=(TextView)rowView.findViewById(R.id.tvDay);
            viewHolder.tvForecastDesc=(TextView)rowView.findViewById(R.id.tvDescription);
            viewHolder.tvForecastHighTemp=(TextView)rowView.findViewById(R.id.tvHigh);
            viewHolder.tvForecastLowTemp=(TextView)rowView.findViewById(R.id.tvLow);
            viewHolder.imgForecastWeather=(ImageView) rowView.findViewById(R.id.forecastImg);

            rowView.setTag(viewHolder);
        }
        else viewHolder=(ViewHolder)rowView.getTag();

        viewHolder.tvForecastDay.setText(dailyForecastList.get(position).getDay());
        viewHolder.tvForecastDesc.setText(dailyForecastList.get(position).getDescription());
        viewHolder.tvForecastHighTemp.setText(dailyForecastList.get(position).getHigh());
        viewHolder.tvForecastLowTemp.setText(dailyForecastList.get(position).getLow());

        return super.getView(position, convertView, parent);
    }
}
