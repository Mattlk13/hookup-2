package hookupandroid.activities;

import android.annotation.TargetApi;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import hookupandroid.R;
import hookupandroid.tasks.UpdateUserLocationTask;

public class OldGeoLocActivity extends AppCompatActivity {

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_geo_loc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestTestLocation();

        Button btnGetMockLocation = (Button) findViewById(R.id.btnGetAndroidMockLocation);
        btnGetMockLocation.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                Location loc = new Location("Test");
                loc.setLatitude (45.251502);
                loc.setLongitude(19.875464);
                loc.setAltitude(0);
                loc.setAccuracy(10f);
                loc.setElapsedRealtimeNanos(System.nanoTime());
                loc.setTime(System.currentTimeMillis());
                mLocationManager.setTestProviderLocation("Test", loc);
            }
        });
    }

    private void requestTestLocation() {
        MyLocationListener myLocationListener = new MyLocationListener();

        myLocationListener = new MyLocationListener();
        if (mLocationManager.getProvider("Test") == null) {
            mLocationManager.addTestProvider("Test", false, false, false, false, false, false, false, 0, 1);
        }
        mLocationManager.setTestProviderEnabled("Test", true);
        mLocationManager.requestLocationUpdates("Test", 0, 0, myLocationListener);

        //        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng l) {
//                Location loc = new Location("Test");
//                loc.setLatitude(l.latitude);
//                loc.setLongitude(l.longitude);
//                loc.setAltitude(0);
//                loc.setAccuracy(10f);
//                loc.setElapsedRealtimeNanos(System.nanoTime());
//                loc.setTime(System.currentTimeMillis());
//                lm.setTestProviderLocation("Test", loc);
//            }
//        };
    }

    @TargetApi(17)
    private void getMockLocation()
    {
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        }
        mLocationManager.addTestProvider
                (
                        LocationManager.GPS_PROVIDER,
                        "requiresNetwork" == "",
                        "requiresSatellite" == "",
                        "requiresCell" == "",
                        "hasMonetaryCost" == "",
                        "supportsAltitude" == "",
                        "supportsSpeed" == "",
                        "supportsBearing" == "",
                        android.location.Criteria.POWER_LOW,
                        android.location.Criteria.ACCURACY_FINE
                );

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

//        newLocation.setLatitude (45.251502);
        newLocation.setLatitude (100.251502);
        newLocation.setLongitude(19.875464);
        newLocation.setAccuracy(500);
//        newLocation.setAccuracy(16F);
//        newLocation.setAltitude(0D);
        newLocation.setTime(System.currentTimeMillis());
        newLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
//        newLocation.setBearing(0F);

        mLocationManager.setTestProviderEnabled
                (
                        LocationManager.GPS_PROVIDER,
                        true
                );

        mLocationManager.setTestProviderStatus
                (
                        LocationManager.GPS_PROVIDER,
                        LocationProvider.AVAILABLE,
                        null,
                        System.currentTimeMillis()
                );

        mLocationManager.setTestProviderLocation
                (
                        LocationManager.GPS_PROVIDER,
                        newLocation
                );
    }

        private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // do whatever you want, scroll the map, etc.
            new UpdateUserLocationTask().execute(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
