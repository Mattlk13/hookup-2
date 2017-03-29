package hookupandroid.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import hookupandroid.MainActivity;
import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.common.Constants;
import hookupandroid.common.security.NoSSLv3SocketFactory;
import hookupandroid.model.FcmJsonData;
import hookupandroid.model.User;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/25/2016.
 */
public class SendFcmMeetingPlaceTask extends AsyncTask<User, String, String> {

    private String latitude;
    private String longitude;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public SendFcmMeetingPlaceTask(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(User... params) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null) {
            return "";
        }

        User receiver = params[0];

        FcmJsonData fcmData = new FcmJsonData();
        fcmData.setTo(receiver.getFirebaseInstaceToken());
        Map<String, String> data = new HashMap<String, String>();
        data.put("personName", NavDrawerMainActivity.currentUser.getFirstname() + " "
                + NavDrawerMainActivity.currentUser.getLastname());
        data.put("hookupUserUID", currentUser.getUid());
        data.put("latitude", latitude);
        data.put("longitude", longitude);


        Request request =  null;
        OkHttpClient client = null;
        URL url = null;

        try {
            url = new URL(Constants.FCM_SERVER_URL);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);

//            client = new OkHttpClient();
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(NoSSLv3Factory)
                    .build();

            RequestBody body = RequestBody.create(JSON, new Gson().toJson(fcmData));
//            client.setSslSocketFactory(NoSSLv3Factory);

            request = new Request.Builder()
                    .url(Constants.FCM_SERVER_URL)
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "key=" + Constants.PROJECT_KEY)
                    .post(body)
                    .build();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

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
