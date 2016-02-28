package info498d.uw.edu.smartalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "**SmartAlarm.Main";
    Menu menu;

    private static CursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runTest();
        runTest();

        String[] cols = new String[]{AlarmDatabase.AlarmEntry.COL_TIME, AlarmDatabase.AlarmEntry.COL_DAY, AlarmDatabase.AlarmEntry.COL_TITLE,};
        int[] ids = new int[]{R.id.alarm_item_time, R.id.alarm_item_day, R.id.alarm_item_title};

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.alarm_item,
                AlarmDatabase.queryDatabase(this),
                cols, ids,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        //set the adapter
        AdapterView listView = (AdapterView)findViewById(R.id.alarm_list_view);
        listView.setAdapter(adapter);

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

    private void runTest() {
        AlarmDatabase.testDatabase(this);
    }
}
