package hookupandroid.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import hookupandroid.R;
import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.common.CommonUtils;

/**
 * Created by Bandjur on 8/20/2016.
 */
public class HookupMessageService extends FirebaseMessagingService {

    private final String HOOKUP_REQUEST_YES_ACTION = "HOOKUP_REQUEST_YES_ACTION";
    private final String HOOKUP_REQUEST_NO_ACTION = "HOOKUP_REQUEST_NO_ACTION";
    private final String HOOKUP_REQUEST_DECIDE_LATER_ACTION = "HOOKUP_REQUEST_DECIDE_LATER_ACTION";
    private final String HOOKUP_REQUEST_VIEW_PROFILE_ACTION = "HOOKUP_REQUEST_VIEW_PROFILE_ACTION";

    private final String VIEW_PROFILE_ACTION = "VIEW_PROFILE_ACTION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String personName = remoteMessage.getData().get("personName");
        String hookupUserUID = remoteMessage.getData().get("hookupUserUID");
        boolean isRecommendedHookup = Boolean.parseBoolean(remoteMessage.getData().get("recommended"));

        if (remoteMessage.getData().containsKey("success_paired")) {
            notifySuccessFriends(personName, hookupUserUID);
        }
        else {
            notifyHookupUser(personName, hookupUserUID, isRecommendedHookup);
        }

//        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(NavDrawerMainActivity.notificationAudioPath));
//        mp.start();
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
        intent.setAction(VIEW_PROFILE_ACTION);
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

//        Intent viewProfileIntent = new Intent();
//        viewProfileIntent.setAction(HOOKUP_REQUEST_VIEW_PROFILE_ACTION);
//        Bundle viewProfileBundle = new Bundle();
//        viewProfileBundle.putInt("notificationId", notificationId);
//        viewProfileIntent.putExtras(viewProfileBundle);
//        PendingIntent pendingIntentViewProfile = PendingIntent.getBroadcast(this, requestCode, viewProfileIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.addAction(R.drawable.information_icon, "View Profile", pendingIntentViewProfile);
    }
}
