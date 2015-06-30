package com.example.locke.boxingapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class RecordingActivity extends Activity implements SensorEventListener {

	private Button repunchButton;

	private SensorManager senSensorManager;
	private Sensor senAccelerometer;

	public TextView x, y, z, textScore, countdown, getReady;
	public float x1, y1, z1, score;
	private String urlString = "http://192.168.43.235:8080/BOXINFG_APP/resources/user/users/1/";
	CountDownTimer c;
	int i;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recordin);

		senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

		x = (TextView) findViewById(R.id.xValue);
		y = (TextView) findViewById(R.id.yValue);
		z = (TextView) findViewById(R.id.zValue);

		countdown = (TextView) findViewById(R.id.countdown);
		repunchButton = (Button) findViewById(R.id.restart_button);
		textScore = (TextView) findViewById(R.id.textView4);
		getReady = (TextView) findViewById(R.id.getReadyText);

		//Set the listener for the restart button
		repunchButton.setOnClickListener(rePunch);

		start();
	}

	public void start() {

		//Hide the button for a rematch
		repunchButton.setVisibility(View.GONE);

		c = new CountDownTimer(11000, 1000) { // adjust the milli seconds here

			public void onTick(long millisUntilFinished) {
				countdown.setText(Long.toString(millisUntilFinished / 1000));
				if(millisUntilFinished < 4000){
					getReady.setText("Get ready!");
				}
				if(millisUntilFinished < 3000){
					getReady.setText("Set?");
				}
				if(millisUntilFinished < 2000){
					getReady.setText(" ");
				}
				updateText();

			}

			public void onFinish() {
					countdown.setText("HIT!");

					score = (float) (((Math.abs(x1) + Math.abs(y1)) * -1 + Math.abs(z1) + 1.9)*10);
					score = Math.round(score);
					i = (int) score;
					textScore.setText("Uw score is: " + i);

					//Hide the button for a rematch
					repunchButton.setVisibility(View.VISIBLE);

					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {

								HttpClient httpClient = new DefaultHttpClient();
								HttpContext localContext = new BasicHttpContext();
								HttpGet httpGet = new HttpGet(urlString + i);
								try {
									httpClient.execute(httpGet, localContext);
								} catch (IOException e) {
									e.printStackTrace();
								}

						}
					});
					thread.start();
				}
		}.start();
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

	//Listener for the repunch button
	Button.OnClickListener rePunch = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			start();
		}
	};

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
        Log.d("RESULTAAT", sb.toString());
        return sb.toString();
    }

}