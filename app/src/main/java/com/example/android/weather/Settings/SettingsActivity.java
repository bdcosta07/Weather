package com.example.android.weather.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.example.android.weather.R;
import com.example.android.weather.ui.BaseActivity;
import com.example.android.weather.ui.Widget.DrawShadowFrameLayout;
import com.example.android.weather.util.UIUtils;

/**
 * Created by Ratul on 8/5/2016.
 */
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);
        overridePendingTransition(0, 0);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * The Fragment is added via the R.layout.settings_act layout xml.
     */
    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        public SettingsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_prefs);

            SettingsUtils.registerOnSharedPreferenceChangeListener(getActivity(), this);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            SettingsUtils.unregisterOnSharedPreferenceChangeListener(getActivity(), this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case "unit":
                case "lengthUnit":
                case "speedUnit":
                case "pressureUnit":
                case "windDirectionFormat":
                    setListPreferenceSummary(key);
                    break;
            }
        }

        private void setContentTopClearance(int clearance) {
            if (getView() != null) {
                getView().setPadding(getView().getPaddingLeft(), clearance,
                        getView().getPaddingRight(), getView().getPaddingBottom());
            }
        }

        private void setListPreferenceSummary(String preferenceKey) {
            ListPreference preference = (ListPreference) findPreference(preferenceKey);
            if (preference != null)
                preference.setSummary(preference.getEntry());
        }

        @Override
        public void onResume() {
            super.onResume();

            // configure the fragment's top clearance to take our overlaid controls (Action Bar
            // and spinner box) into account.
            int actionBarSize = UIUtils.calculateActionBarSize(getActivity());
            DrawShadowFrameLayout drawShadowFrameLayout =
                    (DrawShadowFrameLayout) getActivity().findViewById(R.id.main_content);
            if (drawShadowFrameLayout != null) {
                drawShadowFrameLayout.setShadowTopOffset(actionBarSize);
            }
            setContentTopClearance(actionBarSize);

            // Set summaries to current value
            setListPreferenceSummary("unit");
            setListPreferenceSummary("lengthUnit");
            setListPreferenceSummary("speedUnit");
            setListPreferenceSummary("pressureUnit");
            setListPreferenceSummary("refreshInterval");
            setListPreferenceSummary("windDirectionFormat");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    int x =5;
                    //startActivity(new Intent(this, SettingsActivity.class));
                    break;

                default:
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
