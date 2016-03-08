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
        String minutes = "";
        if (getCal().get(Calendar.MINUTE) < 10) {
            minutes += "0";
        }
        minutes += getCal().get(Calendar.MINUTE);

        String representation = "" + getCal().get(Calendar.HOUR) + ":" + minutes + " ";
        if (getCal().get(Calendar.AM_PM) == 0) {
            representation += "AM";
        } else {
            representation += "PM";
        }
        return representation;
    }

    // get the day "Monday" or "Tuesday"
    public String getDate() {
        Calendar cal = getCal();
        String date = "";

        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            date += "Sunday";
        } else if (day == 2) {
            date += "Monday";
        } else if (day == 3) {
            date += "Tuesday";
        } else if (day == 4) {
            date += "Wednesday";
        } else if (day == 5) {
            date += "Thursday";
        } else if (day == 6) {
            date += "Friday";
        } else if (day == 7) {
            date += "Saturday";
        }

        date += " ";

        // for conversion between 2016 to 16
        int year = cal.get(Calendar.YEAR);
        int lastPlace = year % 10; // save least significant digit from year
        year = year / 10; // remove least significant digit from year
        date += (cal.get(Calendar.MONTH) + 1) + "/" + (year % 10) + lastPlace;

        return date;

    }
}
