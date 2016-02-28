package info498d.uw.edu.smartalarm;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements AlarmListFragment.OnAlarmSelectedListener {
    protected static String TAG = "**SmartAlarm.Main";
    Menu menu;

    // fragment managing objects
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmListFragment alarmList = new AlarmListFragment();

        fragmentTransaction.add(R.id.master_fragment,
                alarmList);
        fragmentTransaction.commit();

        // TODO: set up list view for alarms
        // TODO: how are we going to store alarms?
        // TODO: set up master/detail view for alarms
        // TODO: need to set up settings and let them be stored
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


    // when an alarm is clicked from a list, the alarm details fragment is inflated through
    // this listener method
    @Override
    public void onAlarmSelected(Cursor cursor) {
        AlarmDetailsFragment alarmDetails = new AlarmDetailsFragment();

        Log.v(TAG, "alarm clicked");
        getFragmentManager().beginTransaction()
                .replace(R.id.master_fragment, alarmDetails)
                .addToBackStack(null)
                .commit();
    }


}
