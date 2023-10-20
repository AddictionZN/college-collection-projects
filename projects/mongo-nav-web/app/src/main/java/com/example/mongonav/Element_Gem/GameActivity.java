package com.example.mongonav.Element_Gem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mongonav.BaseActivity;
import com.example.mongonav.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class GameActivity extends AppCompatActivity {

    // Frame of the game
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    // Image of the game
    private ImageView car, mongo, apple, bomb;

    // Size
    private int boxSize;

    // Position
    private float carX, carY;
    private float mongoX, mongoY;
    private float appleX, appleY;
    private float bombX, bombY;

    // Keeping the score of the game
    private TextView scoreLabel;
    private int score, timeCount;

    // Timer Class
    private Timer timer;
    private Handler handler = new Handler();

    // Status of the game
    private boolean start_flag = false;
    private boolean action_flag = false;
    private boolean apple_flag = false;

    // Getting the email throughout the application
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent i = getIntent();
        email = i.getStringExtra("email");

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);

        // Moving elements of the game
        car = findViewById(R.id.car);
        apple = findViewById(R.id.apple);
        mongo = findViewById(R.id.mango);
        bomb = findViewById(R.id.bomb);

        // Keeping track of the score
        scoreLabel = findViewById(R.id.scoreLabel);

    }

    public void startGame(View view) {

        start_flag = true;
        startLayout.setVisibility(View.INVISIBLE);

        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            boxSize = car.getHeight();
            carX = car.getX();
            carY = car.getY();

        }

        frameWidth = initialFrameWidth;

        car.setX(0.0f);
        bomb.setY(3000.0f);
        mongo.setY(3000.0f);
        apple.setY(3000.0f);

        bombY = bomb.getY();
        mongoY = mongo.getY();
        appleY = apple.getY();

        // Setting all the images to in
        car.setVisibility(View.VISIBLE);
        bomb.setVisibility(View.VISIBLE);
        mongo.setVisibility(View.VISIBLE);
        apple.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flag) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }

            }
        }, 0, 20);
    }

    public void quitGame(View view) {

        Intent intent = new Intent(GameActivity.this, BaseActivity.class);
        intent.putExtra("email", email.trim());
        startActivity(intent);
        GameActivity.this.finish();
    }

    // Method to change the position for the state or inactive to active
    public void changePos() {


        // TimeCount
        timeCount += 20;

        // ****************************
        // Mongo
        // ****************************
        mongoY += 12;

        float mongoCenterX = mongoX + mongo.getWidth() / 2;
        float mongoCenterY = mongoY + mongo.getHeight() / 2;

        if (hitCheck(mongoCenterX, mongoCenterY)) {
            mongoY = frameHeight + 100;
            score += 10;
        }
        if (mongoY > frameHeight) {
            mongoY = -100;
            mongoX = (float) Math.floor(Math.random() * (frameWidth - mongo.getWidth()));
        }

        mongo.setX(mongoX);
        mongo.setY(mongoY);


        // ****************************
        // Apple
        // ****************************
        if (!apple_flag && timeCount % 10000 == 0) {
            apple_flag = true;
            appleY = -20;
            appleX = (float) Math.floor(Math.random() * (frameWidth - apple.getWidth()));
        }

        if (apple_flag) {
            appleY += 20;

            float appleCenterX = appleX + apple.getWidth() / 2;
            float appleCenterY = appleY + apple.getHeight() / 2;

            if (hitCheck(appleCenterX, appleCenterY)) {
                appleY = frameHeight + 30;
                score += 30;

                // Change the frame width to be bigger
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
            }

            if (appleY > frameHeight) apple_flag = false;
            apple.setX(appleX);
            apple.setY(appleY);
        }

        // ****************************
        // Bomb
        // ****************************
        bombY += 18;
        float bombCenterX = bombX + bomb.getWidth() / 2;
        float bombCenterY = bombY + bomb.getHeight() / 2;

        if (hitCheck(bombCenterX, bombCenterY)) {
            bombY = frameHeight + 100;

            // Change the frame of the game when hit
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);

            if (frameWidth <= boxSize) {
                // Game over
                gameOver();

            }
        }

        if (bombY > frameHeight) {
            bombY = -100;
            bombX = (float) Math.floor(Math.random() * (frameWidth - bomb.getWidth()));
        }

        bomb.setX(bombX);
        bomb.setY(bombY);


        // ****************************
        // Car
        // ****************************
        if (action_flag) {
            // Touching
            carX += 14;
        } else {
            // Releasing
            carX -= 14;
        }

        //Check the box position
        if (carX < 0) {
            carX = 0;
        }
        if (frameWidth - boxSize < carX) {
            carX = frameWidth - boxSize;
        }

        car.setX(carX);

        scoreLabel.setText("Score : " + score);
    }

    public boolean hitCheck(float x, float y) {
        if (carX <= x && x <= carX + boxSize && carY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {
        // Stop Timer
        timer.cancel();
        timer = null;
        start_flag = false;


        // Before showing startLayout, sleep for 1 second

        try {
            TimeUnit.SECONDS.sleep(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        // Setting all the images to in
        car.setVisibility(View.INVISIBLE);
        bomb.setVisibility(View.INVISIBLE);
        mongo.setVisibility(View.INVISIBLE);
        apple.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flag) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }

        return true;
    }
}
