package com.ru.gpsfit.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ru.gpsfit.Database.DbPresenter;
import com.ru.gpsfit.R;
import com.ru.gpsfit.app.AppController;
import com.ru.gpsfit.fitdata.FitData;

import java.util.Locale;

public class TrackInfoActivity extends AppCompatActivity {

    static final String TAG = "Track Activity";
    FitData fitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.track_title));
        // Todo : Serializable not work properly, could not get data from service.
//        FitData fitData = getIntent().getSerializableExtra(getResources().getString(R.string.FitData));

        initTrackActivity();
    }


    /**
     * Update UI
     * @param fitData
     */
    private void updateUI(FitData fitData){

        EditText etName = (EditText) findViewById(R.id.etName);
        TextView tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        TextView tvElapedTime = (TextView) findViewById(R.id.tvElapsedTime);
        TextView tvAveSpeed = (TextView) findViewById(R.id.tvAveSpeed);

        etName.setText(fitData.getmName());
        tvStartTime.setText(String.format(Locale.US, "%d", fitData.getmStartedTime()));
        tvElapedTime.setText(String.format(Locale.US, "%d", fitData.getmElapsedTime()));
        tvAveSpeed.setText(String.format(Locale.US, "%f", fitData.getmSpeed()));
    }


    /**
     * Init the activity
     */
    private void initTrackActivity(){

        AppController appController = (AppController) getApplicationContext();
        fitData = appController.getFitData();
        if(fitData != null) {
            updateUI(fitData);
        } else {
            Toast.makeText(this, getResources().getText(R.string.toast_no_track_data), Toast.LENGTH_LONG).show();
        }

        final Button btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
                btnSave.setEnabled(false);
            }
        });
    }


    /**
     * Save the track into database
     */
    private void saveTrack(){
        DbPresenter dbPresenter = new DbPresenter(this);
        dbPresenter.writeFitData(fitData);

        Log.d(TAG, "data saved");

        // TODO just for test data
//        dbPresenter.readFitData();
//        Log.d(TAG, "data saved, Reading. ");
    }
}
