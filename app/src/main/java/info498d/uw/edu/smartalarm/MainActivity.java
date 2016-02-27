package info498d.uw.edu.smartalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    protected static String TAG = "**SmartAlarm.Main";
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
