package info498d.uw.edu.smartalarm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AlarmListFragment.OnAlarmSelectedListener, NewAlarmFragment.OnNewAlarmListener {
    protected static String TAG = "**SmartAlarm.Main";
    Menu menu;

    AlarmListFragment alarmListFragment;
    NewAlarmFragment newAlarmFragment;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        SharedPreferences sharedPreferences = this.getSharedPreferences("times", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("last_time",hour+":"+min);
        editor.commit();
        context = getApplicationContext();


        if (alarmListFragment == null) {
            alarmListFragment = new AlarmListFragment();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (findViewById(R.id.rightPane) != null) {
            Log.v(TAG, "Landscape mode");
            AlarmDetailsFragment alarmDetailsFragment = new AlarmDetailsFragment();
            fragmentTransaction.replace(R.id.leftPane, alarmListFragment);
            fragmentTransaction.replace(R.id.rightPane, alarmDetailsFragment);

        } else {
            Log.v(TAG, "Portrait mode");
            fragmentTransaction.replace(R.id.singlePane, alarmListFragment);
        }
        fragmentTransaction.commit();

    }

    public static Context getMainContext() {
        return context;
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
        bundle.putString("day", alarm.getDate());
        bundle.putString("time", alarm.getTimeRepresentation());
        bundle.putBoolean("active", alarm.active);

        alarmDetailsFragment.setArguments(bundle);

        Log.v(TAG, "onAlarmSelected");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (findViewById(R.id.rightPane) != null) {
            fragmentTransaction.replace(R.id.rightPane, alarmDetailsFragment);
        } else {
            fragmentTransaction.replace(R.id.singlePane, alarmDetailsFragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onNewAlarmButtonPressed() {
        Log.v(TAG, "on new alarm selected");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        newAlarmFragment = new NewAlarmFragment();
        fragmentTransaction.replace(R.id.singlePane, newAlarmFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onDatePicked(NewAlarmFragment.DatePickerFragment datePickerFragment) {
        newAlarmFragment.datePickerBtn.setText(datePickerFragment.toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "Overriding on activity result in main activity");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onShowDatePicker(DialogFragment timePickerFragment) {
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onShowTimePicker(DialogFragment datePickerFragment) {
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onNewAlarmSet(Alarm alarm) {
        Log.v(TAG, "Alarm set!");
        alarmListFragment.adapter.add(alarm);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.singlePane,alarmListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
