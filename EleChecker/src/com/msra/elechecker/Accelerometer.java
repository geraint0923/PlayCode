package com.msra.elechecker;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {
	
	private SensorManager sensorManager = null;
	private float calibrateX = 0;
	private float calibrateY = 0;
	private float calibrateZ = 0;
	
	private boolean hasCalibrated = false;
	
	private boolean hasStarted = false;
	
	public Accelerometer(Context ctx) {
		sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void start() {
		hasStarted = true;
		sensorManager.registerListener(accListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void stop() {
		hasStarted = false;
		sensorManager.unregisterListener(accListener);
	}
	
	/*
	public void calibrate() {
		hasCalibrated = false;
	}
	*/
	
	
	
	private SensorEventListener accListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent arg0) {
			// TODO Auto-generated method stub
			
			
			if(arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				if(!hasCalibrated) {
					hasCalibrated = true;
					//stop();
					calibrateX = arg0.values[0];
					calibrateY = arg0.values[1];
					calibrateZ = arg0.values[2];
					return;
				}
				
				if(hasStarted) {
					float xCoord = arg0.values[0] - calibrateX;
					float yCoord = arg0.values[1] - calibrateY;
					float zCoord = arg0.values[2] - calibrateZ;
					System.out.println("value: x-"+xCoord + " y-" + yCoord + " z-" + zCoord);
					System.out.println("calibrate: x-"+calibrateX +" y-"+calibrateY+" z-"+calibrateZ);
				}
			}
		}
		
	};
	
	
	
	
}