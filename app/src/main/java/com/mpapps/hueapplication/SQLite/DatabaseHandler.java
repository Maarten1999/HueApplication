package com.mpapps.hueapplication.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    public DatabaseHandler(Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_BRIDGES + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY, " + Util.KEY_NAME + " TEXT, "
                + Util.KEY_IP + "DOUBLE" + " )";
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

    public void addBridge(Bridge bridge){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_NAME, bridge.getName());
        contentValues.put(Util.KEY_IP, bridge.getIP());

        db.insert(Util.TABLE_BRIDGES, null, contentValues);
    }

    public Bridge getBridge(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Util.TABLE_BRIDGES,
                new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_IP},
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor != null) cursor.moveToFirst();

        return new Bridge(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2));
    }

    public List<Bridge> getAllBridges(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Bridge> bridges = new ArrayList<>();

        String selectAll = "SELECT * FROM " + Util.TABLE_BRIDGES;
        Cursor cursor = db.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){
            do {
                Bridge bridge = new Bridge(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));
                bridges.add(bridge);
            } while(cursor.moveToNext());
        }
        return bridges;
    }
}
