package info498d.uw.edu.smartalarm;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by luis on 3/2/16.
 */
public class AlarmCursorAdapter extends CursorAdapter {
    public static final String TAG = "**Alarm.CursorAdapter";

    public AlarmCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Extract properties from cursor
        final int pk = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry._ID));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_TIME));
        String day = cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_DAY));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_TITLE));
        final int enabled = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_SWITCH));

        // Find fields to populate in inflated template
        TextView alarmTime = (TextView) view.findViewById(R.id.alarm_item_time);
        TextView alarmDay = (TextView) view.findViewById(R.id.alarm_item_day);
        TextView alarmTitle = (TextView) view.findViewById(R.id.alarm_item_title);
        Switch alarmSwitch = (Switch) view.findViewById(R.id.alarm_item_switch);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(TAG, "Toggle button pressed: " + pk);
                if (enabled == 0) {
                    Log.v(TAG, "Enable " + pk);
                    AlarmDatabase.updateAlarm(context, pk, 1);
                } else if (enabled == 1) {
                    Log.v(TAG, "Disable " + pk);
                    AlarmDatabase.updateAlarm(context, pk, 0);
                }

            }
        });


        // Populate fields with extracted properties
        alarmTime.setText(time);
        Log.v(TAG, "" + day + " vs " + String.valueOf(day));
        alarmDay.setText(String.valueOf(day));
        alarmTitle.setText(title);
        if (enabled == 1) {
            alarmSwitch.setChecked(true);
        } else if (enabled == 0){
            alarmSwitch.setChecked(false);
        }
    }
}
