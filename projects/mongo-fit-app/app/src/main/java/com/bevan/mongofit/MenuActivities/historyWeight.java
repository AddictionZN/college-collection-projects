package com.bevan.mongofit.MenuActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.Landing;
import com.bevan.mongofit.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class historyWeight extends AppCompatActivity {

    // getting the database helper
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    LineChart lineChart;
    EditText weightValue;
    Button enter;
    ArrayList<Entry> yValues = new ArrayList<>();

    String username;

    int countDays = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_weight);

        // getting an instance of db helper class
        dbHelper = new dbHelper(this);

        // getting the values of the resources
        lineChart = findViewById(R.id.lineGraph);
        weightValue = findViewById(R.id.etWeight);
        enter = findViewById(R.id.btnEnter);

        // giving the line graph a character and making it not zoomable
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(true);
        lineChart.setDescription(null);
        lineChart.setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        // getting the xAxis and changing to both sides and making the color white
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setTextColor(Color.WHITE);

        // getting the left and right y axis and changing the text color to white
        YAxis left = lineChart.getAxisLeft();
        left.setTextColor(Color.WHITE);

        YAxis right = lineChart.getAxisRight();
        right.setTextColor(Color.WHITE);


        // parsing the username value from the login page
        Intent i = getIntent();
        username = i.getStringExtra("username").trim();

        boolean checkUserInWeight = dbHelper.checkUserInWeightHistory(username);

        if (checkUserInWeight == true) {

            String[] getMaxDays = dbHelper.getWeightHistoryMax(username);
            countDays = Integer.parseInt(getMaxDays[0]);

            //Looping and graphing all the elements in the table of weights
            for (int a = 0; a < countDays; a++) {
                String[] getWeights = dbHelper.getWeightHistory(username, Integer.toString(a + 1));
                yValues.add(new Entry(Integer.parseInt(getWeights[0]), (float) Integer.parseInt(getWeights[1])));
            }

        } else {
            String[] weightAndHeight = dbHelper.getWeightAndHeight(username);
            String wei = weightAndHeight[1];

            if (!wei.contains(".")) {
                dbHelper.insertWeightHistory(username, "1", wei);
                countDays = 1;
                yValues.add(new Entry(1, (float) Integer.parseInt(wei)));
            } else {
                String adjustedValue = wei.substring(0, wei.indexOf("."));
                dbHelper.insertWeightHistory(username, "1", adjustedValue);
                countDays = 1;
                yValues.add(new Entry(1, (float) Integer.parseInt(adjustedValue)));
            }


        }

        // creating a data set for the line graph and add all the values from the database
        LineDataSet set1 = new LineDataSet(yValues, "Weight Progress");

        // changing the colour and text of the graph
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(8f);
        set1.setValueTextColor(Color.WHITE);

        // setting the graph data to the array list and then populating that list
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);

        // giving the data to the line graph
        lineChart.setData(data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(historyWeight.this, Landing.class);
        intent.putExtra("username", username);
        startActivity(intent);
        historyWeight.this.finish();
    }

    public void enterWeight(View view) {

        // getting the value from the EditText
        String value = weightValue.getText().toString().trim();

        // Counting the number of days and adding 1
        countDays++;

        if (value.equals("")) {
            Toast.makeText(this, "Please enter a weight in", Toast.LENGTH_SHORT).show();
            countDays--;
        } else {
            // Inserting it into the database
            dbHelper.insertWeightHistory(username, Integer.toString(countDays), value);

            // Creating an intent to close the page down once value has been entered in.
            Intent intent = new Intent(historyWeight.this, Landing.class);
            intent.putExtra("username", username);
            startActivity(intent);
            // closing down the weight history class
            historyWeight.this.finish();
            Toast.makeText(historyWeight.this, "Weight has been entered", Toast.LENGTH_SHORT).show();
        }


    }
}
