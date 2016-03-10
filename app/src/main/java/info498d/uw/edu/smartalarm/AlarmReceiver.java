package info498d.uw.edu.smartalarm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by kai on 3/8/16.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String TAG = "**AlarmReceiver";

    private NotificationManager alarmNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO: Our receiver plays all alarms it receives. We should have it check to make sure that
        // the alarm it receives is still relevant. We'll need to pass in an extra to the received
        // intent from AlarmService that includes the timestamp, so we can use it to compare to the
        // current timestamp and see if we should actually ring the alarm.
        // this will solve the issue where the alarm will ring when you first open the app
        String title = intent.getStringExtra(AlarmService.EXTRA_TITLE);
        long alarmTime = intent.getLongExtra(AlarmService.EXTRA_TIME, 0);
        long currentTime= System.currentTimeMillis();
        // time between alarm time and current time in seconds
        long delta = Math.abs(alarmTime - currentTime) / 1000;

        if (delta < 60) {

            alarmNotificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);

            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            long[] pattern = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};

            NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                    context).setContentTitle(title).setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText("This is an Alarm for" + title).setPriority(NotificationCompat.PRIORITY_MAX);


            alarmNotificationBuilder.setContentIntent(contentIntent);

            Notification notification = alarmNotificationBuilder.build();

            notification.sound = alarmUri;
            notification.vibrate = pattern;
            notification.flags = Notification.FLAG_INSISTENT;
            Log.v(TAG, "Alarm ringing!");
            alarmNotificationManager.notify(1, notification);
        } else {
            Log.v(TAG, "Did not ring alarm because delta > 60 seconds");
        }
    }
}
