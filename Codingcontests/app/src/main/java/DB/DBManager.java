package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }



    public void insert(String duration, String end,String start, String event,String href, String contest_id,String resource_id, String resource_name,String reminder_status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DURATION, duration);
        contentValue.put(DatabaseHelper.END, end);
        contentValue.put(DatabaseHelper.START, start);
        contentValue.put(DatabaseHelper.EVENT, event);
        contentValue.put(DatabaseHelper.HREF, href);
        contentValue.put(DatabaseHelper.CONTEST_ID, contest_id);
        contentValue.put(DatabaseHelper.RESOURCE_ID, resource_id);
        contentValue.put(DatabaseHelper.RESOURCE_NAME, resource_name);
        contentValue.put(DatabaseHelper.REMINDER, reminder_status);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DURATION, DatabaseHelper.END,DatabaseHelper.START, DatabaseHelper.EVENT
        ,DatabaseHelper.HREF, DatabaseHelper.CONTEST_ID,DatabaseHelper.RESOURCE_ID, DatabaseHelper.RESOURCE_NAME};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String duration, String end,String start, String event,String href, String contest_id,String resource_id, String resource_name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DURATION, duration);
        contentValues.put(DatabaseHelper.END, end);
        contentValues.put(DatabaseHelper.START, start);
        contentValues.put(DatabaseHelper.EVENT, event);
        contentValues.put(DatabaseHelper.HREF, href);
        contentValues.put(DatabaseHelper.CONTEST_ID, contest_id);
        contentValues.put(DatabaseHelper.RESOURCE_ID, resource_id);
        contentValues.put(DatabaseHelper.RESOURCE_NAME, resource_name);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.CONTEST_ID + " = " + contest_id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}