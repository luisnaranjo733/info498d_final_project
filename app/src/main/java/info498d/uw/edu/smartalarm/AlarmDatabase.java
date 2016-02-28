package info498d.uw.edu.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by kai on 2/27/16.
 */
public class AlarmDatabase {

    public AlarmDatabase(){}

    private static final String TAG = "AlarmDatabase";

    //defines schema
    public static abstract class AlarmEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String _ID = "_id";
        public static final String COL_TITLE = "TITLE";
        public static final String COL_TIME = "TIME";
        public static final String COL_DAY = "alarm";

    }


    private static final String CREATE_TABLE =
            "CREATE TABLE " + AlarmEntry.TABLE_NAME + "(" +
                    AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AlarmEntry.COL_TITLE + " TEXT" + "," +
                    AlarmEntry.COL_TIME + " TEXT"+ "," +
                    AlarmEntry.COL_DAY + " TEXT" +
                    " )";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;

    public static class Helper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "message.db";
        private static final int DATABASE_VERSION = 1;

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

    public static void testDatabase(Context context) {
        Helper helper = new Helper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(AlarmEntry.COL_TITLE, "Class");
        content.put(AlarmEntry.COL_TIME, "8:30 AM");
        content.put(AlarmEntry.COL_DAY, "Monday");

        try {
            long newRowId = db.insert(AlarmEntry.TABLE_NAME, null, content);
        }catch (SQLiteConstraintException e) {}

    }

    public static void addAlarm(Context context, String a, String m, String d) {
        Helper helper = new Helper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(AlarmEntry.COL_TITLE, a);
        content.put(AlarmEntry.COL_TIME, d);
        content.put(AlarmEntry.COL_DAY, m );
        try {
            long newRowId = db.insert(AlarmEntry.TABLE_NAME, null, content);
        }catch (SQLiteConstraintException e) {}
    }

    public static Cursor queryDatabase(Context context) {
        Helper helper = new Helper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        String[] cols = new String[]{
                AlarmEntry._ID,
                AlarmEntry.COL_TITLE,
                AlarmEntry.COL_TIME,
                AlarmEntry.COL_DAY};

        Cursor results = db.query(AlarmEntry.TABLE_NAME, cols, null, null, null, null, AlarmEntry._ID + " DESC");

        return results;
    }


}
