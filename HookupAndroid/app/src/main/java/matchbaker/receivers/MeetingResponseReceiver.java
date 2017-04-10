package matchbaker.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import matchbaker.tasks.UpdateMeetingResponseTask;

/**
 * Created by Bandjur on 3/29/2017.
 */

public class MeetingResponseReceiver extends BroadcastReceiver {

    private final String MEETING_OPEN_MAP_ACTION = "MEETING_OPEN_MAP_ACTION";
    private final String MEETING_ACCEPT_ACTION = "MEETING_ACCEPT_ACTION";
    private final String MEETING_DECLINE_ACTION = "MEETING_DECLINE_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String action = intent.getAction();

        Bundle notificationBundle = intent.getExtras();
        int notificationId = notificationBundle.getInt("notificationId");
        String hookupUserUID = notificationBundle.getString("hookupUserUID");

        if(MEETING_OPEN_MAP_ACTION.equals(action)) {
            String latitude = notificationBundle.getString("latitude");
            String longitude = notificationBundle.getString("longitude");

//            String uri = String.format(Locale.ENGLISH, "geo:%s,%s", latitude, longitude);
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
            Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            newIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
        else if(MEETING_ACCEPT_ACTION.equals(action)) {
            String meetingId = notificationBundle.getString("meetingId");
            new UpdateMeetingResponseTask(true, meetingId).execute();
            mNotificationManager.cancel(notificationId);
        }
        else if(MEETING_DECLINE_ACTION.equals(action)) {
            String meetingId = notificationBundle.getString("meetingId");
            new UpdateMeetingResponseTask(false, meetingId).execute();
            mNotificationManager.cancel(notificationId);
        }

    }
}
