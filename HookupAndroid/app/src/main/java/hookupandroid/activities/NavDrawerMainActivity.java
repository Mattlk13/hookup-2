package hookupandroid.activities;

import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import hookupandroid.R;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.fragments.AuthFragment;
import hookupandroid.fragments.DiscoverMatchesFragment;
import hookupandroid.fragments.FriendsFragment;
import hookupandroid.fragments.HomeFragment;
import hookupandroid.fragments.PendingHookupsFragment;
import hookupandroid.fragments.SignupFragment;
import hookupandroid.fragments.ViewFriendProfileFragment;
import hookupandroid.fragments.ViewNonFriendProfileFragment;
import hookupandroid.fragments.ViewPendingProfileFragment;
import hookupandroid.model.Person;

public class NavDrawerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AuthFragment.OnAuthFragmentInteractionListener,
        FriendsFragment.OnFriendsListFragmentInteractionListener, PendingHookupsFragment.OnPendingHookupInteractionListener {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_main);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        } else if (id == R.id.nav_exitApp) {
            frag = new AuthFragment();
        }

        if(frag != null) {
            toolbar.setTitle(toolbarTitle);

            fragmentManager.beginTransaction()
                    .replace(R.id.content_nav_drawer_main, frag)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRegisterButtonClicked() {
        Fragment signupFragment = new SignupFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        toolbar.setTitle("Sign up");
        fragmentManager.beginTransaction()
                .replace(R.id.content_nav_drawer_main, signupFragment)
                .commit();
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_nav_drawer_main, frag)
                .commit();

    }

    @Override
    public void onPendingHookupItemClicked(Person item) {
        onPersonViewClicked(item);
    }
}
