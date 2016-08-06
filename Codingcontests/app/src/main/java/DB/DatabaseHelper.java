package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {



    // Table Name
    public static final String TABLE_NAME = "CONTESTS";

    // Table columns
    public static final String _ID = "id";
    public static final String DURATION = "duration";
    public static final String END = "end";
    public static final String START = "start";
    public static final String EVENT = "event";
    public static final String HREF = "href";
    public static final String CONTEST_ID = "contest_id";
    public static final String RESOURCE_ID = "resource_id";
    public static final String RESOURCE_NAME = "resource_name";
    public static final String REMINDER = "reminder";


    // Database Information
    static final String DB_NAME = "CODINGCONTESTS.sqlite";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" +
            _ID +  " INTEGER PRIMARY KEY," +
            DURATION + " TEXT," +
            END + " TEXT,"+
            START + " TEXT,"+
            EVENT + " TEXT,"+
            HREF + " TEXT,"+
            CONTEST_ID + " TEXT NOT NULL UNIQUE,"+
            RESOURCE_ID + " TEXT,"+
            RESOURCE_NAME + " TEXT,"+
            REMINDER + " TEXT"+");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}