package info498d.uw.edu.smartalarm;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocation {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    private static final String TAG = "MY_LOCATION";
    // May change
    // private static final int WAIT_TIME = 1800000;
    private static final int WAIT_TIME = 10000;


    public boolean getLocation(Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled)
            return false;

        // try {
            if(gps_enabled)
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            if(network_enabled)
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        /*} catch (SecurityException e) {
            Log.e(TAG, e.toString());
        }
*/
        timer1=new Timer();
        timer1.schedule(new GetLastLocation(), WAIT_TIME);
        return true;
    }
    public void cancelTimer() {
        timer1.cancel();
        // try {
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);
        /*} catch (SecurityException e) {
            Log.v(TAG, e.toString());
        }*/

    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            // try {
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerNetwork);
            /*} catch (SecurityException e) {
                Log.e(TAG, e.toString());
            }*/

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            // try {
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerGps);
            /*} catch(SecurityException e) {
                Log.e(TAG, e.toString());
            }*/

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            Location net_loc=null, gps_loc=null;
            // try {
                lm.removeUpdates(locationListenerGps);
                lm.removeUpdates(locationListenerNetwork);
                if(gps_enabled)
                    gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(network_enabled)
                    net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           /* } catch (SecurityException e) {
                Log.e(TAG, e.toString());
            }
*/
            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if(gps_loc!=null){
                locationResult.gotLocation(gps_loc);
                return;
            }
            if(net_loc!=null){
                locationResult.gotLocation(net_loc);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }
}