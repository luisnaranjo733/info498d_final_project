package info498d.uw.edu.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by kartikey on 3/8/2016.
 */
public class ChargeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SLEEP_LOCATIONS", Context.MODE_PRIVATE);


    }
}
