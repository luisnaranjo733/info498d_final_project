package info498d.uw.edu.smartalarm;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by luis on 3/3/16.
 */
@Table
public class Alarm extends SugarRecord {
    String alarmTitle;
    Long date;

    public Alarm() {
    }

    public Alarm(String alarmTitle, Long date) {
        this.alarmTitle = alarmTitle;
        this.date = date;
    }

    public String toString() {
        return "Alarm: " + alarmTitle + " @ " + date;
    }
}
