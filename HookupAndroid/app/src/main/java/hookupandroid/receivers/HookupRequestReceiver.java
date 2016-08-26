package hookupandroid.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Bandjur on 8/26/2016.
 */
public class HookupRequestReceiver extends BroadcastReceiver {

    private final String YES_ACTION = "HOOKUP_REQUEST_YES_ACTION";
    private final String NO_ACTION = "HOOKUP_REQUEST_NO_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String action = intent.getAction();

        Bundle notificationBundle = intent.getExtras();
        int notificationId = notificationBundle.getInt("notificationId");

        if(YES_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed YES");
        } else if(NO_ACTION.equals(action)) {
            mNotificationManager.cancel(notificationId);
            Log.v("shuffTest","Pressed NO");
        }
    }
}
