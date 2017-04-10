package matchbaker.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import matchbaker.R;
import matchbaker.activities.NavDrawerMainActivity;

/**
 * Created by Bandjur on 8/20/2016.
 */
public class HookupMessageService extends FirebaseMessagingService {

    private final String HOOKUP_REQUEST_YES_ACTION = "HOOKUP_REQUEST_YES_ACTION";
    private final String HOOKUP_REQUEST_NO_ACTION = "HOOKUP_REQUEST_NO_ACTION";
    private final String HOOKUP_REQUEST_DECIDE_LATER_ACTION = "HOOKUP_REQUEST_DECIDE_LATER_ACTION";
    private final String HOOKUP_REQUEST_VIEW_PROFILE_ACTION = "HOOKUP_REQUEST_VIEW_PROFILE_ACTION";

    private final String MEETING_OPEN_MAP_ACTION = "MEETING_OPEN_MAP_ACTION";
    private final String MEETING_ACCEPT_ACTION = "MEETING_ACCEPT_ACTION";
    private final String MEETING_DECLINE_ACTION = "MEETING_DECLINE_ACTION";

    private final String VIEW_PROFILE_ACTION = "VIEW_PROFILE_ACTION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String personName = remoteMessage.getData().get("personName");
        String hookupUserUID = remoteMessage.getData().get("hookupUserUID");


        if (remoteMessage.getData().containsKey("success_paired")) {
            notifySuccessFriends(personName, hookupUserUID);
        }
        else if (remoteMessage.getData().containsKey("hookup")) {
            boolean isRecommendedHookup = Boolean.parseBoolean(remoteMessage.getData().get("recommended"));
            notifyHookupUser(personName, hookupUserUID, isRecommendedHookup);
        }
        else if (remoteMessage.getData().containsKey("meetup")) {
            String longitude = remoteMessage.getData().get("longitude");
            String latitude = remoteMessage.getData().get("latitude");
            String meetingId = remoteMessage.getData().get("meetingId");
            notifyMeetingReceiver(personName,hookupUserUID, latitude, longitude, meetingId);
        }
        else if (remoteMessage.getData().containsKey("meetingResponse")) {
            String response = remoteMessage.getData().get("response");
            notifyUserAboutMeetingResponse(personName, response);
        }

//        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(NavDrawerMainActivity.notificationAudioPath));
//        mp.start();
    }

    private void notifyUserAboutMeetingResponse(String personName, String response) {
        int notificationID = UUID.randomUUID().toString().hashCode();
//        Intent intent = new Intent(this, NavDrawerMainActivity.class);
        String notifications_new_message_ringtone = PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_new_message_ringtone", "ffs");
        boolean isVibrateOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message_vibrate", false);
        Uri soundUri = Uri.parse(notifications_new_message_ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSound(soundUri);

        if(response.equals("true")) {
            builder.setContentTitle("You got a date!");
            builder.setContentText(personName + " accepted your meeting request.");
            builder.setSmallIcon(R.drawable.green_success_icon);
        }
        else {
            builder.setContentTitle("Date rejected");
            builder.setContentText(personName + " declined your meeting request.");
            builder.setSmallIcon(R.drawable.broken_heart_icon);
        }

        if(isVibrateOn) {
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notificationID,builder.build());
    }

    private void notifyMeetingReceiver(String personName, String hookupUserUID, String latitude, String longitude, String meetingId) {
        int notificationID = UUID.randomUUID().toString().hashCode();
        Intent intent = new Intent(this, NavDrawerMainActivity.class);
//        intent.setAction(VIEW_PROFILE_ACTION);
        intent.putExtra("profileUID", hookupUserUID);
        intent.putExtra("notificationId", notificationID);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("meetingId", meetingId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notifications_new_message_ringtone = PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_new_message_ringtone", "ffs");
        boolean isVibrateOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message_vibrate", false);
        Uri soundUri = Uri.parse(notifications_new_message_ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setContentTitle("New meeting request!")
                .setSmallIcon(R.drawable.location_32x32)
                .setContentText(personName + " invited you to meet up. ")
                .setContentIntent(pendingIntent)
                .setSound(soundUri);

        if(isVibrateOn) {
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        buildMeetingResponseActions(builder, notificationID, hookupUserUID, longitude, latitude, meetingId);
        manager.notify(notificationID,builder.build());
    }

    private void notifySuccessFriends(String personName, String hookupUserUID) {
        int notificationID = UUID.randomUUID().toString().hashCode();
        Intent intent = new Intent(this, NavDrawerMainActivity.class);
        intent.setAction(VIEW_PROFILE_ACTION);
        intent.putExtra("profileUID", hookupUserUID);
        intent.putExtra("notificationId", notificationID);
        intent.putExtra("friends", true);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notifications_new_message_ringtone = PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_new_message_ringtone", "ffs");
        boolean isVibrateOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message_vibrate", false);
        Uri soundUri = Uri.parse(notifications_new_message_ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setContentTitle("Congratulations!")
                .setSmallIcon(R.drawable.green_success_icon)
                .setContentText(personName + " and you have just become friends.")
                .setContentIntent(pendingIntent)
                .setSound(soundUri);

        if(isVibrateOn) {
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notificationID,builder.build());
    }

    private void notifyHookupUser(String personName, String hookupUserUID, boolean isRecommendedHookup) {
        int notificationID = UUID.randomUUID().toString().hashCode();
        Intent intent = new Intent(this, NavDrawerMainActivity.class);
//        intent.setAction(VIEW_PROFILE_ACTION);
        intent.putExtra("profileUID", hookupUserUID);
        intent.putExtra("notificationId", notificationID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notifications_new_message_ringtone = PreferenceManager.getDefaultSharedPreferences(this).getString("notifications_new_message_ringtone", "ffs");
        boolean isVibrateOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message_vibrate", false);
        Uri soundUri = Uri.parse(notifications_new_message_ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
//                .setContentTitle("Mmmm! New hook up request")
                .setContentIntent(pendingIntent)
                .setContentText("Would you like to accept " + personName + " as friend?")
                .setSound(soundUri);

        if(isVibrateOn) {
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }

        if(isRecommendedHookup) {
            builder.setSmallIcon(R.drawable.red_two_hearts);
            builder.setContentTitle(personName + "(recommended) is nearby.");
//            builder.setContentText(personName + "(recommended) is nearby. Would you like to accept " + personName + " as friend?");
//            builder.setContentText("Would you like to accept " + personName + " as friend?");
        }
        else{
            builder.setSmallIcon(R.drawable.heart);
//            builder.setContentText(personName + " is nearby. Would you like to accept " + personName + " as friend?");
            builder.setContentTitle(personName + " liked you.");
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        buildHookupRequestActions(builder, notificationID, hookupUserUID);

        manager.notify(notificationID,builder.build());
    }

    private void buildHookupRequestActions(NotificationCompat.Builder builder, int notificationId, String hookupUserUID) {
        int requestCode = notificationId;

        Intent yesIntent = new Intent();
        yesIntent.setAction(HOOKUP_REQUEST_YES_ACTION);
        Bundle yesBundle = new Bundle();
        yesBundle.putInt("notificationId", notificationId);
        yesBundle.putString("hookupUserUID", hookupUserUID);
        yesIntent.putExtras(yesBundle);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, requestCode, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.heart, "Yes", pendingIntentYes);

        Intent noIntent = new Intent();
        noIntent.setAction(HOOKUP_REQUEST_NO_ACTION);
        Bundle noBundle = new Bundle();
        noBundle.putInt("notificationId", notificationId);
        noIntent.putExtras(noBundle);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, requestCode, noIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.broken_heart_icon, "No", pendingIntentNo);

        Intent decideLaterIntent = new Intent();
        decideLaterIntent.setAction(HOOKUP_REQUEST_DECIDE_LATER_ACTION);
        Bundle decideLaterBundle = new Bundle();
        decideLaterBundle.putInt("notificationId", notificationId);
        decideLaterIntent.putExtras(decideLaterBundle);
        PendingIntent pendingIntentDecideLater = PendingIntent.getBroadcast(this, requestCode, decideLaterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.question_mark_icon, "Later", pendingIntentDecideLater);

    }

    private void buildMeetingResponseActions(NotificationCompat.Builder builder, int notificationId,
                                             String hookupUserUID, String longtitude,
                                             String latitude, String meetingId) {
        int requestCode = notificationId;

        Intent mapIntent = new Intent();
        mapIntent.setAction(MEETING_OPEN_MAP_ACTION);
        Bundle mapBundle = new Bundle();
        mapBundle.putInt("notificationId", notificationId);
        mapBundle.putString("longitude", longtitude);
        mapBundle.putString("latitude", latitude);
        mapIntent.putExtras(mapBundle);
        PendingIntent pendingIntentMap = PendingIntent.getBroadcast(this, requestCode, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.location_32x32, "View map", pendingIntentMap);

        Intent yesIntent = new Intent();
        yesIntent.setAction(MEETING_ACCEPT_ACTION);
        Bundle yesBundle = new Bundle();
        yesBundle.putInt("notificationId", notificationId);
        yesBundle.putString("hookupUserUID", hookupUserUID);
        yesBundle.putString("meetingId", meetingId);
        yesIntent.putExtras(yesBundle);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, requestCode, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.heart, "Yes", pendingIntentYes);

        Intent noIntent = new Intent();
        noIntent.setAction(MEETING_DECLINE_ACTION);
        Bundle noBundle = new Bundle();
        noBundle.putInt("notificationId", notificationId);
        noBundle.putString("hookupUserUID", hookupUserUID);
        noBundle.putString("meetingId", meetingId);
        noIntent.putExtras(noBundle);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, requestCode, noIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.broken_heart_icon, "No", pendingIntentNo);


    }
}
