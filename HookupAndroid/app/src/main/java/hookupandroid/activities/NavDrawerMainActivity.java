package hookupandroid.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.common.CommonUtils;
import hookupandroid.common.Constants;
import hookupandroid.common.FragmentTransitionUtils;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.fragments.AuthFragment;
import hookupandroid.fragments.DiscoverMatchesFragment;
import hookupandroid.fragments.EditProfileFragment;
import hookupandroid.fragments.FriendsFragment;
import hookupandroid.fragments.HomeFragment;
import hookupandroid.fragments.PendingHookupsFragment;
import hookupandroid.fragments.PersonalizationFragment;
import hookupandroid.fragments.SignupFragment;
import hookupandroid.fragments.ViewFriendProfileFragment;
import hookupandroid.fragments.ViewNonFriendProfileFragment;
import hookupandroid.fragments.ViewPendingProfileFragment;
import hookupandroid.fragments.personalizationFragmentPages.PsychologyPageFragment;
import hookupandroid.model.Person;
import hookupandroid.model.User;
import hookupandroid.tasks.GetAllUserDataTask;
import hookupandroid.tasks.GetFriendsTask;
import hookupandroid.tasks.GetPendingHookupsTask;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NavDrawerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AuthFragment.OnAuthFragmentInteractionListener,
        FriendsFragment.OnFriendsListFragmentInteractionListener, PendingHookupsFragment.OnPendingHookupInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SignupFragment.OnSignupFragmentInteractionListner, ViewPendingProfileFragment.OnViewPendingProfileInteractionListener,
        ViewFriendProfileFragment.OnViewFriendProfileInteractionListner, PersonalizationFragment.OnPersonalizationFragmentInteractionListener {

    private final String VIEW_PROFILE_ACTION = "VIEW_PROFILE_ACTION";

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth auth;

    public static ArrayList<User> friends;
    public static ArrayList<User> pendingHookups;
    public static ArrayList<User> nonFriends;
//    public static boolean userProfileComplete = false;
    public static User currentUser = null;

    private Fragment switchFragment;
    private String switchToolbarTitle;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.edit_profile_button) Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_main);
        Unbinder unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            switchFragment = new HomeFragment();
            FragmentTransitionUtils.to(switchFragment, this);
//            new GetAllUserDataTask(this, switchFragment.getView(), this).execute(); // don't freeze ui
            try {
                new GetAllUserDataTask(this, switchFragment.getView(), this).execute().get(); // freeze ui
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else {
            switchFragment = new AuthFragment();
            FragmentTransitionUtils.to(switchFragment, this);
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        if(auth.getCurrentUser() != null) {
//            switchFragment = new HomeFragment();
//            FragmentTransitionUtils.to(switchFragment, this);
//            new GetAllUserDataTask(this, switchFragment.getView(), this);
//        }
//        else {
//            switchFragment = new AuthFragment();
//            FragmentTransitionUtils.to(switchFragment, this);
//        }

//        LinearLayout header = (LinearLayout) findViewById(R.id.nav_header_container);
        View headerview = navigationView.getHeaderView(0);

        Button editProfileBtn = (Button) headerview.findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Fragment editProfileFragment = new EditProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("current_user_profile", currentUser);
                editProfileFragment.setArguments(bundle);
                toolbar.setTitle("Edit profile");
                FragmentTransitionUtils.to(editProfileFragment, NavDrawerMainActivity.this);
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        super.onNewIntent(intent);
        if(intent.getAction() == VIEW_PROFILE_ACTION ) {
            if (intent.hasExtra("profileUID")) {
                String profileUID = intent.getStringExtra("profileUID");
                Bundle bundle = new Bundle();
//                Person person = new Person();
//                person.setFirstname("NotificationTest");
//                person.setLastname("Testic");
//                bundle.putSerializable("personData", person);

                int notificationId = intent.getIntExtra("notificationId", 0);

                mNotificationManager.cancel(notificationId);

                Fragment nonFriendFragment = new ViewNonFriendProfileFragment();
                nonFriendFragment.setArguments(bundle);
                FragmentTransitionUtils.to(nonFriendFragment, this);
            }
        }
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switchFragment = null;
        switchToolbarTitle = "Home";
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {
            if(auth.getCurrentUser() != null) {
                switchFragment = new HomeFragment();
                Bundle bundle = new Bundle();
//                bundle.putSerializable("user_profile_complete", currentUser.isProfileComplete());
                bundle.putSerializable("current_user_profile", currentUser);
                switchFragment.setArguments(bundle);
            }
            else {
                switchFragment = new AuthFragment();
            }
            switchToolbarTitle = "Home";
        }
        else if (id == R.id.nav_discover) {
            switchFragment = new DiscoverMatchesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("non-friends", nonFriends);
            switchFragment.setArguments(bundle);
            switchToolbarTitle = "Discover matches";
        } else if (id == R.id.nav_friends) {
            switchFragment=new FriendsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("friends", friends);
            switchFragment.setArguments(bundle);
            switchToolbarTitle = "Friends";
        } else if (id == R.id.nav_pendingHookups) {
            switchFragment = new PendingHookupsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("pending-hookups", pendingHookups);
            switchFragment.setArguments(bundle);
            switchToolbarTitle = "Pending hookups";
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(NavDrawerMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signOut) {
            if(auth.getCurrentUser()!=null ) {
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                signoutUserAndReleaseData();
                                Toast.makeText(NavDrawerMainActivity.this, "Successfully signed out ...", Toast.LENGTH_SHORT).show();
                                switchFragment = new AuthFragment();
                                switchToolbarTitle = "Authentification";
                                toolbar.setTitle(switchToolbarTitle);
                                FragmentTransitionUtils.to(switchFragment, NavDrawerMainActivity.this);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }else {
                Toast.makeText(NavDrawerMainActivity.this, "No user signed in...", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_exitApp) {

            if(auth.getCurrentUser() != null) {
                signoutUserAndReleaseData();
            }
//            Process suProcess = null;
//            try {
//
//                suProcess = Runtime.getRuntime().exec("su");
//                DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
//                os.writeBytes("adb shell" + "\n");
//                os.flush();
//                os.writeBytes("am force-stop rs.androidhookup" + "\n");
//                os.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            ActivityCompat.finishAffinity(this);
        }

        if(switchFragment != null) {
            toolbar.setTitle(switchToolbarTitle);
            FragmentTransitionUtils.to(switchFragment, this);
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @OnClick(R.id.edit_profile_button)
//    public void OnEditProfileButtonClicked() {
//        // TODO: pass currentUser as bundle argument
//        FragmentTransitionUtils.to(new EditProfileFragment(), this);
//    drawer.closeDrawer(GravityCompat.START);
//
//    }



    @Override
    public void onRegisterButtonClicked() {
        toolbar.setTitle("Sign up");
        FragmentTransitionUtils.to(new SignupFragment(), this);
    }

    @Override
    public void onSuccessLogon() {
        toolbar.setTitle("Home");
        homeFragmentTransition();
    }

    @Override
    public void onSuccessRegistration() {
        toolbar.setTitle("Home");
        homeFragmentTransition();
//        FragmentTransitionUtils.to(new HomeFragment(), this);
    }

    @Override
    public void onPersonViewClicked(User item) {
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
    public void onPendingHookupItemClicked(User item) {
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
    public void onPendingHookupResponseAction(User pendingProfile) {
        pendingHookups.remove(pendingProfile);
        Fragment frag=new PendingHookupsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pending-hookups", pendingHookups);
        frag.setArguments(bundle);
        toolbar.setTitle("Pending hookups");
        FragmentTransitionUtils.to(frag, this, false);
        // TODO: add snackbar to notify user about action completion
    }

    @Override
    public void onFriendResponseAction(User friend) {
        friends.remove(friend);
        Fragment frag=new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("friends", friends);
        frag.setArguments(bundle);
        toolbar.setTitle("Friends");
        FragmentTransitionUtils.to(frag, this, false);
        // TODO: add snackbar to notify user about action completion
    }

    @Override
    public void onPersonalizationDone() {
        toolbar.setTitle("Home");

        currentUser.setProfileComplete(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new GetFriendsTask().execute();
                new GetPendingHookupsTask().execute();
            }
        }, 20000);

//        Fragment frag = new HomeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("user_profile_complete", currentUser.isProfileComplete());
//        frag.setArguments(bundle);
//        FragmentTransitionUtils.to(frag, this);
    }

    private void homeFragmentTransition() {
        Fragment frag = new HomeFragment();
        Bundle bundle = new Bundle();
//        bundle.putSerializable("user_profile_complete", currentUser.isProfileComplete());
        bundle.putSerializable("current_user_profile", currentUser);
        frag.setArguments(bundle);
        FragmentTransitionUtils.to(frag, this);
    }

    private void signoutUserAndReleaseData() {
        if(auth.getCurrentUser() != null) {
            auth.signOut();
            friends.clear();
            pendingHookups.clear();
        }
    }
}
