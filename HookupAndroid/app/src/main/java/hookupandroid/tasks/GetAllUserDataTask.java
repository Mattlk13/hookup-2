package hookupandroid.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import hookupandroid.R;
import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.common.Constants;
import hookupandroid.fragments.AuthFragment;
import hookupandroid.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Bandjur on 3/20/2017.
 */

public class GetAllUserDataTask extends AsyncTask<String, String, String> {

    LinearLayout linlaHeaderProgress; // = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

    private Context mContext;
    private View mRootView;
    AuthFragment.OnAuthFragmentInteractionListener mListener;

    private ProgressDialog dialog = null;

    public GetAllUserDataTask(Context context, View rootView, AuthFragment.OnAuthFragmentInteractionListener listener){
        mContext = context;
        mRootView = rootView;
        mListener = listener;
        dialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        if(mContext instanceof AuthFragment.OnAuthFragmentInteractionListener) {
            if(mRootView != null) {
                LinearLayout linlaHeaderProgress = (LinearLayout) mRootView.findViewById(R.id.linlaHeaderProgress);
//                linlaHeaderProgress.setVisibility(View.VISIBLE);
                this.dialog.setMessage("Processing...");
                this.dialog.show();
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(mContext instanceof AuthFragment.OnAuthFragmentInteractionListener) {
            if(mRootView != null) {
                LinearLayout linlaHeaderProgress = (LinearLayout) mRootView.findViewById(R.id.linlaHeaderProgress);
//                linlaHeaderProgress.setVisibility(View.GONE);
                this.dialog.dismiss();
            }
            mListener.onSuccessLogon();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        getUserFriends();
        getUserPendingProfiles();
        getUserDetails();
        getNonFriends();

        return "";
    }


    private void getUserFriends() {
        ArrayList<User> users = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/friends")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            if (!json.isEmpty() && !json.startsWith("<!DOCTYPE html>")) {
                Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                users = new Gson().fromJson(json, listType);
                NavDrawerMainActivity.friends = users;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserPendingProfiles() {
        ArrayList<User> users = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/pendingFriends")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            if (!json.isEmpty() && !json.startsWith("<!DOCTYPE html>")) {
                Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                users = new Gson().fromJson(json, listType);
                NavDrawerMainActivity.pendingHookups = users;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserDetails() {
        User user = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .build();

        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            if (!json.isEmpty() && !json.startsWith("<!DOCTYPE html>")) {
                user = new Gson().fromJson(json, User.class);
                NavDrawerMainActivity.currentUser = user;
//                NavDrawerMainActivity.userProfileComplete = user.isProfileComplete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getNonFriends() {
        ArrayList<User> users = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.SERVER_URL + "/firebaseUser/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/non-friends")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            if (!json.isEmpty() && !json.startsWith("<!DOCTYPE html>")) {
                Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                users = new Gson().fromJson(json, listType);
                NavDrawerMainActivity.nonFriends = users;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
