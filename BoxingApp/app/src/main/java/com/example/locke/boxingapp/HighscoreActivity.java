package com.example.locke.boxingapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class HighscoreActivity extends ActionBarActivity {
	EditText ed1,ed2,ed3,ed4,ed5;

	private String url1 = "http://192.168.43.235:8080/BOXINFG_APP/resources/user/";
	private String url2 = "";
	private HandleXML obj;
	Button b1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore2);
		b1=(Button)findViewById(R.id.button);

		ed1 = (EditText)findViewById(R.id.editText);
		ed2 = (EditText)findViewById(R.id.editText2);


		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = ed1.getText().toString();
				String finalUrl = url1 + url + url2;
				ed2.setText(finalUrl);

				obj = new HandleXML(finalUrl);
				obj.fetchXML();

				while(obj.parsingComplete);
				ed2.setText("Uw highscore is:  " + obj.getHighscore());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

