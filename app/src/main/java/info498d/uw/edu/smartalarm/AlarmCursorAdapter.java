package info498d.uw.edu.smartalarm;

import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView alarmTime = (TextView) view.findViewById(R.id.alarm_item_time);
        TextView alarmDay = (TextView) view.findViewById(R.id.alarm_item_day);
        TextView alarmTitle = (TextView) view.findViewById(R.id.alarm_item_title);
        Switch alarmSwitch = (Switch) view.findViewById(R.id.alarm_item_switch);

        // Extract properties from cursor
        String time = cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_TIME));
        String day = cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_DAY));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabase.AlarmEntry.COL_TITLE));

        // Populate fields with extracted properties
        alarmTime.setText(time);
        Log.v(TAG, "" + day + " vs " + String.valueOf(day));
        alarmDay.setText(String.valueOf(day));
        alarmTitle.setText(title);
    }
}
