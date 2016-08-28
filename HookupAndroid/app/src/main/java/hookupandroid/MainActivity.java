package hookupandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

import hookupandroid.activities.NavDrawerExampleActivity;
import hookupandroid.activities.PersonRecyclerViewActivity;
import hookupandroid.model.UserData;
import hookupandroid.tasks.RegisterUserTask;
import hookupandroid.tasks.UpdateUserAuthToken;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        auth = FirebaseAuth.getInstance();

        setUpToolbar();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonRecyclerViewActivity.class);
                startActivity(intent);
            }
        });

        final Button btnGotoNavDrawActity = (Button) findViewById(R.id.btnGotoNavDrawActivity);
        btnGotoNavDrawActity.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavDrawerExampleActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail!= null && !TextUtils.isEmpty(txtEmail.getText().toString())
                        && txtPassword != null && !TextUtils.isEmpty(txtPassword.getText().toString())) {
                    auth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("RESIGSTRATION SUCESS", "debug console write test");
                                Toast.makeText(MainActivity.this, "You have successfully registered ", Toast.LENGTH_SHORT).show();
                                String token = FirebaseInstanceId.getInstance().getToken();
                                RegisterUserTask registerAsyncTask = new RegisterUserTask(MainActivity.this);
                                UserData data = new UserData();
                                data.setEmail(txtEmail.getText().toString());
                                data.setUid(auth.getCurrentUser().getUid());
                                data.setLatitude(String.valueOf(mLastLocation.getLatitude()));
                                data.setLongitude(String.valueOf(mLastLocation.getLongitude()));
                                data.setToken(token);
                                registerAsyncTask.execute(data);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Registration failed. Try again...", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail!= null && !TextUtils.isEmpty(txtEmail.getText().toString())
                        && txtPassword != null && !TextUtils.isEmpty(txtPassword.getText().toString())) {


                    auth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "You have successfully logged in ", Toast.LENGTH_SHORT).show();
                                String token = FirebaseInstanceId.getInstance().getToken();
                                // TODO check if token equals token in SQLite
                                new UpdateUserAuthToken(MainActivity.this).execute(token);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Authentification failed. Try again...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()!=null) {
                    auth.signOut();
                    Toast.makeText(MainActivity.this, "Successfully signed out ...", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "No user signed in...", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Hook up");
        toolbar.setSubtitle("lets do dis!");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String toastMsg = "";

        switch (item.getItemId()) {

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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, String.valueOf(mLastLocation.getLatitude()) + " " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
