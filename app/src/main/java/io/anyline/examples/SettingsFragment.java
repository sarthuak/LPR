package io.anyline.examples;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.sarthuak.opencv.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_PREF_REPORTING_ON = "pref_report_energy";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }


}