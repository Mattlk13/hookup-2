package hookupandroid.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.model.User;
import hookupandroid.tasks.UpdateUserLocationTask;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private static final String ARG_USER_PROFILE_COMPLETE = "user_profile_complete";
    private static final String ARG_CURRENT_USER_PROFILE = "current_user_profile";
    private boolean userProfileComplete;
    private User currentUser;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int PLACE_PICKER_REQUEST = 0x3;

    private View inflatedView;
    private Unbinder unbinder;

    @BindView(R.id.home_personalization_layout) LinearLayout personalizationLayout;
    @BindView(R.id.home_recommendations_layout) LinearLayout recommendationsLayout;
    @BindView(R.id.home_nearest_hookup_distance_text) TextView nearestHookupDistanceText;
    @BindView(R.id.home_recommended_persons_count_text) TextView recommendedPersonsCountText;

    private OnHomeFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(User currentUser) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//        args.putBoolean(ARG_USER_PROFILE_COMPLETE, userProfileComplete);
        args.putSerializable(ARG_CURRENT_USER_PROFILE, currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            userProfileComplete = getArguments().getBoolean(ARG_USER_PROFILE_COMPLETE);
            currentUser = (User) getArguments().getSerializable(ARG_CURRENT_USER_PROFILE);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
//                    .enableAutoManage(this, this)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        if(currentUser != null && !currentUser.isProfileComplete()) {
            personalizationLayout.setVisibility(View.VISIBLE);
            //            personalizationLayout.setVisibility(View.INVISIBLE);
        }
        else if(currentUser != null && currentUser.isProfileComplete()) {
            if(currentUser.getUnpairedRecommendationsCounter() > 0 ) {
                recommendationsLayout.setVisibility(View.VISIBLE);
                nearestHookupDistanceText.setText(Double.toString(currentUser.getNearestHookupDistnace()) + " km");
                recommendedPersonsCountText.setText(Integer.toString(currentUser.getUnpairedRecommendationsCounter()));
            }
        }

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_open_google_maps)
    public void openGoogleMaps() {
        if(mLastLocation != null) {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getContext().startActivity(intent);
        }
    }

    @OnClick(R.id.btn_set_user_mock_location)
    public void setUserLocation() {
        PlacePicker.IntentBuilder builder = null;
        if(mLastLocation != null) {
                        builder = new PlacePicker.IntentBuilder();
        }
        else {
            builder = new PlacePicker.IntentBuilder()
                    .setLatLngBounds(new LatLngBounds(new LatLng(45.252112, 19.797040), new LatLng(45.252112, 19.797040)));
        }

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
//                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        locationSettingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getContext());
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();

                    Location loc = new Location("Test");
                    loc.setLatitude (place.getLatLng().latitude);
                    loc.setLongitude(place.getLatLng().longitude);
                    loc.setAltitude(0);
                    loc.setAccuracy(10f);
                    loc.setElapsedRealtimeNanos(System.nanoTime());
                    loc.setTime(System.currentTimeMillis());
//                    LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, loc);
                    new UpdateUserLocationTask().execute(loc);
                }
        }
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
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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

    @OnClick(R.id.btn_personalization)
    public void openPersonalizationFragment() {
        mListener.onPersonalizationButtonClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mListener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // for wi-fi and tower settings
//        mLocationRequest.setInterval(30000);
//        mLocationRequest.setFastestInterval(6000);
        //        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // for GPS settings, precise location
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient,true);
        locationSettingsRequest();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
//        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        new UpdateUserLocationTask().execute(location);
        Toast.makeText(getContext(), "Current location: " + location.getLatitude() +
                ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
//        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHomeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPersonalizationButtonClicked();
    }
}
