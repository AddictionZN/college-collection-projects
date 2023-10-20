package com.bevan.mongofit.Tabs;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.Landing;
import com.bevan.mongofit.MenuActivities.Settings;
import com.bevan.mongofit.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class tabCalories extends Fragment {

    // Declaring the variables
    TextView remain;
    TextView consumed;
    EditText enterCalories;
    Button enter;

    // Declaring the variables
    String username;
    int maxCalories = 0;
    int consumedCalories;
    int remainCalories;

    // For the pie chart
    private float[] yData = new float[2];
    private String[] xData = {"Remain", "Consumed"};
    PieChart pieChart;

    // getting the db helper
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_calories, container, false);

        // assigning the variables to the xml
        remain = rootView.findViewById(R.id.tvCaloriesRemain);
        consumed = rootView.findViewById(R.id.tvCaloriesConsumed);
        enterCalories = rootView.findViewById(R.id.etCalories);
        enter = rootView.findViewById(R.id.btnEnter);
        pieChart = rootView.findViewById(R.id.PieChart);

        dbHelper = new dbHelper(this.getActivity());

        Intent i = getActivity().getIntent();
        username = i.getStringExtra("username");

        // For the pie chart
        pieChart.setHoleRadius(25f);
        pieChart.setDescription(null);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.isUsePercentValuesEnabled();

        // Checking if the user exists in the tblCalories
        boolean checkCalories = dbHelper.checkUserInCalories(username);
        if (checkCalories == true) {
            String[] getMaxCalories = dbHelper.getCalories(username);
            maxCalories = Integer.parseInt(getMaxCalories[0]);
        } else {
            dbHelper.insertCalories(username, "3000");
            maxCalories = 3000;
        }

        // checking if the user exists in the tblCaloriesHistory
        boolean checkCaloriesHistory = dbHelper.checkUserInCalorieHistory(username);
        if (checkCaloriesHistory == true) {
            String[] getCalorieHistory = dbHelper.getCalorieHistory(username);

            consumedCalories = Integer.valueOf(getCalorieHistory[0]);
            remainCalories = maxCalories - consumedCalories;

            // if the remaining calories is greater than the max calories
            // then set it to 0 and don't update in the database
            consumed.setText(consumedCalories + "");
            remain.setText(remainCalories + "");
        } else {
            dbHelper.insertCalorieHistory(username, "0", Integer.toString(maxCalories));
            consumed.setText(0 + "");
            remain.setText(maxCalories + "");
        }

        yData[0] = remainCalories;
        yData[1] = consumedCalories;

        addDataSet();

        // Creating an event for the onclick button with the view
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = enterCalories.getText().toString().trim();

                // if the number is 0
                if (number.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a number", Toast.LENGTH_SHORT).show();
                } else {
                    // calories consumed is number + number of calories in the database
                    consumedCalories = consumedCalories + Integer.parseInt(number);
                    // remaining calories is the max calories - the number entered by the user
                    remainCalories = remainCalories - Integer.parseInt(number);

                    // if the remain of the calories is 0 then don't update it.
                    if (remainCalories <=0) {
                        remainCalories = 0;
                        dbHelper.updateCalorieHistory(username, Integer.toString(consumedCalories), Integer.toString(remainCalories));
                        remain.setText(remainCalories + "");
                        consumed.setText(consumedCalories + "");
                        enterCalories.setText("");
                    } else {
                        dbHelper.updateCalorieHistory(username, Integer.toString(consumedCalories), Integer.toString(remainCalories));
                        consumed.setText(consumedCalories + "");
                        remain.setText(remainCalories + "");

                        enterCalories.setText("");
                    }

                    // Adding the items to the pie chart
                    yData[0] = remainCalories;
                    yData[1] = consumedCalories;
                    addDataSet();
                }
            }
        });
        return rootView;
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
        colours.add(Color.GREEN);
        colours.add(Color.RED);

        // added it to the pie data
        pieDataSet.setColors(colours);

        // Set the pie data to the pie data set
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
