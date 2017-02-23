package hookupandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import hookupandroid.R;
import hookupandroid.common.FragmentTransitionUtils;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.fragments.AuthFragment;
import hookupandroid.fragments.DiscoverMatchesFragment;
import hookupandroid.fragments.FriendsFragment;
import hookupandroid.fragments.HomeFragment;
import hookupandroid.fragments.PendingHookupsFragment;
import hookupandroid.fragments.PersonalizationFragment;
import hookupandroid.fragments.SignupFragment;
import hookupandroid.fragments.ViewFriendProfileFragment;
import hookupandroid.fragments.ViewNonFriendProfileFragment;
import hookupandroid.fragments.ViewPendingProfileFragment;
import hookupandroid.model.Person;

public class NavDrawerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AuthFragment.OnAuthFragmentInteractionListener,
        FriendsFragment.OnFriendsListFragmentInteractionListener, PendingHookupsFragment.OnPendingHookupInteractionListener,
        PersonalizationFragment.OnPersonalizationFragmentInteractionListener, HomeFragment.OnHomeFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SignupFragment.OnSignupFragmentInteractionListner {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth auth;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_main);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        auth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment frag = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        String toolbarTitle = "Home";

        if (id == R.id.nav_home) {
            frag = new HomeFragment();
            toolbarTitle = "Home";
        }
        else if (id == R.id.nav_discover) {
            frag = new DiscoverMatchesFragment();
            toolbarTitle = "Discover matches";
        } else if (id == R.id.nav_friends) {
            frag=new FriendsFragment();
            toolbarTitle = "Friends";
        } else if (id == R.id.nav_pendingHookups) {
            frag=new PendingHookupsFragment();
            toolbarTitle = "Pending hookups";
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(NavDrawerMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signOut) {
            if(auth.getCurrentUser()!=null ) {
                // TODO: alert dialogue ... Are you sure you want to log out(Yes/No)
                auth.signOut();
                Toast.makeText(NavDrawerMainActivity.this, "Successfully signed out ...", Toast.LENGTH_SHORT).show();
                frag = new AuthFragment();
                toolbarTitle = "Authentification";
            }else {
                Toast.makeText(NavDrawerMainActivity.this, "No user signed in...", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_exitApp) {
            frag = new AuthFragment();
        }

        if(frag != null) {
            toolbar.setTitle(toolbarTitle);
            FragmentTransitionUtils.to(frag, this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRegisterButtonClicked() {
        toolbar.setTitle("Sign up");
        FragmentTransitionUtils.to(new SignupFragment(), this);
    }

    @Override
    public void onSuccessLogon() {
        toolbar.setTitle("Home");
        FragmentTransitionUtils.to(new HomeFragment(), this);
    }

    @Override
    public void onPersonViewClicked(Person item) {
        PersonRelation relation = item.getPersonRelation();

        Fragment frag = null;
        Bundle bundle = new Bundle();
        bundle.putSerializable("personData", item);

        switch (relation) {
            case FRIEND: {
                frag = new ViewFriendProfileFragment();
                break;
            }
            case NON_FRIEND: {
                frag = new ViewNonFriendProfileFragment();
                break;
            }
            case PENDING: {
                frag = new ViewPendingProfileFragment();
                break;
            }
        }

        frag.setArguments(bundle);

        FragmentTransitionUtils.to(frag, this);
    }

    @Override
    public void onPendingHookupItemClicked(Person item) {
        onPersonViewClicked(item);
    }

    @Override
    public void onPersonalizationButtonClicked() {
        toolbar.setTitle("Personalization");
        FragmentTransitionUtils.to(new PersonalizationFragment(), this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onSuccessRegistration() {
        toolbar.setTitle("Home");
        FragmentTransitionUtils.to(new HomeFragment(), this);
    }
    
}
