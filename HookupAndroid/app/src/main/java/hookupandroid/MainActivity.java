package hookup.hookupandroid;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hookup.hookupandroid.activities.NavDrawerExampleActivity;
import hookup.hookupandroid.activities.PersonRecyclerViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else { // For Below API 21
            // Implement this feature without material design
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Hook up");
        toolbar.setSubtitle("lets do dis!");

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                Intent intent = new Intent(MainActivity.this, PersonRecyclerViewActivity.class);
                startActivity(intent);
            }
        });

        Button btnGotoNavDrawActity = (Button) findViewById(R.id.btnGotoNavDrawActivity);

        btnGotoNavDrawActity.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                Intent intent = new Intent(MainActivity.this, NavDrawerExampleActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String toastMsg ="";

        switch(item.getItemId()){

            case R.id.discard:
                toastMsg = getString(R.string.delete);
                break;

            case R.id.search:
                toastMsg = getString(R.string.search);
                break;

            case R.id.edit:
                toastMsg = getString(R.string.edit);
                break;

            case R.id.settings:
                toastMsg = getString(R.string.settings);
                break;

            case R.id.Exit:
                toastMsg = getString(R.string.exit);
                break;
        }
        Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }


}
