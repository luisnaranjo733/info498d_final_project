package info498d.uw.edu.smartalarm;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by luis on 3/3/16.
 */
@Table
public class Alarm extends SugarRecord {
    public static final String TAG = "**Alarmdb";

    String alarmTitle;
    long timestamp;

    public Alarm() {
    }

    public Alarm(String alarmTitle, long timestamp) {
        this.alarmTitle = alarmTitle;
        this.timestamp = timestamp;
    }

    public String toString() {
        return "Alarm: " + alarmTitle + " @ " + timestamp;
    }

    public GregorianCalendar getDate() {
        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }
}
