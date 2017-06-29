package com.ru.gpsfit.Database;

import android.provider.BaseColumns;

/**
 * Created by chaminda on 17/06/20.
 */

public class FitGPSDataContract {

    private FitGPSDataContract() {}

    public static class TrackEntry implements BaseColumns {
        public static final String TABLE_TRACKS = "tracks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_ELAPSED_TIME = "elapsed_time";
        public static final String COLUMN_NAME_DISTANCE = "distance";

    }

    public static class GPSEntry implements BaseColumns {
        public static final String TABLE_GPS = "gps_data";
        public static final String COLUMN_NAME_TRACK = "track";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_ACCURACY = "accuracy";
        public static final String COLUMN_NAME_ALTITUDE = "altitude";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_PROVIDER = "provider";
        public static final String COLUMN_NAME_BEARING = "bearing";

    }

}
