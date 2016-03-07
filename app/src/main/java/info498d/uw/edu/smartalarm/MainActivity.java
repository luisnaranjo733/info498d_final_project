package info498d.uw.edu.smartalarm;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements AlarmListFragment.OnAlarmSelectedListener {
    protected static String TAG = "**SmartAlarm.Main";
    Menu menu;

    AlarmListFragment alarmListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (alarmListFragment == null) {
            alarmListFragment = new AlarmListFragment();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (findViewById(R.id.rightPane) != null) {
            Log.v(TAG, "Landscape mode");
//            alarmDetailsFragment = new AlarmDetailsFragment();
//            fragmentTransaction.add(R.id.leftPane, alarmListFragment);
//            fragmentTransaction.add(R.id.rightPane, alarmDetailsFragment);

        } else {
            Log.v(TAG, "Portrait mode");
            fragmentTransaction.add(R.id.singlePane, alarmListFragment);
        }
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        this.menu = menu;
        MenuItem settingsItem = menu.findItem(R.id.open_settings_option);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v(TAG, "Opening settings activity");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

                // Return true to consume this click and prevent others from executing.
                return true;
            }
        });
        return true;
    }

    // where does this come from?
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "orientation changed");
        setContentView(R.layout.activity_main);
    }


    // listens for alarm being clicked to show details
    @Override
    public void onAlarmSelected(Alarm alarm) {
        AlarmDetailsFragment alarmDetailsFragment = new AlarmDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title", alarm.alarmTitle);
        bundle.putString("day", alarm.getDay());
        bundle.putString("time", alarm.getTime());
        bundle.putBoolean("active", alarm.active);

        alarmDetailsFragment.setArguments(bundle);

        Log.v(TAG, "onAlarmSelected");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (findViewById(R.id.rightPane) != null) {
            fragmentTransaction.replace(R.id.leftPane, alarmDetailsFragment);
        } else {
            fragmentTransaction.replace(R.id.singlePane, alarmDetailsFragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
