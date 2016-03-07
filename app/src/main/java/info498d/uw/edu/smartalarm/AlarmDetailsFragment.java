package info498d.uw.edu.smartalarm;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by kai on 2/27/16.
 */
public class AlarmDetailsFragment extends Fragment {


    TextView alarmTitle;
    TextView alarmTime;
    TextView alarmDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_alarm_details, container, false);

        Bundle bundle = getArguments();

        if (alarmTitle == null) {
            alarmTitle = (TextView) rootView.findViewById(R.id.alarmDescTitle);
        }
        if (alarmTime == null) {
            alarmTime = (TextView) rootView.findViewById(R.id.alarmDescTime);
        }
        if (alarmDate == null) {
            alarmDate = (TextView) rootView.findViewById(R.id.alarmDescDate);
        }

        final String title = bundle.getString("title");
        final String time = bundle.getString("time");
        final String date = bundle.getString("day");
        boolean active = bundle.getBoolean("active");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alarmTitle.setText(title);
                alarmTime.setText(time);
                alarmDate.setText(date);
            }
        });




        return rootView;
    }

}
