package hookupandroid.tasks;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import hookupandroid.R;
import hookupandroid.common.Constants;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/25/2016.
 */
public class UpdateUserLocationTask extends AsyncTask<Location, String, String> {

//    private final String serverUrl = "http://192.168.0.14:8080";
//    private String serverUrl;

    public UpdateUserLocationTask(Context context) {
//        serverUrl = context.getString(R.string.server_url);
    }

    @Override
    protected String doInBackground(Location... params) {

//        Location location = params[0];

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null) {
            return "";
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("latitude", String.valueOf(params[0].getLatitude()))
                .add("longitude", String.valueOf(params[0].getLongitude()))
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + currentUser.getUid() + "/updateLocation")
//                .url(serverUrl + "/firebaseUser/" + currentUser.getUid() + "/updateLocation")
//                .url(serverUrl + "/firebaseUser/updateLocation")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            Log.d("async task http success", "http call with return code = " + code);
        } catch (IOException e) {
            Log.d("async task http fail", "http register user failed. " + e.getStackTrace());
            e.printStackTrace();
        }

        return "";
    }
}
