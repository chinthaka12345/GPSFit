package com.ru.gpsfit.gpssensor;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ru.gpsfit.R;
import com.ru.gpsfit.app.AppController;
import com.ru.gpsfit.fitdata.FitData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chaminda on 17/06/19.
 */

public class GPSSensor extends Service implements LocationListener {

    static final String TAG = "GPSSensor";

    public LocationManager mLocationManager;
    int elapsedTime;
    Timer timer;
    FitData fitData;
    private final IBinder mBinder = new GPSBinder();


    public class GPSBinder extends Binder {
        public GPSSensor getService() {
            return GPSSensor.this;
        }
    }


    public IBinder onBind(Intent intent) {

        Log.d(TAG, "on bind");
        return mBinder;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "on create");

        initializeLocationManager();
        startTracking();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "unbind ");
        mLocationManager.removeUpdates(this);
        stopTimer();

        sendFitData();
        return super.onUnbind(intent);
    }



    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "rebind...");
        super.onRebind(intent);
    }



    @Override
    public void onDestroy() {

        Log.d(TAG, "destroy");
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.d(TAG, "Task removed");
        super.onTaskRemoved(rootIntent);
    }




    public void startTracking() {

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
            Log.d(TAG, "start location req");
            startTimer();
            AppController appController = (AppController) getApplicationContext();
            fitData = appController.getNewFitData();
        } catch (java.lang.SecurityException ex){
            // Already checked permission in Main Activity
//            Toast.makeText(this, "Do not have GPS permission", Toast.LENGTH_LONG).show();
        }
    }


    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    /**
     * send Location as broadcast into UI
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

        Intent local = new Intent();

        local.setAction(getResources().getString(R.string.LocationUpdateService));
        local.putExtra(getResources().getString(R.string.MessageTypes), getResources().getString(R.string.LocationMsg));

        try {
            this.sendBroadcast(local);
        } catch (java.lang.NullPointerException ex){
            Log.d(TAG, "got null, and ignore");
        }

        updateFitData(location);
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


    /**
     * Count elapsed time
     */
    public void startTimer(){
        timer = new Timer();
        int delay = 0;   // delay for 1 sec.
        int period = 1000;  // repeat every 60 sec.
        final TimerTask update = new TimerTask() {

            public void run() {
                sendElapsedTime();
                elapsedTime += 1;
            }
        };

        timer.scheduleAtFixedRate(update, delay, period);
    }


    /**
     * Stop elapsed time timer
     */
    private void stopTimer() {
        timer.cancel();
    }


    /**
     * Send elapsed time into UI thread
     */
    private void sendElapsedTime() {
        Intent local = new Intent();
        local.setAction(getResources().getString(R.string.LocationUpdateService));

        local.putExtra(getResources().getString(R.string.MessageTypes), getResources().getString(R.string.ElpasedMsg));
        local.putExtra(getResources().getString(R.string.ElapsedTime), elapsedTime);

        try {
            this.sendBroadcast(local);
        } catch (java.lang.NullPointerException ex){
            Log.d(TAG, "got null, and ignore");
        }
    }




    /**
     * Update FitData as Location receives
     * @param location
     */
    private void updateFitData(Location location) {
        fitData.updateByLocation(location);
    }


    /**
     * Send fitdata into main activity
     */
    private void sendFitData(){
        Intent local = new Intent();
        local.setAction(getResources().getString(R.string.LocationUpdateService));
        local.putExtra(getResources().getString(R.string.MessageTypes), getResources().getString(R.string.FitDataMsg));

        try {
            this.sendBroadcast(local);
        } catch (java.lang.NullPointerException ex){
            Log.d(TAG, "got null, and ignore");
        }
    }

}
