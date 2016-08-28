package hookupandroid.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import hookupandroid.common.Constants;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/28/2016.
 */
public class UpdateHookupResponseTask extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... params) {

        String currentUserUID = params[0];
        String hookupPersonUID= params[1];


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("currentUserUID", currentUserUID)
                .add("hookupPersonUID", hookupPersonUID)
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/hookup/updateResponse")
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
