package com.yy.sensorrecorder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorPrinter implements SensorEventListener {
	protected SensorManager sensorManager;
	protected Sensor sensor;
	protected SensorController sensorController;
	protected boolean hasStarted = false;
	
	public void init(SensorManager sm, SensorController sc) {
		sensorManager = sm;
		sensorController = sc;
		initSelf();
	}
	protected abstract void initSelf();
	
	protected void initSensor(int type) {
		if(sensorManager != null) {
			sensor = sensorManager.getDefaultSensor(type);
		}
	}
	public void start() {
		if(sensor != null) {
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			hasStarted = true;
		}
	}
	public void stop() {
		if(sensor != null && hasStarted) {
			sensorManager.unregisterListener(this);
			hasStarted = false;
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	@Override
	public abstract void onSensorChanged(SensorEvent event);
}
