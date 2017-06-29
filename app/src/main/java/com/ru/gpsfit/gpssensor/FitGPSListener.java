package com.ru.gpsfit.gpssensor;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;



/**
 * Created by chaminda on 17/06/20.
 */

public class FitGPSListener implements LocationListener {

    static final String TAG = "FitGPSListener";

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "location : " + location.getLatitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
