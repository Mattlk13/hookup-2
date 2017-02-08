package hookupandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hookupandroid.R;
import hookupandroid.adapters.NavigationDrawerAdapter;
import hookupandroid.model.NavigationDrawerItem;

public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle   mDrawerToggle;
    private DrawerLayout            mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);

        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity(), getNavigationDrawerItems());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setUpDrawer(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // Do something of Slide of Drawer
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    private List<NavigationDrawerItem> getNavigationDrawerItems() {
        List<NavigationDrawerItem> list = new ArrayList<>();

        NavigationDrawerItem item = new NavigationDrawerItem();
        item.setTitle("Discover matches");
        item.setImageId(R.drawable.food);
        list.add(item);

        item = new NavigationDrawerItem();
        item.setTitle("Friends");
        item.setImageId(R.drawable.wc);
        list.add(item);

        item = new NavigationDrawerItem();
        item.setTitle("Pending hookups");
        item.setImageId(R.drawable.wc);
        list.add(item);

        item = new NavigationDrawerItem();
        item.setTitle("Settings");
        item.setImageId(R.drawable.wc);
        list.add(item);

        item = new NavigationDrawerItem();
        item.setTitle("Sign out");
        item.setImageId(R.drawable.wc);
        list.add(item);

        item = new NavigationDrawerItem();
        item.setTitle("Exit application");
        item.setImageId(R.drawable.wc);
        list.add(item);

        return list;
    }
}
