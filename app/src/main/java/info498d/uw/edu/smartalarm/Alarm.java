package info498d.uw.edu.smartalarm;

import android.provider.CalendarContract;
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
    boolean active;

    public Alarm() {
    }

    public Alarm(String alarmTitle, long timestamp, boolean active) {
        this.alarmTitle = alarmTitle;
        this.timestamp = timestamp;
        this.active = active;
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
        int minutes = calendar.get(Calendar.MINUTE);
        String amPM;
        if (calendar.get(Calendar.AM_PM) == 0) {
            amPM = "AM";
        } else {
            amPM = "PM";
            hour = hour - 12;
        }
        String time;
        time = "" + hour + ":";
        if (minutes < 10) {
            time += "0";
        }
        time += minutes;
        time += " " + amPM;
        return time;
    }

    public String getDay() {
        GregorianCalendar calendar = getDate();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch(day) {
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            default: return "";
        }
    }


    public static Alarm newDefaultInstance() {
        //Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        //int year = 2016; int month=3; int day=3; int hour=11; int minute=30; int second=31;
        //cal.set(year + 1900, month, day, hour, minute, second);
        Calendar cal = new GregorianCalendar();
        long datetime = cal.getTime().getTime();
        Alarm alarm = new Alarm("Wake up for class", datetime, false);
        alarm.save();
        return alarm;
    }
}
