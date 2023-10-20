package com.bevan.mongofit.MenuActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bevan.mongofit.Landing;
import com.bevan.mongofit.R;

public class Camera extends AppCompatActivity {

    // declare the variables
    Button camera;
    ImageView camView;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // type cast the variables
        camera = findViewById(R.id.btnCam);
        camView = findViewById(R.id.imgCam);

        // parsing the username value from the login page
        Intent i = getIntent();
        username = i.getStringExtra("username").trim();

    }

    // method to take pic
    public void onCam(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Camera.this, Landing.class);
        intent.putExtra("username", username);
        startActivity(intent);
        Camera.this.finish();
    }


    // code fot the activity for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            //sent the image to your image view
            camView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "picture could not be saved" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
