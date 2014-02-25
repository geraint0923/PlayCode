package com.yy.sensorrecorder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;



public class OrientationPrinter extends SensorPrinter {

	@Override
	protected void initSelf() {
		// TODO Auto-generated method stub
		this.initSensor(Sensor.TYPE_ORIENTATION);
		sensorName = "Orientation";
	}

	@Override
	public String outputData(SensorEvent event) {
		// TODO Auto-generated method stub
		return String.format("x=%.6f,y=%.6f,z=%.6f", 
				event.values[0], event.values[1], event.values[2]);
	}

}