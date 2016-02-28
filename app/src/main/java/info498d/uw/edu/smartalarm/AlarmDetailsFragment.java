package info498d.uw.edu.smartalarm;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kai on 2/27/16.
 */
public class AlarmDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (container != null) {
//            container.removeAllViews();
//        }

        final View rootView = inflater.inflate(R.layout.fragment_alarm_details, container, false);

        return rootView;
    }
}
