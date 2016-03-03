package info498d.uw.edu.smartalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    public AlarmAdapter(Context context, List<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
        }

        // Find fields to populate in inflated template
        TextView alarmTime = (TextView) convertView.findViewById(R.id.alarm_item_time);
        TextView alarmDay = (TextView) convertView.findViewById(R.id.alarm_item_day);
        TextView alarmTitle = (TextView) convertView.findViewById(R.id.alarm_item_title);
        Switch alarmSwitch = (Switch) convertView.findViewById(R.id.alarm_item_switch);

        // Get the data item for this position
        Alarm alarm = getItem(position);


        // Populate the data into the template view using the data object
        alarmTime.setText("7:30 AM");
        alarmDay.setText("Monday");
        alarmTitle.setText(alarm.alarmTitle);
        alarmSwitch.setChecked(true);


        // Return the completed view to render on screen
        return convertView;
    }
}
