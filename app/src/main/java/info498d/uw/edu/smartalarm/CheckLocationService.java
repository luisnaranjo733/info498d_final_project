package info498d.uw.edu.smartalarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;

public class CheckLocationService extends Service {

    private static final String TAG = "CHECK_LOCATION_SERVICE";
    public CheckLocationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "service is started");
        super.onStartCommand(intent, flags, startId);
        startLocationCheck();
        return Service.START_STICKY;
    }

    private final IBinder mLocalBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        /*throw new UnsupportedOperationException("Not yet implemented");*/
        return mLocalBinder;
    }

    class LocalBinder extends Binder {
        public CheckLocationService getService() {
            return CheckLocationService.this;
        }

    }

    private void startLocationCheck() {
        Log.v(TAG, "starting location check");
        SharedPreferences sharedPreferences = getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE);
        Log.v(TAG, "this is the saved bed time: " + sharedPreferences.getString("bedTime", "09:00"));
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Log.v(TAG, "this is the current time: " + hour + ":" + minute);
        // TODO: figure out when to start checking location
        

    }
}
