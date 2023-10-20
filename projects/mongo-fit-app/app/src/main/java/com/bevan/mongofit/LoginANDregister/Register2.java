package com.bevan.mongofit.LoginANDregister;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.R;

import java.text.DecimalFormat;

public class Register2 extends AppCompatActivity {

    EditText etheight;
    EditText etweight;
    EditText etgoalweight;
    Switch im;
    Button register;

    TextView tvHeight;
    TextView tvGoalWeight;
    TextView tvWeight;

    String Weight;
    String GoalWeight;
    String Height;

    String setSystem;
    DecimalFormat df = new DecimalFormat("00.00");

    com.bevan.mongofit.Databases.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        etheight = findViewById(R.id.etHeight);
        etweight = findViewById(R.id.etWeight);
        etgoalweight = findViewById(R.id.etGoalWeight);
        im = findViewById(R.id.switchIM);

        register = findViewById(R.id.btnRegister);

        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvGoalWeight = findViewById(R.id.tvGoalWeight);

        dbHelper = new dbHelper(this);

        if (im.isChecked() == false) {
            String hei = etheight.getText().toString();
            String wei = etweight.getText().toString();
            String goal = etgoalweight.getText().toString();

            Weight = wei;
            Height = hei;
            GoalWeight = goal;

            setSystem = "Metric";
            tvWeight.setText(wei + " kg");
            tvGoalWeight.setText(goal + " kg");
            tvHeight.setText(hei + " cm");

        }
        etheight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String hei = etheight.getText().toString();
                Height = hei;
                tvHeight.setText(hei + ".00 cm");
            }
        });

        etweight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String wei = etweight.getText().toString();
                Weight = wei;
                tvWeight.setText(wei + ".00 kg");

            }
        });

        etgoalweight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String goal = etgoalweight.getText().toString();
                GoalWeight = goal;
                tvGoalWeight.setText(goal + ".00 kg");
            }
        });

        im.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {

                    if (im.isChecked() == false) {
                        String hei = etheight.getText().toString();
                        String wei = etweight.getText().toString();
                        String goal = etgoalweight.getText().toString();

                        setSystem = "Metric";
                        if (!wei.equals("")) {
                            double weigh = (Double.parseDouble(wei) * 1);
                            Weight = df.format(weigh);
                            tvWeight.setText(Weight + " kg");

                            double goalWeight = (Double.parseDouble(goal) * 1);
                            GoalWeight = df.format(goalWeight);
                            tvGoalWeight.setText(GoalWeight + " kg");

                            double heigh = (Double.parseDouble(hei) * 1);
                            Height = df.format(heigh);
                            tvHeight.setText(Height + " cm");
                        }

                    } else {
                        String hei = etheight.getText().toString();
                        String wei = etweight.getText().toString();
                        String goal = etgoalweight.getText().toString();

                        setSystem = "Imperial";
                        if (!wei.equals("") || !hei.equals("")) {

                            double weigh = (Double.parseDouble(wei) * 2.20462);
                            Weight = df.format(weigh);
                            tvWeight.setText(Weight + " lb");

                            double goalWeight = (Double.parseDouble(goal) * 2.20462);
                            GoalWeight = df.format(goalWeight);
                            tvGoalWeight.setText(GoalWeight + " lb");

                            double heigh = (Double.parseDouble(hei) * 0.393701);
                            Height = df.format(heigh);
                            tvHeight.setText(Height + " inches");
                        }
                    }

                } catch (Exception e) {
                    AlertDialog.Builder messageBox = new AlertDialog.Builder(Register2.this);
                    messageBox.setMessage(e.getMessage());
                    messageBox.show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                String username = i.getStringExtra("username");
                if (Height.equals("") || Weight.equals("") || GoalWeight.equals("")) {
                    Toast.makeText(Register2.this, "Please fill in all text fields", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.RegisteringUserInfo(Height.trim(), Weight.trim(), GoalWeight.trim(), username.trim(), setSystem.trim());
                    Intent intent = new Intent(Register2.this, Login.class);
                    Register2.this.finish();
                    startActivity(intent);
                }
            }
        });
    }
}
