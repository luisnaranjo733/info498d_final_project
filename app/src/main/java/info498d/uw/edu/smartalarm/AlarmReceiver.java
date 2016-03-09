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

/**
 * Created by kai on 3/8/16.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private NotificationManager alarmNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");

        alarmNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000,1000};

        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                context).setContentTitle(title).setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentText(title).setPriority(NotificationCompat.PRIORITY_MAX);


        alarmNotificationBuilder.setContentIntent(contentIntent);

        Notification notification = alarmNotificationBuilder.build();

        notification.sound = alarmUri;
        notification.vibrate = pattern;
        notification.flags = Notification.FLAG_INSISTENT;
        alarmNotificationManager.notify(1, notification);
    }
}
