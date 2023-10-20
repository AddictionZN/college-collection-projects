package com.bevan.mongofit.Tabs;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.R;
import com.bevan.mongofit.Tabs.StepCounter.StepDetector;
import com.bevan.mongofit.Tabs.StepCounter.StepListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

public class tabHome extends Fragment implements SensorEventListener, StepListener {

    // getting the database helper
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    ProgressBar barSteps;
    ProgressBar barCals;

    // Declaring the global variables
    private float[] yData = new float[2];
    private String[] xData = {"Steps", "Calories Burnt"};
    String username;

    int progress = 0;
    PieChart pieChart;

    int numSteps = 0;
    float calsBurnt = 0;
    int calories = 0;

    // Declaring all the variables
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor stepCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_home, container, false);

        // Calling the variables for the fields
        pieChart = rootView.findViewById(R.id.PieChart);
        barSteps = rootView.findViewById(R.id.pbSteps);

        barCals = rootView.findViewById(R.id.pbCal);

        barSteps.getProgressDrawable().setColorFilter(
                Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);

        barCals.getProgressDrawable().setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        // Creating the pie chart
        pieChart.setHoleRadius(25f);
        pieChart.setDescription(null);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.isUsePercentValuesEnabled();


        // getting the username parsed through the application
        Intent i = getActivity().getIntent();
        username = i.getStringExtra("username");

        // Getting the values from the database
        dbHelper = new dbHelper(this.getActivity());

        // Checking if the user exists in the tblSteps table
        boolean check = dbHelper.checkUserInSteps(username);
        // if is true then get insert it into the database with the username and password else select it from the database
        if (check == true) {
            String[] getMaxSteps = dbHelper.getSteps(username);
            barSteps.setMax(Integer.parseInt(getMaxSteps[0]));

        } else {
            dbHelper.insertSteps(username, "10000");
            barSteps.setMax(10000);
        }

        // Checking if the user exists in the tbStepsHistory table
        boolean checkHistory = dbHelper.checkUserInStepsHistory(username);
        if (checkHistory == true) {
            String[] getStepHistory = dbHelper.getStepHistory(username);
            numSteps = Integer.valueOf(getStepHistory[0]);
            progress = Integer.valueOf(getStepHistory[0]);
            calsBurnt = Float.valueOf(getStepHistory[2]);
            barSteps.setProgress(numSteps);

            yData[0] = numSteps;
            yData[1] = calsBurnt;

        } else {
            dbHelper.insertStepsHistory(username, "0", "0", "0");
        }

        // Checking if the user is in tblCalories table
        boolean checkCalories = dbHelper.checkUserInCalories(username);

        if (checkCalories == true) {
            String[] getMaxCalories = dbHelper.getCalories(username);
            barCals.setMax(Integer.parseInt(getMaxCalories[0]));

        } else {
            dbHelper.insertCalories(username, "3000");
            barCals.setMax(3000);
        }

        // Checking if the user is in the tblCaloriesHistory table
        boolean checkCalorieHistory = dbHelper.checkUserInCalorieHistory(username);
        if (checkCalorieHistory == true) {
            String[] getCalorieHistory = dbHelper.getCalorieHistory(username);
            barCals.setProgress(Integer.parseInt(getCalorieHistory[0]));
            calories = Integer.parseInt(getCalorieHistory[0]);

        } else {
            dbHelper.insertCalorieHistory(username, "0", "3000");
        }

        // calling the method of getting the data for the
        addDataSet();

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

    public void stepCounter(long timeNs) {
        numSteps++;
        progress += 1;
        calsBurnt = numSteps * 0.05f;
        barSteps.setProgress(progress);
        yData[0] = numSteps;
        yData[1] = calsBurnt;
        addDataSet();
    }

    public void addDataSet() {

        // Creating array lists for the data to be stored in the dataset
        ArrayList<PieEntry> yEntry = new ArrayList<>();

        // Adding the data to the data entries
        for (int x = 0; x < yData.length; x++) {
            yEntry.add(new PieEntry(yData[x], xData[x]));
        }

        //Creating the data set to link to the pie chart
        PieDataSet pieDataSet = new PieDataSet((yEntry), null);
        pieDataSet.setSliceSpace(6);
        pieDataSet.setValueTextSize(12);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(15f);

        // Getting the colors and storing it in an array list
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.CYAN);
        colours.add(Color.RED);

        // added it to the pie data
        pieDataSet.setColors(colours);

        // Set the pie data to the pie data set
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
