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
        int year = 2016; int month=3; int day=3; int hour=11; int minute=30; int second=31;
        cal.set(year + 1900, month, day, hour, minute, second);
        long datetime = cal.getTime().getTime();
        Alarm alarm = new Alarm("Wake up for class", datetime);
        alarm.save();
    }
}
