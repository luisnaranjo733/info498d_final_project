package info498d.uw.edu.smartalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by kai on 3/8/16.
 */
public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    private IntentFilter matcher;
    private Long id;
    String title;

    public AlarmService() {
        super("AlarmService");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        long timeStamp = intent.getLongExtra("timestamp", 0);
        title = intent.getStringExtra("title");
        id = intent.getLongExtra("id", 0);


        if (matcher.matchAction(action)) {
            if (CREATE.equals(action)) {
                execute(CREATE, timeStamp, id);
            }

            if (CANCEL.equals(action)) {
                execute(CANCEL, 0, id);
            }
        }
    }

    private void execute(String action, long time, Long id) {
        Intent i;

        PendingIntent pi;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        i = new Intent(this, AlarmReceiver.class);
        i.putExtra("title", title);
        pi = PendingIntent.getBroadcast(this, id.intValue() , i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (CREATE.equals(action)) {
                am.set(AlarmManager.RTC_WAKEUP, time, pi);

        } else if (CANCEL.equals(action)) {
            am.cancel(pi);
        }
    }
}
