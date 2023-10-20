package com.example.mongonav.Load;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mongonav.R;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.text_progress);

        // Getting the progress bar to populate
        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        ProgressBarAnimation();
    }

    public void ProgressBarAnimation() {
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, textView, 0, 100f);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);

    }
}
