package matchbaker.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import matchbaker.common.Constants;
import matchbaker.model.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/25/2016.
 */
public class UpdateUserProfileTask extends AsyncTask<User, String, String> {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public UpdateUserProfileTask() {
    }

    @Override
    protected String doInBackground(User... params) {
        User user = params[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(user));

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/updateProfile")
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
