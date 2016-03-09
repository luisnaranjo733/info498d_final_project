package info498d.uw.edu.smartalarm;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    Button datePickerBtn;
    Button timePickerBtn;



    public interface OnNewAlarmListener {
        public void onShowDatePicker(DialogFragment dialogFragment);
        public void onShowTimePicker(DialogFragment dialogFragment);
        public void onDatePicked(DatePickerFragment datePickerFragment);
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

        datePickerBtn= (Button) rootView.findViewById(R.id.datePicker);
        timePickerBtn = (Button) rootView.findViewById(R.id.timePicker);
        Button setAlarmBtn = (Button) rootView.findViewById(R.id.setNewAlarm);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment = new DatePickerFragment();
                ((OnNewAlarmListener) getActivity()).onShowDatePicker(datePickerFragment);
                //datePickerBtn.setText(datePickerFragment.toString());
            }
        });

        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerFragment = new TimePickerFragment();
                ((OnNewAlarmListener) getActivity()).onShowTimePicker(timePickerFragment);
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

        private Button datePickerBtn;


        protected int year;
        protected int month;
        protected int day;

        @Override
        public String toString() {
            return "" + (month + 1) + "/" + day + "/" + year;
        }

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
            ((OnNewAlarmListener) getActivity()).onDatePicked(this);
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
