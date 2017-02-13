package hookupandroid.activities;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import hookupandroid.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String notifications_new_message_ringtone = PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_new_message_ringtone", "ffs");

        Toast.makeText(this, "ringtone: " + notifications_new_message_ringtone, Toast.LENGTH_SHORT).show();

    }
}
