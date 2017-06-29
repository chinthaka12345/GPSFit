package com.ru.gpsfit.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ru.gpsfit.Database.FitGPSDataContract.GPSEntry;
import com.ru.gpsfit.Database.FitGPSDataContract.TrackEntry;

/**
 * Created by chaminda on 17/06/20.
 */

public class FitGPSDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "FitnessGPS.db";

    //  Basic SQLs ====================================================
//  Create
    private static final String SQL_CREATE_TRACKS_TABLE =
            "CREATE TABLE " + TrackEntry.TABLE_TRACKS + " (" +
                    TrackEntry._ID + " INTEGER PRIMARY KEY," +
                    TrackEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TrackEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    TrackEntry.COLUMN_NAME_TIME + " TEXT" +
                    TrackEntry.COLUMN_NAME_ELAPSED_TIME + " TEXT," +
                    TrackEntry.COLUMN_NAME_DISTANCE + " TEXT)";

    private static final String SQL_CREATE_GPS_TABLE =
            "CREATE TABLE " + GPSEntry.TABLE_GPS + " (" +
                    GPSEntry._ID + " INTEGER PRIMARY KEY," +
                    GPSEntry.COLUMN_NAME_TRACK + " TEXT," +
                    GPSEntry.COLUMN_NAME_LONGITUDE + " REAL," +
                    GPSEntry.COLUMN_NAME_LATITUDE + " REAL," +
                    GPSEntry.COLUMN_NAME_ACCURACY + " INTEGER" +
                    GPSEntry.COLUMN_NAME_ALTITUDE + " INTEGER" +
                    GPSEntry.COLUMN_NAME_SPEED + " INTEGER" +
                    GPSEntry.COLUMN_NAME_PROVIDER + " TEXT" +
                    GPSEntry.COLUMN_NAME_BEARING + " TEXT)" ;


    //    Delete
    private static final String SQL_DELETE_TRACKS =
            "DROP TABLE IF EXISTS " + TrackEntry.TABLE_TRACKS;

    private static final String SQL_DELETE_GPS =
            "DROP TABLE IF EXISTS " + GPSEntry.TABLE_GPS;

//  Basic SQLs ====================================================



    public FitGPSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_TRACKS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TRACKS);
        sqLiteDatabase.execSQL(SQL_DELETE_GPS);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }


}
