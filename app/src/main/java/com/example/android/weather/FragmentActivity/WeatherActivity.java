package com.example.android.weather.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.weather.R;
import com.example.android.weather.Settings.SettingsActivity;
import com.example.android.weather.Settings.SettingsUtils;
import com.example.android.weather.ui.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.location.Address;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private ImageView[] dots;
    LinearLayout linearLayout;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    ProgressDialog dialog;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText txtSearchLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(mPageChangeListener);

        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle("");

        drawPageSelectionIndicators(0);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();

                drawPageSelectionIndicators(position);
            }
        });

        dialog = new ProgressDialog(WeatherActivity.this);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void drawPageSelectionIndicators(int mPosition) {
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }

        linearLayout = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        dots = new ImageView[NUM_PAGES];
        for (int i = 0; i < NUM_PAGES; i++) {
            dots[i] = new ImageView(this);
            if (i == mPosition) {
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            } else {
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            linearLayout.addView(dots[i], params);
        }
    }

    private class WeatherPagerAdapter extends FragmentStatePagerAdapter {
        public WeatherPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return HourlyWeatherFragment.newInstance(position);

                default:
                    return WeatherPageFragment.create(position);

            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            /*if (object instanceof HourlyWeatherFragment) {
                ((HourlyWeatherFragment) object).getHourlyWeather();
            } else if (object instanceof WeatherPageFragment) {
                ((WeatherPageFragment) object).getCurrentWeather();
            }
            return super.getItemPosition(object);*/
            return PagerAdapter.POSITION_NONE;
        }
    }

    /**
     * To update fragment in ViewPager, we should call PagerAdapter.notifyDataSetChanged() when data changed.
     * we should also override FragmentPagerAdapter.getItemPosition(), and
     * implement a public method for updating fragment.
     * Refer to [Update Fragment from ViewPager](http://stackoverflow.com/a/18088509/2722270)
     */
    private void notifyViewPagerDataSetChanged() {
        //Log.d(TAG, "\nnotifyDataSetChanged()");
        mPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_set_location);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_location_search:
                GetCityByLocation();
                return true;
            case R.id.action_set_location:
                searchCities();
                return true;
        }
        return false;
    }

    private void searchCities() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(this.getString(R.string.search_title));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setSingleLine(true);
        alert.setView(input, 32, 0, 32, 0);
        alert.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    SettingsUtils.saveLocation(result, WeatherActivity.this);
                    notifyViewPagerDataSetChanged();
                }
            }
        });
        alert.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        alert.show();
    }

    void GetCityByLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        dialog.setMessage("getting your location");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getLocality();
                SettingsUtils.saveLocation(cityName, this);

                notifyViewPagerDataSetChanged();
            } else {
                Toast.makeText(this, "No location is detected", Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        //Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}
