package hookupandroid.tasks;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.common.Constants;
import hookupandroid.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Bandjur on 8/21/2016.
 */
public class GetPendingHookupsTask extends AsyncTask<String, String, String> {

    private String serverUrl;

    public GetPendingHookupsTask() {
//        serverUrl = context.getString(R.string.server_url);
    }

    @Override
    protected String doInBackground(String... params) {
        ArrayList<User> users = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/pendingFriends")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            if (!json.isEmpty()) {
                Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                users = new Gson().fromJson(json, listType);
                NavDrawerMainActivity.pendingHookups = users;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }


}
