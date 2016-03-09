package info498d.uw.edu.smartalarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CheckLocationService extends Service {

    private static final String TAG = "CHECK_LOCATION_SERVICE";
    private MyLocation.LocationResult locationResult;
    private MyLocation myLocation;
    final Context context = this;
    final Handler handler = new Handler();

    BroadcastReceiver mReceiver;
    // use this as an inner class like here or as a top-level class
    public class MyReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sp = getSharedPreferences("GLOBAL", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                editor.putBoolean("charging", true);
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                editor.putBoolean("charging", false);
            }

            editor.commit();

        }

        // constructor
        public MyReceiver(){

        }
    }

    @Override
    public void onCreate() {
        // get an instance of the receiver in your service
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
    }

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
        int day = c.get(Calendar.DAY_OF_MONTH);
        Log.v(TAG, "this is the current time: " + hour + ":" + minute);
        // TODO: figure out when to start checking location

        locationResult = new MyLocation.LocationResult() {
            SharedPreferences sleepLocationPreferences = getSharedPreferences("SLEEP_LOCATIONS", Context.MODE_PRIVATE);
            String sleepLocations = sleepLocationPreferences.getString("sleepLocations", "");
            @Override
            public void gotLocation(Location location) {
                SharedPreferences sp = getSharedPreferences("GLOBAL", Context.MODE_PRIVATE);
                Boolean charging = sp.getBoolean("charging", false);
                Log.v(TAG, "current charging: " + charging);
                if (charging && currentLocationCheck(location.getLatitude(), location.getLongitude())) {
                    Log.v(TAG, "this person is sleeping");
                    // myLocation.cancelTimer();
                } else {
                    Log.v(TAG, "check again later");
                    // TODO: if current time is equal to target hour of sleep + bedtime stop
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);

                    checkAgainLater(context);

                }
            }
        };

        myLocation = new MyLocation();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3 seconds
                Log.v(TAG, "been after 3 seconds");
                myLocation.getLocation(context, locationResult);
            }
        }, 3000);
        // TODO: figure out how long delay should be
    }

    private void checkAgainLater(final Context context) {
        /*Looper.prepare();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3 seconds
                Log.v(TAG, "been after 5 seconds");
                myLocation.getLocation(context, locationResult);
            }
        }, 5000);*/
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3 seconds
                Log.v(TAG, "been after 3 seconds");
                myLocation.getLocation(context, locationResult);
            }
        }, 3000);

    }



    class LocationGetter extends TimerTask {

        @Override
        public void run() {
            myLocation.getLocation(context, locationResult);
        }
    }

    private boolean currentLocationCheck(double currLat, double currLong) {
        SharedPreferences sleepLocationPreferences = getSharedPreferences("SLEEP_LOCATIONS", Context.MODE_PRIVATE);
        String sleepLocations = sleepLocationPreferences.getString("sleepLocations", "");
        String[] sleepLocationsArray = sleepLocations.split(",");
        for (int i = 0; i < sleepLocationsArray.length; i++) {

            // Log.v(TAG, sleepLocationsArray[i]);
            String[] savedLocationArray = sleepLocationsArray[i].split(":");
            double savedLat = Double.parseDouble(savedLocationArray[0]);
            double savedLong = Double.parseDouble(savedLocationArray[1]);
            if ((currLat <= (savedLat + .001) && currLat >= (savedLat - .001)) &&
                    (currLong <= (savedLong + .001) && currLong >= (savedLong - .001))) {
                return true;
            }
        }

        return false;
    }
}
