package info498d.uw.edu.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by kai on 2/27/16.
 */
public class AlarmDatabase {

    public AlarmDatabase(){}

    private static final String TAG = "AlarmDatabase";

    //defines scheme of the alarm
    public static abstract class AlarmEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String _ID = "_id";
        public static final String COL_TITLE = "TITLE";
        public static final String COL_TIME = "TIME";
        public static final String COL_DAY = "alarm";
        public static final String COL_SWITCH = "enabled";

    }


    private static final String CREATE_TABLE =
            "CREATE TABLE " + AlarmEntry.TABLE_NAME + "(" +
                    AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AlarmEntry.COL_TITLE + " TEXT" + "," +
                    AlarmEntry.COL_TIME + " TEXT"+ "," +
                    AlarmEntry.COL_DAY + " TEXT" + "," +
                    AlarmEntry.COL_SWITCH + " INTEGER" +
                    " )";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;

    public static class Helper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "message.db";
        private static final int DATABASE_VERSION = 2;

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }

    // adds dummy values to database to show dummy alarms
    public static void testDatabase(Context context) {
        addAlarm(context, "Class", "8:30 AM", "Monday", 1);
    }

    // method to add new alarms **not used yet**
    public static void addAlarm(Context context, String title, String time, String day, Integer enabled) {
        if (enabled != 0 || enabled != 1) {
            Log.v(TAG, "Invalid alarm input");
        }
        Helper helper = new Helper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(AlarmEntry.COL_TITLE, title);
        content.put(AlarmEntry.COL_TIME, time);
        content.put(AlarmEntry.COL_DAY, day);
        content.put(AlarmEntry.COL_SWITCH, enabled);
        try {
            long newRowId = db.insert(AlarmEntry.TABLE_NAME, null, content);
        }catch (SQLiteConstraintException e) {}
    }

    public static void updateAlarm(Context context, long rowId, int switchInt) {
        Helper helper = new Helper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(AlarmEntry.COL_SWITCH, switchInt);

        String selection = AlarmEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };

        try {
            db.update(AlarmEntry.TABLE_NAME, content, selection, selectionArgs);
        } catch (SQLiteConstraintException e) {}
    }

    public static Cursor queryDatabase(Context context) {
        Helper helper = new Helper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        String[] cols = new String[]{
                AlarmEntry._ID,
                AlarmEntry.COL_TITLE,
                AlarmEntry.COL_TIME,
                AlarmEntry.COL_DAY,
                AlarmEntry.COL_SWITCH
        };

        Cursor results = db.query(AlarmEntry.TABLE_NAME, cols, null, null, null, null, AlarmEntry._ID + " DESC");

        return results;
    }


}
