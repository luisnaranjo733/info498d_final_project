package info498d.uw.edu.smartalarm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by kai on 2/27/16.
 */
public class AlarmListFragment extends Fragment {
    private static final String TAG = "**Alarm.AlarmListFrag";
    public static final int ADD_ALARM_REQUEST = 1;

    private OnAlarmSelectedListener callback;
    public AlarmAdapter adapter;

    public interface OnAlarmSelectedListener {
        public void onAlarmSelected(Alarm alarm);
        public void onNewAlarmButtonPressed();
    }

    public AlarmListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            callback = (OnAlarmSelectedListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnAlarmSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_alarm_list, container, false);


        List<Alarm> alarms = Alarm.listAll(Alarm.class);
//        for (int i=0; i < alarms.size(); i++) {
//            Alarm alarm = alarms.get(i);
//            Log.v(TAG, "" + i + ": " + alarm.toString());
//        }

        if (adapter == null) {
            adapter = new AlarmAdapter(getActivity(), alarms);
        }

        //set the adapter
        AdapterView listView = (AdapterView) rootView.findViewById(R.id.alarm_list_view);
        listView.setAdapter(adapter);

        //set alarm item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "Item clicked!");
                Alarm alarm = (Alarm) parent.getItemAtPosition(position);
                ((OnAlarmSelectedListener)getActivity()).onAlarmSelected(alarm);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarm = (Alarm) parent.getItemAtPosition(position);
                Intent cancelThis = new Intent(MainActivity.getMainContext(), AlarmService.class);
                cancelThis.setAction(AlarmService.ACTION_CANCEL);
                cancelThis.putExtra("id", alarm.getId());
                getActivity().startService(cancelThis);
                alarm.delete();
                adapter.remove(alarm);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        Button addAlarm = (Button) rootView.findViewById(R.id.addAlarmButton);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    Log.v(TAG, "Created new alarm");
                }
                ((OnAlarmSelectedListener) getActivity()).onNewAlarmButtonPressed();

            }
        });

        return rootView;
    }

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
                        Log.v(TAG, "Clicked a toggle button " + isChecked);
                        Alarm alarm = getItem(position);
                        alarm.active = isChecked;
                        alarm.save();
                        if (isChecked) {
                            Intent saveThis = new Intent(MainActivity.getMainContext(), AlarmService.class);
                            saveThis.setAction(AlarmService.ACTION_CREATE);
                            saveThis.putExtra("id", alarm.getId());
                            saveThis.putExtra("title", alarm.alarmTitle);
                            saveThis.putExtra("timestamp", alarm.timestamp);
                            getActivity().startService(saveThis);
                            Log.v(TAG, "Toggle create alarm");
                        } else {
                            Intent cancelThis = new Intent(MainActivity.getMainContext(), AlarmService.class);
                            cancelThis.setAction(AlarmService.ACTION_CANCEL);
                            cancelThis.putExtra("id", alarm.getId());
                            getActivity().startService(cancelThis);
                            Log.v(TAG, "Toggle cancel alarm");
                        }
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

            alarmTime.setText(alarm.getTimeRepresentation());
            alarmDay.setText(alarm.getDate());
            alarmTitle.setText(alarm.alarmTitle);
            alarmSwitch.setChecked(alarm.active);


            // Return the completed view to render on screen
            return convertView;
        }
    }
}
