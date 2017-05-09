package matchbaker.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import matchbaker.common.Constants;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/21/2016.
 */
public class UpdateUserAuthToken extends AsyncTask<String, String, String> {

    private String serverUrl;

    public UpdateUserAuthToken() {
    }

    @Override
    protected String doInBackground(String... params) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String token = params[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + currentUser.getUid() + "/updateToken")
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