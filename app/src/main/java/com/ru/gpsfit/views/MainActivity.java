package com.ru.gpsfit.views;

import android.Manifest.permission;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.ru.gpsfit.R;
import com.ru.gpsfit.app.AppController;
import com.ru.gpsfit.fitdata.FitData;
import com.ru.gpsfit.gpssensor.GPSSensor;
import com.ru.gpsfit.gpssensor.GPSSensor.GPSBinder;
import com.ru.gpsfit.utils.Conversions;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity{

    static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    GPSSensor mGpsSensor;
    BroadcastReceiver updateUIReceiver;
    boolean mBound = false;
    FitData fitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGPSFit();
    }


    @Override
    protected void onStart() {
        super.onStart();

        listenBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopListenBroadcast();
    }



    private void initGPSFit(){
        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        Fabric.with(this, new Crashlytics());
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLocationPermission()) {
                    startTracking();
                } else {
                    requestLocationPermission();
                }

            }
        });

        btnStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTrack();
            }
        });
    }



    private void startTracking() {

        bindGPSSensor();
    }

    private void stopTrack() {

        unbindGPSSensor();
    }


    private void bindGPSSensor(){
        Log.d(TAG, "binded " + mBound);
        Intent intent = new Intent(this, GPSSensor.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    private void unbindGPSSensor(){
        Log.d(TAG, "unbind GPS");
        if (mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }


    private boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "We do have permission for GPS");
            return true;
        } else {
            Log.d(TAG, "We don't have permission for GPS");
            return false;
        }

    }

    private void requestLocationPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "permission got");
                    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Got permission");
                        startTracking();
                    }

                } else {
                    Log.d(TAG, "No permissions");
                }
            }
        }
    }


    public void updateUI(Bundle bundle, Intent intent) {

        TextView tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        TextView tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        TextView tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        TextView tvElapsedTime = (TextView) findViewById(R.id.tvElapsedTime);

        String messageType = bundle.getString(getResources().getString(R.string.MessageTypes));
        if(messageType.contains(getString(R.string.LocationMsg))) {
            // Update Location information
            // Todo locale
            tvLongitude.setText(String.format(Locale.US, "%f", fitData.getCurrentPosition().getLongitude()));
            tvLatitude.setText(String.format(Locale.US, "%f", fitData.getCurrentPosition().getLatitude()));
            tvSpeed.setText(String.format(Locale.US, "%f", fitData.getmSpeed()));

        } else if(messageType.contains(getString(R.string.ElpasedMsg))){
            // Update elapsed time
            int elapsedTime = bundle.getInt(getResources().getString(R.string.ElapsedTime));
            tvElapsedTime.setText(String.format(Locale.US, "%s", Conversions.SecondsToString(elapsedTime)));

        } else if (messageType.contains(getString(R.string.FitDataMsg))){
            Intent trackIntent = new Intent(this, TrackInfoActivity.class);
            startActivity(trackIntent);
        } else{
            Log.d(TAG, "Got other");
        }

    }


    /***
     * Listen for broadcast messages from GPSSensor
     */
    private void listenBroadcast(){

        IntentFilter filter = new IntentFilter();
        filter.addAction(getResources().getString(R.string.LocationUpdateService));
        updateUIReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                updateUI(bundle, intent);
            }
        };
        registerReceiver(updateUIReceiver,filter);

    }

    /***
     * Stop listen for broadcast messages from GPSSensor
     */
    private void stopListenBroadcast(){
        if(updateUIReceiver != null) {
            unregisterReceiver(updateUIReceiver);
        }
    }


    private ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "GPSsensor service connected");

            GPSBinder binder = (GPSBinder) iBinder;
            mGpsSensor = binder.getService();
            mBound = true;

            AppController appController = (AppController) getApplicationContext();
            fitData = appController.getFitData();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "GPSsensor service  disconnected");
            mBound = false;
        }

    };



}
