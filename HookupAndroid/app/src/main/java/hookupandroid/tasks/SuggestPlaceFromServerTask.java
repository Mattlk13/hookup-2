package hookupandroid.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.common.Constants;
import hookupandroid.model.FcmJsonData;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/25/2016.
 */
public class SuggestPlaceFromServerTask extends AsyncTask<String, String, String> {

    private String latitude;
    private String longitude;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public SuggestPlaceFromServerTask(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(String... params) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null) {
            return "";
        }

        String receiverUserId = params[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("latitude",latitude)
                .add("longitude",longitude)
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + currentUser.getUid() + "/suggestPlace/" + receiverUserId)
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
