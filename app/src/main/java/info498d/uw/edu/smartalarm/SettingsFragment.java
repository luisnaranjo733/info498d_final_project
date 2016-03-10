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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements ServiceConnection{
    private static final String TAG = "SETTING_FRAGMENT";

    private int target = -1;

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

        final Preference targetSleep = findPreference("pref_key_sleep_time");
        targetSleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences sharedPref = getActivity().getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("targetSleepTime", (String) o);

                editor.commit();

                target = Integer.parseInt(o.toString());

                return true;
            }
        });



        final Preference bedTime = findPreference("pref_key_bed_time");
        bedTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent intent = new Intent(getActivity(), CheckLocationService.class);
                getActivity().startService(intent);
                SharedPreferences sharedPref = getActivity().getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                Log.v(TAG, preference.toString());
                Calendar c = Calendar.getInstance();

                editor.putString("bedTime", (String) newValue);
                editor.putInt("day", c.DAY_OF_MONTH);


                String bedTime = sharedPref.getString("bedTime","");

                if(!bedTime.equals("")) {
                    String bedTimeArray[] =bedTime.split(":");
                    String bedTimeHour = bedTimeArray[0];
                    String bedTimeMin = bedTimeArray[1];

                    if(bedTimeHour.length()==1) {
                        bedTimeHour = "0"+bedTimeHour;
                    }
                    if(bedTimeMin.length()==1) {
                        bedTimeMin = "0"+bedTimeMin;
                    }

                    bedTime = bedTimeHour + ":" + bedTimeMin;

                    int wakeTimeHour = (int)((Integer.parseInt(bedTimeHour) + target)%24);
                    String wakeHour = wakeTimeHour+"";

                    if(wakeHour.length()==1) {
                        wakeHour = "0"+wakeHour;
                    }

                    String wakeTime = wakeHour+":"+bedTimeMin;

                    Log.v(TAG,"Bed Time: " + bedTime);
                    Log.v(TAG,"Wake Time: " + wakeTime);
                    editor.putString("wakeTime", wakeTime);

                }

                editor.commit();
                //Log.v(TAG, sharedPref.getString("bedTime", "default"));
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
