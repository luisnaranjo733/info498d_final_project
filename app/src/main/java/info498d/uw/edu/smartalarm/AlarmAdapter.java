package info498d.uw.edu.smartalarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    public static final String TAG = "**Alarm.Adapter";
    public AlarmAdapter(Context context, List<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Alarm alarm = getItem(position);

        Switch alarmSwitch;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
            alarmSwitch= (Switch) convertView.findViewById(R.id.alarm_item_switch);
            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v(TAG, "Clicked a toggle button" + isChecked);
                    Alarm alarm = getItem(position);
                    alarm.active = isChecked;
                    alarm.save();
                }
            });
        } else {
            alarmSwitch= (Switch) convertView.findViewById(R.id.alarm_item_switch);
        }

        // Find fields to populate in inflated template
        TextView alarmTime = (TextView) convertView.findViewById(R.id.alarm_item_time);
        TextView alarmDay = (TextView) convertView.findViewById(R.id.alarm_item_day);
        TextView alarmTitle = (TextView) convertView.findViewById(R.id.alarm_item_title);





        // Populate the data into the template view using the data object
        alarmTime.setText(alarm.getTime());
        alarmDay.setText(alarm.getDay());
        alarmTitle.setText(alarm.alarmTitle);
        alarmSwitch.setChecked(alarm.active);


        // Return the completed view to render on screen
        return convertView;
    }
}
