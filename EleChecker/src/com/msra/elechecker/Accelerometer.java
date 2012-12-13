package com.msra.elechecker;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {
	
	private SensorManager sensorManager = null;
	
	public Accelerometer(Context ctx) {
		sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void start() {
		sensorManager.registerListener(accListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void stop() {
		sensorManager.unregisterListener(accListener);
	}
	
	
	
	private SensorEventListener accListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				float xCoord = arg0.values[0];
				float yCoord = arg0.values[1];
				float zCoord = arg0.values[2];
				System.out.println("value: x-"+xCoord + " y-" + yCoord + " z-" + zCoord);
			}
		}
		
	};
	
	
	
	
}