package info498d.uw.edu.smartalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class NewAlarmActivity extends AppCompatActivity {

    public static final String TAG = "**Alarm.NewAlarm";

    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        Button datePickerBtn = (Button) findViewById(R.id.datePicker);
        Button timePickerBtn = (Button) findViewById(R.id.timePicker);
        Button setAlarmBtn = (Button) findViewById(R.id.setNewAlarm);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(v);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setAlarm(View v) {
        // if date and time have both been set, parse, create, and save new alarm
        if (datePickerFragment == null) {
            Toast.makeText(this, "You need to set the date first!", Toast.LENGTH_SHORT).show();
        } else if (timePickerFragment == null) {
            Toast.makeText(this, "You need to set the time first!", Toast.LENGTH_SHORT).show();
        } else {
            //String alarmTitle, long timestamp, boolean active
            Alarm alarm = new Alarm("ALARM", datePickerFragment.year, datePickerFragment.month,
                    datePickerFragment.day, timePickerFragment.hourOfDay,
                    timePickerFragment.minute, true);
            alarm.save();
            // then figure out how to tell the adapter in AlarmListFragment that the db has changed
            // http://developer.android.com/training/basics/intents/filters.html
            Intent result = new Intent();
            result.putExtra("test", true);
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        protected int year;
        protected int month;
        protected int day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.v(TAG, "Date set: " + month + " " + day);
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        protected int hourOfDay;
        protected int minute;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Log.v(TAG, "time set: " + hourOfDay + ": " + minute);
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }
    }
}
