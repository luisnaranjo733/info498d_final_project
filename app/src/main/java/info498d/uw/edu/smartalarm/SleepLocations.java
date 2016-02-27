package info498d.uw.edu.smartalarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SleepLocations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_locations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
