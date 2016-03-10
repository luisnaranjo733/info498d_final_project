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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CheckLocationService extends Service{

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
        final SharedPreferences sharedPreferences = getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE);
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

                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    Log.v(TAG, "TEST current day: " + day);

                    int add = 0;

                    String bedTime = sharedPreferences.getString("bedTime", "");

                    int bedTimeHour = Integer.parseInt(bedTime.split(":")[0]);
                    int bedTimeMin = Integer.parseInt(bedTime.split(":")[1]);

                    if(hour >= bedTimeHour  && hour < 24)
                        add = 1;

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, add);
                    cal.set(Calendar.HOUR_OF_DAY, bedTimeHour);
                    cal.set(Calendar.MINUTE,bedTimeMin);

                    Log.v(TAG, "TEST next DAY: " + cal.get(Calendar.DAY_OF_MONTH));
                    Log.v(TAG, "TEST HOUR: "+ cal.get(Calendar.HOUR_OF_DAY));
                    Log.v(TAG, "TEST Min: "+ cal.get(Calendar.MINUTE));


                    long cms = c.getTimeInMillis();
                    long tms = cal.getTimeInMillis();
                    long diff = Math.abs(tms-cms);

                    Log.v(TAG,"DIFF: "+ diff);

                    checkAgainLater(context, diff);
                    // TODO: replace these values with calculated values

                    int year = 2016;
                    int month = 3;
                    int day = 9;
                    int hour = 10;
                    int minute = 30;
                    Alarm alarm = new Alarm("Smart Alarm", year, month, day, hour, minute, true);
                    alarm.save();
                    Intent saveThis = new Intent(MainActivity.getMainContext(), AlarmService.class);
                    saveThis.setAction(AlarmService.ACTION_CREATE);
                    saveThis.putExtra("id", alarm.getId());
                    saveThis.putExtra("title", alarm.alarmTitle);
                    saveThis.putExtra("timestamp", alarm.timestamp);
                    startService(saveThis);

                    Intent intent = new Intent("my-event");
                    // add data
                    intent.putExtra("id", alarm.getId());
                    LocalBroadcastManager.getInstance(CheckLocationService.this)
                            .sendBroadcast(intent);
                    Log.v(TAG, "Just send broadcast to update listview for alarm " + alarm.getId());
                } else {
                    Log.v(TAG, "check again later");
                    // TODO: if current time is equal to target hour of sleep + bedtime stop


                    checkAgainLater(context, 3000);

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

    private void checkAgainLater(final Context context, final long delayTime) {
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
                Log.v(TAG, "Delay time was " + delayTime + "ms");
                myLocation.getLocation(context, locationResult);
            }
        }, delayTime);

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
