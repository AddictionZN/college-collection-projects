package com.bevan.mongofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bevan.mongofit.LoginANDregister.Login;

public class Loading extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //type cast the imageview

        imageView = findViewById(R.id.imgV);

        // put the timer thread inside the on create method

        //new thread
        Thread timer = new Thread() {
            //the run method when the thread starts
            public void run() {
                try {
                    //specify the timer in milliseconds
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //if all is fine launch the new activity

                    Intent intent = new Intent(Loading.this, Login.class);
                    startActivity(intent);
                    Loading.this.finish();
                }
            }
        };
        timer.start();
    }

}
