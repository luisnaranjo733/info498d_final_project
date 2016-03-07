package info498d.uw.edu.smartalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SleepLocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final Context context = this;
    private static final String TAG = "SLEEP_LOCATION_ACTIVITY";
    String[] sleepLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_locations_map);
        SharedPreferences sharedPref = getSharedPreferences("SLEEP_LOCATIONS", Context.MODE_PRIVATE);
        String sleepLocations = sharedPref.getString("sleepLocations", "");
        /*SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sleepLocations", "");
        editor.commit();*/
        Log.v(TAG, sleepLocations);
        if (sleepLocations != "") {
            sleepLocationList = sleepLocations.split(",");
            // Log.v(TAG, sleepLocationList.toString());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sleep_locations_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMap.clear();
        SharedPreferences sharedPref = getSharedPreferences("SLEEP_LOCATIONS", Context.MODE_PRIVATE);
        // String sleepLocations = sharedPref.getString("sleepLocations", "");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sleepLocations", "");
        editor.commit();
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng uw = new LatLng(39.8282, -98.5795);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uw, 3));
        if (sleepLocationList != null && sleepLocationList.length > 0) {
            for (int i = 0; i < sleepLocationList.length; i++) {
                String latLngString = sleepLocationList[i];
                Log.v(TAG, latLngString);
                String[] latLngDoubleArray = latLngString.split(":");
                double lat = Double.parseDouble(latLngDoubleArray[0]);
                double lng = Double.parseDouble(latLngDoubleArray[1]);
                LatLng sleepLoc = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(sleepLoc).title("Sleep Location"));
            }
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng latLng) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Sleep Location"));
                                SharedPreferences sharedPref = getSharedPreferences("SLEEP_LOCATIONS", Context.MODE_PRIVATE);
                                String sleepLocs = sharedPref.getString("sleepLocations", "");
                                String newLatLng = latLng.latitude + ":" + latLng.longitude;
                                if (sleepLocs == "") {
                                    sleepLocs += newLatLng;
                                } else {
                                    sleepLocs += "," + newLatLng;
                                }

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("sleepLocations", sleepLocs);
                                editor.commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Log.v(TAG, "no clicked");
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Set as a sleep location?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }
}
