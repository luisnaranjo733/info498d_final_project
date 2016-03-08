package info498d.uw.edu.smartalarm;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewAlarmFragment extends Fragment {
    private static final String TAG = "**Alarm.NewAlarmFrag";

    private OnNewAlarmListener callback;

    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;

    public interface OnNewAlarmListener {
        public void onShowDatePicker(DialogFragment dialogFragment);
        public void onShowTimePicker(DialogFragment dialogFragment);
        public void onNewAlarmSet(Alarm alarm);
    }

    public NewAlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            callback = (OnNewAlarmListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnSetNewAlarmListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_new_alarm, container, false);

        Button datePickerBtn = (Button) rootView.findViewById(R.id.datePicker);
        Button timePickerBtn = (Button) rootView.findViewById(R.id.timePicker);
        Button setAlarmBtn = (Button) rootView.findViewById(R.id.setNewAlarm);

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

        return rootView;
    }

    public void showDatePickerDialog(View v) {
        datePickerFragment = new DatePickerFragment();
        ((OnNewAlarmListener) getActivity()).onShowDatePicker(datePickerFragment);
    }

    public void showTimePickerDialog(View v) {
        timePickerFragment = new TimePickerFragment();
        // TODO: figure out how to show dialogFragment from a fragment
        ((OnNewAlarmListener) getActivity()).onShowTimePicker(timePickerFragment);
    }

    public void setAlarm(View v) {
        // if date and time have both been set, parse, create, and save new alarm
        if (datePickerFragment == null) {
            Toast.makeText(getActivity(), "You need to set the date first!", Toast.LENGTH_SHORT).show();
        } else if (timePickerFragment == null) {
            Toast.makeText(getActivity(), "You need to set the time first!", Toast.LENGTH_SHORT).show();
        } else {
            //String alarmTitle, long timestamp, boolean active
            Alarm alarm = new Alarm("ALARM", datePickerFragment.year, datePickerFragment.month,
                    datePickerFragment.day, timePickerFragment.hourOfDay,
                    timePickerFragment.minute, true);
            alarm.save();
            ((OnNewAlarmListener) getActivity()).onNewAlarmSet(alarm);
            // then figure out how to tell the adapter in AlarmListFragment that the db has changed

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

        public static NewAlarmActivity.DatePickerFragment newInstance() {
            NewAlarmActivity.DatePickerFragment datePicker = new NewAlarmActivity.DatePickerFragment();
            return datePicker;
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