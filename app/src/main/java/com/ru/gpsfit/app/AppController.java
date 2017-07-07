package com.ru.gpsfit.app;

import android.app.Application;
import android.util.Log;

import com.ru.gpsfit.fitdata.FitData;

/**
 * Created by chaminda on 17/07/07.
 */

public class AppController extends Application {


    static final String TAG = "AppController";

    private FitData fitData;

    public FitData getFitData(){
        return fitData;
    }

    public FitData getNewFitData(){
        fitData = new FitData();
        return fitData;
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "Low memory");
        super.onLowMemory();
    }
}
