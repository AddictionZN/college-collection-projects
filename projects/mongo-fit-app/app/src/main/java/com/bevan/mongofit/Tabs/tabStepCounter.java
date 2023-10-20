package com.bevan.mongofit.Tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.R;
import com.bevan.mongofit.Tabs.StepCounter.StepDetector;
import com.bevan.mongofit.Tabs.StepCounter.StepListener;

import static android.content.Context.SENSOR_SERVICE;

public class tabStepCounter extends Fragment implements SensorEventListener, StepListener {

    // getting all the main components
    ProgressBar progressBar;
    TextView steps;
    TextView numberOfSteps;
    TextView distance;
    TextView caloriesBurnt;
    TextView maxSteps;

    com.bevan.mongofit.Databases.dbHelper dbHelper;

    // Declaring all the variables
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor stepCounter;
    int numSteps = 0;
    float totalCaloriesBurned = 0;
    float totalDistance = 0;
    int progress = 0;
    String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_stepcounter, container, false);
        steps = rootView.findViewById(R.id.tvStep);
        distance = rootView.findViewById(R.id.tvDistance);
        caloriesBurnt = rootView.findViewById(R.id.tvCaloriesBurnt);
        maxSteps = rootView.findViewById(R.id.tvMaxSteps);

        progressBar = rootView.findViewById(R.id.progressBar);
        numberOfSteps = rootView.findViewById(R.id.tvNumberSteps);

        dbHelper = new dbHelper(this.getActivity());

        Intent i = getActivity().getIntent();
        username = i.getStringExtra("username");

        // Checking if the user exists in the tblSteps table
        boolean check = dbHelper.checkUserInSteps(username);

        // if is true then get insert it into the database with the username and password else select it from the database
        if (check == true) {
            String[] getMaxSteps = dbHelper.getSteps(username);
            maxSteps.setText("/ " + getMaxSteps[0]);
            progressBar.setMax(Integer.parseInt(getMaxSteps[0]));

        } else {
            dbHelper.insertSteps(username, "10000");
            maxSteps.setText("/ 10000");
            progressBar.setMax(10000);
        }

        // Checking if the user exists in the tbStepsHistory table
        boolean checkHistory = dbHelper.checkUserInStepsHistory(username);
        if (checkHistory == true) {
            String[] getStepHistory = dbHelper.getStepHistory(username);
            steps.setText(getStepHistory[0] + "");
            numberOfSteps.setText(getStepHistory[0] + "");
            numSteps = Integer.valueOf(getStepHistory[0]);
            progress = Integer.valueOf(getStepHistory[0]);
            progressBar.setProgress(numSteps);
            distance.setText(getStepHistory[1] + "");
            caloriesBurnt.setText(getStepHistory[2] + "");
        } else {
            dbHelper.insertStepsHistory(username, "0", "0", "0");
            steps.setText("0");
            numberOfSteps.setText("0");
            distance.setText("0" + " M");
            caloriesBurnt.setText("0" + "");
            // setting the progress bar to the number of steps taken
            progressBar.setProgress(0);
        }

        // This is for the step counter and getting the accelerometer working
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);

        return rootView;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        dbHelper.updateStepsHistory(username, steps.getText().toString(), distance.getText().toString(), caloriesBurnt.getText().toString());
    }

    @Override
    public void stepCounter(long timeNs) {
        numSteps++;
        progress += 1;
        steps.setText("" + numSteps);
        numberOfSteps.setText("" + numSteps);
        progressBar.setProgress(progress);

        totalCaloriesBurned = numSteps * 0.05f;
        totalDistance = numSteps * 0.5f;

        distance.setText(totalDistance + " M");
        caloriesBurnt.setText(totalCaloriesBurned + "");
    }
}
