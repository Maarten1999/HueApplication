package com.mpapps.hueapplication.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mpapps.hueapplication.Models.Bridge;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static DatabaseHandler sInstance = null;
    private DatabaseHandler(Context context)
    {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
    }

    public static void Detach(){
        sInstance = null;
    }

    public static DatabaseHandler getInstance(Context context){
        if (sInstance == null){
            sInstance = new DatabaseHandler(context);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_BRIDGES + "("
                + Util.KEY_NAME + " TEXT PRIMARY KEY, "
                + Util.KEY_USERNAME + " TEXT, "
                + Util.KEY_IP + " TEXT " + " )";
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        // Delete (drop) the old version of the table
        db.execSQL("DROP TABLE IF EXISTS " + Util.DB_NAME);

        // Create the new version of the table
        onCreate(db);
    }

    public void addBridge(Bridge bridge)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_NAME, bridge.getName());
        contentValues.put(Util.KEY_USERNAME, bridge.getUsername());
        contentValues.put(Util.KEY_IP, bridge.getIP());

        long returnValue = db.insertWithOnConflict(Util.TABLE_BRIDGES, null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.i("DatabaseHandler", String.valueOf(returnValue));
    }

    public Bridge getBridge(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Util.TABLE_BRIDGES,
                new String[]{Util.KEY_NAME, Util.KEY_USERNAME, Util.KEY_IP},
                Util.KEY_NAME + "= ?",
                new String[]{name},
                null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        return new Bridge(cursor.getString(0),
                cursor.getString(2),
                cursor.getString(1));
    }

    public List<Bridge> getAllBridges()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Bridge> bridges = new ArrayList<>();

        String selectAll = "SELECT * FROM " + Util.TABLE_BRIDGES;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Bridge bridge = new Bridge(cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(1));
                bridges.add(bridge);
            } while (cursor.moveToNext());
        }
        return bridges;
    }

    public void updateBridge(Bridge thisBridge)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_NAME, thisBridge.getName());
        contentValues.put(Util.KEY_USERNAME, thisBridge.getUsername());
        contentValues.put(Util.KEY_IP, thisBridge.getIP());

        long returnValue = db.update(Util.TABLE_BRIDGES,
                contentValues, Util.KEY_NAME + "= '" + thisBridge.getName() + "'", null );
        Log.i("DatabaseHandler", String.valueOf(returnValue));
    }
}
