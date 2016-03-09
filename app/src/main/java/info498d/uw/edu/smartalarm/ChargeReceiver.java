package info498d.uw.edu.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by kartikey on 3/8/2016.
 */
public class ChargeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent != null) {

            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            int sec = 0;
            SharedPreferences sharedPreferences = context.getSharedPreferences("times", Context.MODE_PRIVATE);

            if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String bed_time = sp.getString("pref_key_bed_time","");

                if(!bed_time.toString().equals("")) {
                // TODO: CHECK BED TIME AND START SERVICE.
                }

                Toast.makeText(context, sharedPreferences.getString("last_time", "default"), Toast.LENGTH_SHORT).show();
            }


            /* If screen is on (action user present) && power is connected.:
             *      Take the time and update the last_time screen was unlocked.
             */
            if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
                Log.v("CHARGE RECEIVER", "SCREEN ON!!!");


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("last_time",hour+":"+min);
                editor.commit();

                Toast.makeText(context, "screen on!", Toast.LENGTH_LONG);
            }
        }

    }
}
