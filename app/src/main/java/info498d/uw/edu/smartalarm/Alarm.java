package info498d.uw.edu.smartalarm;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


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
        return "Alarm: " + alarmTitle + " @ " + getDate().toString();
    }

    public GregorianCalendar getDate() {
        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    public String getTime() {
        GregorianCalendar calendar = getDate();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String amPM;
        if (calendar.get(Calendar.AM_PM) == 0) {
            amPM = "AM";
        } else {
            amPM = "PM";
            hour = hour - 12;
        }
        return "" + hour + ":" + minute + " " + amPM;
    }

    public static void newDefaultInstance() {
        //Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        //int year = 2016; int month=3; int day=3; int hour=11; int minute=30; int second=31;
        //cal.set(year + 1900, month, day, hour, minute, second);
        Calendar cal = new GregorianCalendar();
        long datetime = cal.getTime().getTime();
        Alarm alarm = new Alarm("Wake up for class", datetime);
        alarm.save();
    }
}
