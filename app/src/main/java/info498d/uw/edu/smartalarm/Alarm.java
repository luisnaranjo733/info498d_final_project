package info498d.uw.edu.smartalarm;

import android.provider.CalendarContract;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Alarm extends SugarRecord {
    public static final String TAG = "**Alarmdb";

    String alarmTitle;
    long timestamp; // local time in millis
    boolean active;

    public Alarm() {
    }


    /**
     * Create an alarm instance
     * Don't forget to call .save() to persist
     *
     * @param alarmTitle title of alarm
     * the following parameters will be stored as unix epoch UTC timestamp
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param active true for enabled alarm, false for disabled
     */
    public Alarm(String alarmTitle, int year, int month, int day, int hour, int minute,
                 boolean active) {
        this.alarmTitle = alarmTitle;
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, hour, minute);
        this.timestamp = cal.getTimeInMillis();
        this.active = active;
    }

    private Calendar getCal() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);
        return cal;
    }

    // full string representation of the date and time
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        return sdf.format(getCal().getTime());
    }

    // get string that represents the year
    public String getYear() {
        return "" + getCal().get(Calendar.YEAR);
    }

    // get just the time "7:30 AM"
    public String getTimeRepresentation() {
        String representation = "" + getCal().get(Calendar.HOUR) + ":" + getCal().get(Calendar.MINUTE) + " ";
        if (getCal().get(Calendar.AM_PM) == 0) {
            representation += "AM";
        } else {
            representation += "PM";
        }
        return representation;
    }

    // get the day "Monday" or "Tuesday"
    public String getDay() {
        int day = getCal().get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }

    }
}
