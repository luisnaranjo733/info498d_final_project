package info498d.uw.edu.smartalarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        //setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: Get rid of this, only for testing purposes
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        cal.set(2016 + 1900, 3, 3, 11, 07);
        Long datetime = cal.getTime().getTime();
        Alarm alarm = new Alarm("Wake up for class", datetime);
        alarm.save();
    }
}
