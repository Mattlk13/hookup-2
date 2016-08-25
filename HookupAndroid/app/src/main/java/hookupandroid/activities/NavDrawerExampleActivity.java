package hookupandroid.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hookupandroid.R;
import hookupandroid.adapters.NavigationDrawerAdapter;
import hookupandroid.fragments.NavigationDrawerFragment;
import hookupandroid.model.NavigationDrawerItem;

public class NavDrawerExampleActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_example);

        setUpToolbar();
//        setUpRecyclerView();
        setUpDrawer();

        Button btnMockLock = (Button) findViewById(R.id.btnShowMockLocActivity);
        btnMockLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavDrawerExampleActivity.this, MockLocationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Nav Draw Demo");
        toolbar.inflateMenu(R.menu.main_menu);
    }

    private void setUpDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUpDrawer(R.id.nav_drwr_fragment, drawerLayout, toolbar);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.drawerList);
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(this, getNavigationDrawerItems());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<NavigationDrawerItem> getNavigationDrawerItems() {
        List<NavigationDrawerItem> list = new ArrayList<>();

        NavigationDrawerItem item = new NavigationDrawerItem();
        item.setTitle("Krmacenje hranom");
        item.setImageId(R.drawable.food);
        list.add(item);

        item = new NavigationDrawerItem();
        item.setTitle("Yseravanje");
        item.setImageId(R.drawable.wc);
        list.add(item);

        return list;
    }
}
