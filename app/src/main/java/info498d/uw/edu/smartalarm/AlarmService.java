package info498d.uw.edu.smartalarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by kai on 3/8/16.
 */
public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("Wake up!");
    }

    private void sendNotification(String s) {
        Log.v("AlarmService", "Preparing to send notification...: " + s);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(s))
                .setContentText(s);


        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
