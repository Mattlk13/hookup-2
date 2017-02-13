package hookupandroid.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import hookupandroid.R;
import hookupandroid.tasks.UpdateUserLocationTask;

public class MockLocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int PLACE_PICKER_REQUEST = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_location);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(MockLocationActivity.this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }

        Button btnGetFusedLocation = (Button) findViewById(R.id.btnGetMockLocation);
        btnGetFusedLocation.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                Location loc = new Location("Test");
//                45.253244, 19.875299
//                loc.setLatitude (45.251502);
//                loc.setLongitude(19.875464);
                loc.setLatitude (45.253244);
                loc.setLongitude(19.875299);
                loc.setAltitude(0);
                loc.setAccuracy(10f);
                loc.setElapsedRealtimeNanos(System.nanoTime());
                loc.setTime(System.currentTimeMillis());

                LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, loc);
            }
        });

        Button btnFindPlace = (Button) findViewById(R.id.btnFindPlace);
        btnFindPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                // default should give start activity with current location but it doesn't
                //                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder()
                        .setLatLngBounds(new LatLngBounds(new LatLng(45.252112, 19.797040), new LatLng(45.252112, 19.797040)));
//                        .setLatLngBounds(new LatLngBounds(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));

                try {
                    startActivityForResult(builder.build(MockLocationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void locationSettingsRequest() {
        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MockLocationActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        locationSettingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                    Location loc = new Location("Test");
                    loc.setLatitude (place.getLatLng().latitude);
                    loc.setLongitude(place.getLatLng().longitude);
                    loc.setAltitude(0);
                    loc.setAccuracy(10f);
                    loc.setElapsedRealtimeNanos(System.nanoTime());
                    loc.setTime(System.currentTimeMillis());
                    LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, loc);
                }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // for wi-fi and tower settings
//        mLocationRequest.setInterval(30000);
//        mLocationRequest.setFastestInterval(6000);
        // for GPS settings, precise location
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
//        locationSettingsRequest();
        super.onStart();
    }

    protected void onStop() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient,true);
        locationSettingsRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, MockLocationActivity.this);
    }

    @Override
    public void onLocationChanged(Location location) {
        new UpdateUserLocationTask(MockLocationActivity.this).execute(location);
        Toast.makeText(MockLocationActivity.this, "Current location: " + location.getLatitude() +
                ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//        Snackbar.make(findViewById(R.id.drawer_layout), "Current location: " + location.getLatitude() +
//                ", " + location.getLongitude(), Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }
}
