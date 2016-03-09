package info498d.uw.edu.smartalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


/**
 * Created by kai on 3/8/16.
 */
public class AlarmService extends IntentService {
    public static final String TAG = "**AlarmService";
    private NotificationManager alarmNotificationManager;

    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_CANCEL = "CANCEL";
    public static final String EXTRA_TITLE = "ALARM_TITLE";
    public static final String EXTRA_TIME = "ALARM_TIME";

    private IntentFilter matcher;
    private Long id;
    String title;

    public AlarmService() {
        super("AlarmService");
        matcher = new IntentFilter();
        matcher.addAction(ACTION_CREATE);
        matcher.addAction(ACTION_CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        long timeStamp = intent.getLongExtra("timestamp", 0);
        title = intent.getStringExtra("title");
        id = intent.getLongExtra("id", 0);


        if (matcher.matchAction(action)) {
            if (ACTION_CREATE.equals(action)) {
                execute(ACTION_CREATE, timeStamp, id);
            }

            if (ACTION_CANCEL.equals(action)) {
                execute(ACTION_CANCEL, 0, id);
            }
        }
    }

    private void execute(String action, long time, Long id) {
        Intent i;

        PendingIntent pi;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        i = new Intent(this, AlarmReceiver.class);
        i.putExtra(EXTRA_TITLE, title);
        i.putExtra(EXTRA_TIME, time);
        pi = PendingIntent.getBroadcast(this, id.intValue() , i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ACTION_CREATE.equals(action)) {
                am.set(AlarmManager.RTC_WAKEUP, time, pi);
                Log.v(TAG, "ALARM SET!!!");

        } else if (ACTION_CANCEL.equals(action)) {
            am.cancel(pi);
        }
    }
}
