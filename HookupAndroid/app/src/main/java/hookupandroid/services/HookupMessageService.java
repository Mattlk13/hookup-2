package hookupandroid.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import hookupandroid.MainActivity;
import hookupandroid.R;

/**
 * Created by Bandjur on 8/20/2016.
 */
public class HookupMessageService extends FirebaseMessagingService {

    private final String HOOKUP_REQUEST_YES_ACTION = "HOOKUP_REQUEST_YES_ACTION";
    private final String HOOKUP_REQUEST_NO_ACTION = "HOOKUP_REQUEST_NO_ACTION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String personName = remoteMessage.getData().get("personName");
        String hookupUserUID = remoteMessage.getData().get("hookupUserUID");

        pushNotification(personName, hookupUserUID);
    }

    private void pushNotification(String personName, String hookupUserUID) {
        int notificationID = UUID.randomUUID().toString().hashCode();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setContentTitle("Mmmm! New hook up request")
                .setContentText(personName + " is nearby. Would you like to accept " + personName + " as friend?")
                .setSmallIcon(R.drawable.heart)
                .setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        buildHookupRequestActions(builder, notificationID, hookupUserUID);

        manager.notify(notificationID,builder.build());
    }



    private void buildHookupRequestActions(NotificationCompat.Builder builder, int notificationId, String hookupUserUID) {
        int requestCode = notificationId;

        Intent yesReceive = new Intent();
        yesReceive.setAction(HOOKUP_REQUEST_YES_ACTION);
        Bundle yesBundle = new Bundle();
        yesBundle.putInt("notificationId", notificationId);
        yesBundle.putString("hookupUserUID", hookupUserUID);
        yesReceive.putExtras(yesBundle);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, requestCode, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, yesReceive, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(R.drawable.heart, "Yes", pendingIntentYes);

        Intent noReceive = new Intent();
        noReceive.setAction(HOOKUP_REQUEST_NO_ACTION);
        Bundle noBundle = new Bundle();
        noBundle.putInt("notificationId", notificationId);
        noReceive.putExtras(noBundle);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, requestCode, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, requestCode, noReceive, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(R.drawable.broken_heart, "No", pendingIntentNo);
    }
}
