package com.bevan.mongofit.MenuActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bevan.mongofit.R;

public class Quit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }
}
