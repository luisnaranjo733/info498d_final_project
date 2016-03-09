package info498d.uw.edu.smartalarm;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.util.concurrent.Service;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements ServiceConnection{
    private static final String TAG = "SETTING_FRAGMENT";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference sleepPreference = findPreference("pref_key_sleep_locations");
        sleepPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SleepLocationsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        final Preference bedTime = findPreference("pref_key_bed_time");
        bedTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                /*Intent intent = new Intent(getActivity(), CheckLocationService.class);
                getActivity().startService(intent);*/
                SharedPreferences sharedPref = getActivity().getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("bedTime", (String) newValue);
                editor.commit();
                return true;
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        /*Intent intent = new Intent(this.getActivity(), CheckLocationService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);*/
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        /*CheckLocationService.LocalBinder binder = (CheckLocationService.LocalBinder) service;
        CheckLocationService mService = binder.getService();*/
        // mService.startLocationCheck();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
