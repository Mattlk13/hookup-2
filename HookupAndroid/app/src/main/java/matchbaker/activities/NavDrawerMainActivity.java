package matchbaker.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import matchbaker.R;
import matchbaker.common.FragmentTransitionUtils;
import matchbaker.common.UserUtils;
import matchbaker.common.enums.PersonRelation;
import matchbaker.fragments.AuthFragment;
import matchbaker.fragments.DiscoverMatchesFragment;
import matchbaker.fragments.EditProfileFragment;
import matchbaker.fragments.FriendsFragment;
import matchbaker.fragments.HomeFragment;
import matchbaker.fragments.PendingHookupsFragment;
import matchbaker.fragments.PersonalizationFragment;
import matchbaker.fragments.SignupFragment;
import matchbaker.fragments.ViewFriendProfileFragment;
import matchbaker.fragments.ViewNonFriendProfileFragment;
import matchbaker.fragments.ViewPendingProfileFragment;
import matchbaker.model.User;
import matchbaker.tasks.GetAllUserDataTask;

public class NavDrawerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AuthFragment.OnAuthFragmentInteractionListener,
        FriendsFragment.OnFriendsListFragmentInteractionListener, PendingHookupsFragment.OnPendingHookupInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SignupFragment.OnSignupFragmentInteractionListner, ViewPendingProfileFragment.OnViewPendingProfileInteractionListener,
        ViewFriendProfileFragment.OnViewFriendProfileInteractionListner, PersonalizationFragment.OnPersonalizationFragmentInteractionListener,
        EditProfileFragment.OnEditProfileListener {

    private final String VIEW_PROFILE_ACTION = "VIEW_PROFILE_ACTION";

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth auth;

    public static ArrayList<User> friends;
    public static ArrayList<User> pendingHookups;
    public static ArrayList<User> nonFriends;
    public static ArrayList<User> allUsers;
    public static User currentUser = null;

//    public static Context mApplicationContext;

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
//            try {
////                new GetAllUserDataTask(this, switchFragment.getView(), this).execute().get(); // freeze ui
//                new GetAllUserDataTask(this, null, this).execute().get(); // freeze ui
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }

            final Fragment homeFragment = homeFragmentTransition();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new GetAllUserDataTask(NavDrawerMainActivity.this, homeFragment.getView(), NavDrawerMainActivity.this).execute();
                }
            }, 1000);
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

        View headerview = navigationView.getHeaderView(0);

        Button editProfileBtn = (Button) headerview.findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser() != null) {
                    drawer.closeDrawer(GravityCompat.START);
                    Fragment editProfileFragment = new EditProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("current_user_profile", currentUser);
                    editProfileFragment.setArguments(bundle);
                    toolbar.setTitle("Edit profile");
                    FragmentTransitionUtils.to(editProfileFragment, NavDrawerMainActivity.this);
                }
                else {
                    Toast.makeText(NavDrawerMainActivity.this, "No user logged in...", Toast.LENGTH_SHORT).show();
                }
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
                User notificationUser = UserUtils.getUserFromMainActivityData(profileUID);

                int notificationId = intent.getIntExtra("notificationId", 0);
                mNotificationManager.cancel(notificationId);

                if(notificationUser != null) {
                    if(notificationUser.getPersonRelation() == PersonRelation.NON_FRIEND
                            || notificationUser.getPersonRelation() == PersonRelation.PENDING) {
                        if(intent.hasExtra("friends")) {
                            notificationUser.setPersonRelation(PersonRelation.FRIEND);
                            if(nonFriends.contains(notificationUser)) {
                                nonFriends.remove(notificationUser);
                            }
                            notificationUser.setFriendsDate(new Date());
                            friends.add(notificationUser);
                        }
                        onPersonViewClicked(notificationUser);
                    }
                }
                else {
                    homeFragmentTransition();
                }
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
        if (id == R.id.action_refresh) {
            if(auth.getCurrentUser() != null) {
                new GetAllUserDataTask(this, toolbar, this).execute();
            }
            else {
                Toast.makeText(this, "No user signed in ...", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(this, "Refresh button clicked!", Toast.LENGTH_SHORT).show();
        }

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
            if(nonFriends == null || nonFriends.size() == 0) {
                Toast.makeText(this, "Couldn't retrieve users data ...", Toast.LENGTH_SHORT).show();
            }
            else {
                switchFragment = new DiscoverMatchesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("non-friends", nonFriends);
                switchFragment.setArguments(bundle);
                switchToolbarTitle = "Discover matches";
            }
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
        currentUser = new User();
        currentUser.setProfileComplete(false);

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
        homeFragmentTransition();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                new GetFriendsTask().execute();
//                new GetPendingHookupsTask().execute();
                new GetAllUserDataTask(NavDrawerMainActivity.this, null, NavDrawerMainActivity.this).execute();
            }
        }, 2000);
    }

    private Fragment homeFragmentTransition() {
        Fragment frag = new HomeFragment();
        Bundle bundle = new Bundle();
//        bundle.putSerializable("user_profile_complete", currentUser.isProfileComplete());
        bundle.putSerializable("current_user_profile", currentUser);
        frag.setArguments(bundle);
        FragmentTransitionUtils.to(frag, this);

        return frag;
    }

    private void signoutUserAndReleaseData() {
        if(auth.getCurrentUser() != null) {
            auth.signOut();
            if(friends != null) {
                friends.clear();
            }
            if(pendingHookups != null) {
                pendingHookups.clear();
            }
            if(nonFriends != null) {
                nonFriends.clear();
            }
            currentUser = null;
        }
    }

    @Override
    public void onEditProfileDone() {
        toolbar.setTitle("Home");
        homeFragmentTransition();
    }

}
