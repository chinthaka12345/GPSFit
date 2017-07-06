package com.ru.gpsfit.fitdata;

import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by chaminda on 17/06/19.
 */

public class FitData{

    static final String TAG = "FitData";
    private ArrayList<Location> mGPSData;

    @Nullable
    private String mName;
    private long mDistance;
    private long mElapsedTime;
    private long mStartedTime;
    private long mEndedTime;
    private double mAveSpeed;
    private Location currentPosition;

    public FitData() {
        this.mName = "My track";
        this.mDistance = 0;
        this.mElapsedTime = 0;
        this.mStartedTime = System.currentTimeMillis();
        this.mEndedTime = 0;
        this.mAveSpeed = 0;
        this.currentPosition = null;
        this.mGPSData = new ArrayList<>();
    }

    public ArrayList<Location> getmGPSData() {
        return mGPSData;
    }

    public void setmGPSData(ArrayList<Location> mGPSData) {
        this.mGPSData = mGPSData;
    }

    @Nullable
    public String getmName() {
        return mName;
    }

    public void setmName(@Nullable String mName) {
        this.mName = mName;
    }

    public long getmDistance() {
        return mDistance;
    }

    public long getmElapsedTime() {
        return mElapsedTime;
    }

    public void setmElapsedTime(long mElapsedTime) {
        this.mElapsedTime = mElapsedTime;
    }

    public long getmStartedTime() {
        return mStartedTime;
    }

    public void setmStartedTime(long mStartedTime) {
        this.mStartedTime = mStartedTime;
    }

    public double getmSpeed() {
        return mAveSpeed;
    }

    public void setmSpeed(long mAveSpeed) {
        this.mAveSpeed = mAveSpeed;
    }

    public long getmEndedTime() {
        return mEndedTime;
    }

    public void setmEndedTime(long mEndedTime) {
        this.mEndedTime = mEndedTime;
    }

    public Location getCurrentPosition(){
        return currentPosition;
    }

    public void updateByLocation(Location location) {

        if(currentPosition != null) {
            this.mDistance += location.distanceTo(currentPosition);
        }

        currentPosition = location;
        mGPSData.add(location);
    }

    public void finishTrack() {
        this.mEndedTime = System.currentTimeMillis();

        mElapsedTime = mEndedTime - mStartedTime;
        if ((mElapsedTime) > 0){
            mAveSpeed = (mDistance/mElapsedTime)*1000; // units m/s
            Log.d(TAG, "Speed " + mAveSpeed);
        } else {
            mElapsedTime = 0;
        }
    }


    // Dummy data
    public static FitData getDummyData(){

        FitData dummy = new FitData();

        dummy.mStartedTime = 2;
        dummy.mDistance = 5;
        dummy.mElapsedTime = 10;
        dummy.mName = "River side";
        dummy.mAveSpeed = 0.5;

        return dummy;
    }

}
