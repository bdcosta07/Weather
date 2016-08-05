package com.example.android.weather.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.weather.Adapter.ForecastCustomAdapter;
import com.example.android.weather.Data.Forecast;
import com.example.android.weather.R;

import java.util.ArrayList;

public class HourlyWeatherFragment extends Fragment {



    private static final String ARG_PAGE = "page";

    // TODO: Rename and change types of parameters
    private int mPageNumber;

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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_forecast_page, container, false);

        TextView text = (TextView) rootView.findViewById(R.id.text3);
        text.setText(getString(R.string.title_template_step_2, mPageNumber));


        // Inflate the layout for this fragment
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
