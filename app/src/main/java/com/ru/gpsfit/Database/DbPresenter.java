package com.ru.gpsfit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.ru.gpsfit.Database.FitGPSDataContract.GPSEntry;
import com.ru.gpsfit.Database.FitGPSDataContract.TrackEntry;
import com.ru.gpsfit.fitdata.FitData;

/**
 * Created by chaminda on 17/06/20.
 */

public class DbPresenter {

    Context mContext;
    final static String TAG = "DbPresenter";

    public DbPresenter(Context context) {
        this.mContext = context;
    }

    /**
     * Write into Data base
     * @param fitData
     */
    public void writeFitData(FitData fitData) {
        FitGPSDbHelper mFitnessGPSDbHelper = new FitGPSDbHelper(mContext);

        // Gets the data repository in write mode
        SQLiteDatabase db = mFitnessGPSDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TrackEntry.COLUMN_NAME_TITLE, fitData.getmName());
//        values.put(TrackEntry.COLUMN_NAME_DESCRIPTION, fitData.getmDescription());
//        values.put(TrackEntry.COLUMN_NAME_TIME, fitData.getmTime());
//        values.put(TrackEntry.COLUMN_NAME_ELAPSED_TIME, fitData.getmElapsedTime());
//        values.put(TrackEntry.COLUMN_NAME_DISTANCE, fitData.getmDistance());

        long newRowId = db.insert(TrackEntry.TABLE_TRACKS, null, values);

        for(Location location: fitData.getmGPSData()){
            ContentValues gps_val = new ContentValues();
//            gps_val.put(GPSEntry.COLUMN_NAME_TRACK, fitData.getmName());
            gps_val.put(GPSEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
            gps_val.put(GPSEntry.COLUMN_NAME_LATITUDE, location.getLatitude());

            long newGPSId = db.insert(GPSEntry.TABLE_GPS, null, gps_val);

        }
    }


    /**
     * Read from database
     */
    public void readFitData() {
        FitGPSDbHelper mFitnessGPSDbHelper = new FitGPSDbHelper(mContext);
        SQLiteDatabase db = mFitnessGPSDbHelper.getReadableDatabase();

        // TODO , for all data
        String[] projection = {
                TrackEntry._ID,
                TrackEntry.COLUMN_NAME_TITLE
        };


        String[] projectionGPS = {
                GPSEntry._ID,
                GPSEntry.COLUMN_NAME_LATITUDE,
                GPSEntry.COLUMN_NAME_LONGITUDE

        };


        Cursor cursor = db.query(
                TrackEntry.TABLE_TRACKS,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Cursor cursorGps = db.query(
                GPSEntry.TABLE_GPS,                     // The table to query
                projectionGPS,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TrackEntry._ID));

            String title = cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_NAME_TITLE));
            Log.d(TAG, "titles " + title);

        }

        while(cursorGps.moveToNext()) {
            long itemId = cursorGps.getLong(
                    cursorGps.getColumnIndexOrThrow(GPSEntry._ID));

            Double title = cursorGps.getDouble(cursorGps.getColumnIndex(GPSEntry.COLUMN_NAME_LATITUDE));
            Log.d(TAG, "titles " + title);

        }

        cursor.close();
        cursorGps.close();

    }

}
