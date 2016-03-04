package info498d.uw.edu.smartalarm;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by kai on 2/27/16.
 */
public class AlarmListFragment extends Fragment {
    private static final String TAG = "**Alarm.AlarmListFrag";

    private OnAlarmSelectedListener callback;

    public interface OnAlarmSelectedListener {
        public void onAlarmSelected(Alarm alarm);
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

        if (container != null) {
            container.removeAllViews();
        }

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_alarm_list, container, false);


        List<Alarm> alarms = Alarm.listAll(Alarm.class);
        for (int i=0; i < alarms.size(); i++) {
            Alarm alarm = alarms.get(i);
            Log.v(TAG, "" + i + ": " + alarm.toString());
        }

        AlarmAdapter adapter = new AlarmAdapter(getActivity(), alarms);

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


        return rootView;
    }
}
