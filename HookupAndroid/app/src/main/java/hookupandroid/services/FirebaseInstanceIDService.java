package hookupandroid.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import hookupandroid.tasks.UpdateUserAuthToken;

/**
 * Created by Bandjur on 8/20/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            String token = FirebaseInstanceId.getInstance().getToken();
            updateUserToken(token);
        }
    }

    private void updateUserToken(String token) {
//        new UpdateUserAuthToken(getApplicationContext()).execute(token);
    }
}
