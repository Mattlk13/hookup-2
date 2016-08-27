package hookupandroid.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import hookupandroid.R;
import hookupandroid.model.UserData;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/20/2016.
 */
public class RegisterUserTask extends AsyncTask<UserData, String, String> {

//    private final String serverUrl = "http://192.168.0.14:8080";
    private String mServerUrl;

    private Context mContext;

    // TODO stavi String kao ulazni parametar umesto UserData
    public RegisterUserTask(Context context) {
        mContext = context;
        mServerUrl = context.getString(R.string.server_url);
    }

    @Override
    protected String doInBackground(UserData... params) {

        UserData data = params[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email",data.getEmail())
                .add("uid",data.getUid())
                .add("token",data.getToken())
                .add("latitude",data.getLatitude())
                .add("longitude",data.getLongitude())
                .build();

        Request request = new Request.Builder()
                .url(mServerUrl + "/firebaseUser/add")
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
