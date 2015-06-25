package com.example.locke.boxingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.util.Log;
import android.widget.TextView;

import com.example.locke.boxingapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class RecordingActivity extends Activity implements SensorEventListener {


    TextView text1;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private static final String FORMAT = "%02d:%02d:%02d";
    boolean isReady = false;
    public TextView x, y, z,textScore;
    public float x1, y1, z1, score;

    CountDownTimer c;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordin);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        x = (TextView) findViewById(R.id.textView);
        y = (TextView) findViewById(R.id.textView2);
        z = (TextView) findViewById(R.id.textView3);

        text1=(TextView)findViewById(R.id.textView1);
        textScore=(TextView)findViewById(R.id.textView4);



      start();
    }

    public void start () {

        c = new CountDownTimer(3000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                text1.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                updateText();

            }

            public void onFinish() {
                if (!isReady) {
                    text1.setText("HIT!");
                    isReady=true;
                    hit();
                }
                else {
                    text1.setText("DONE!!!");

                    score = (float) ((Math.abs(x1) + Math.abs(y1))*-1 + Math.abs(z1) + 0.19);
<<<<<<< HEAD
                    textScore.setText("Your score: " + score);
                    connectWeb("145.109.215.185/Tickets.php");
                    Log.d("CHECK","CHECK");
                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                InputStream is = null;
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("getAllTickets", "3"));

                                try {
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost("http://145.109.215.185/Tickets.php");
                                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                    HttpResponse response = httpclient.execute(httppost);
                                    HttpEntity entity = response.getEntity();
                                    is = entity.getContent();
                                    Log.d("HTTP", "HTTP: OK: " + response.toString());
                                    convertStreamToString(is);

                                } catch (Exception e) {
                                    Log.e("HTTP", "Error in http connection " + e.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

=======
                    if(score < 1){
                        textScore.setText("Your score: 1");
                    }
                    else
                    {
                        textScore.setText("Your score: " + score);
                    }
>>>>>>> origin/master
                }
            }
        }.start();
    }

    public void hit () {
            c.start();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x1 = sensorEvent.values[0];
            y1 = sensorEvent.values[1];
            z1 = sensorEvent.values[2];

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void updateText() {
        x.setText("X:" + x1);
        y.setText("Y:" + y1);
        z.setText("Z:" + z1);
    }

    public void calcPercentage(double x, double y, double z) {
        double xPerc, yPerc, zPerc, total;

        xPerc = (100/-0.09806824)*x;
        yPerc = (100/-0.05883789)*y;
        zPerc = (100/-9.728195)*z;

        total = (xPerc + yPerc + zPerc) /3;
        Log.d("PERC: " ,String.valueOf(total) + "?");

    }

    public  void connectWeb (String url)
    {

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url);

        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            Log.d("DONE",entity.toString());
            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                // now you have the string representation of the HTML request
                Log.d("RESULT", result);
                instream.close();
            }


        }

        catch (Exception e) {}

    }

    private  String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null ) {
                line = reader.readLine();
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("RESULTAAT",sb.toString());
        return sb.toString();
    }
}