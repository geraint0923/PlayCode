package com.yy.sensorrecorder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SensorRecorderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_recorder);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor_recorder, menu);
		return true;
	}

}
