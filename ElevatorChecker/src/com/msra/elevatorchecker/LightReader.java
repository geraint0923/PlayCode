package com.msra.elevatorchecker;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LightReader {
	
	private SensorManager sensorManager;
	private Sensor lightSensor;
	
	
	private SensorEventListener lightSensorEventListener = new SensorEventListener(){
 
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
 
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(event.sensor.getType() == Sensor.TYPE_LIGHT){
				float currentReading = event.values[0];
				MainActivity.Instance().lightView.setText("Current Reading: " + String.valueOf(currentReading));
			}
		}
	};
	
	public LightReader(Context ctx) {
		sensorManager = (SensorManager)ctx.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        
        if(lightSensor == null) {
        	Toast.makeText(ctx, "We don't have a light sensor!", Toast.LENGTH_LONG).show();
        } else {
        	Toast.makeText(ctx, "We have a light sensor!", Toast.LENGTH_LONG).show();
        }
        
        sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        
	}
	
}