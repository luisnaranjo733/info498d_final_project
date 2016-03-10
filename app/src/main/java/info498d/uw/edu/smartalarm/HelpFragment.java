package info498d.uw.edu.smartalarm;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {


    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        TextView helpText = (TextView) rootView.findViewById(R.id.helpText);
        helpText.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        return rootView;
    }

}
