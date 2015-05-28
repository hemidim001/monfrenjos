package com.example.locke.boxingapp;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.widget.TextView;

import com.example.locke.boxingapp.R;


public class RecordingActivity extends Activity {


    TextView text1;

    private static final String FORMAT = "%02d:%02d:%02d";
    boolean isReady = false;
    int seconds , minutes;
    CountDownTimer c;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordin);


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

}