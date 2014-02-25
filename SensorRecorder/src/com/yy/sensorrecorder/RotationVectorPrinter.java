package com.yy.sensorrecorder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;


public class RotationVectorPrinter extends SensorPrinter {

	@Override
	protected void initSelf() {
		// TODO Auto-generated method stub
		this.initSensor(Sensor.TYPE_ROTATION_VECTOR);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	
}