package com.example.locke.boxingapp;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.widget.TextView;

import com.example.locke.boxingapp.R;


public class RecordingActivity extends Activity implements SensorEventListener {


    TextView text1;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private static final String FORMAT = "%02d:%02d:%02d";
    boolean isReady = false;
    int seconds , minutes;
    public TextView x, y, z;
    public float x1, y1, z1;
    CountDownTimer c;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordin);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        text1=(TextView)findViewById(R.id.textView1);



      start();
    }

    public void start () {

        c = new CountDownTimer(4000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                text1.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


                senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                senSensorManager.registerListener(RecordingActivity.this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                x = (TextView) findViewById(R.id.textView1);
                y = (TextView) findViewById(R.id.textView2);
                z = (TextView) findViewById(R.id.textView3);
            }

            public void onFinish() {
                if (isReady ==false) {
                    text1.setText("HIT!");
                    isReady=true;
                    hit();
                }
                else {

                    text1.setText("DONE!!!");
                }
            }
        }.start();
    }

    public void hit () {
            c.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}