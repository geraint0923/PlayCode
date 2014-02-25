package com.yy.sensorrecorder;

import java.io.OutputStream;
import java.util.ArrayList;

import com.yy.sensorrecorder.SensorRecorderActivity.TextPrinter;

import android.content.Context;
import android.hardware.SensorManager;




public class SensorController {
	
	private OutputStream outputStream;
	private ArrayList<SensorPrinter> printerList = new ArrayList<SensorPrinter>();
	private SensorManager sensorManager;
	private TextPrinter textPrinter;
	
	public SensorController(Context ctx, TextPrinter tp) {
		sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
		textPrinter = tp;
		initSensorPrinter();
	}
	
	private void initSensorPrinter() {
		printerList.add(new AccelerometerPrinter());
		printerList.add(new GravityPrinter());
		printerList.add(new GyroscopePrinter());
		printerList.add(new LinearAccelerationPrinter());
		printerList.add(new MagneticFieldPrinter());
		printerList.add(new OrientationPrinter());
		printerList.add(new RotationVectorPrinter());
		
		for(int i = 0; i < printerList.size(); i++) 
			printerList.get(i).init(sensorManager, this);
	}
	
	public void startSensors() {
		for(int i = 0; i < printerList.size(); i++)
			printerList.get(i).start();
	}
	
	public void stopSensors() {
		for(int i = 0; i < printerList.size(); i++)
			printerList.get(i).stop();
	}
}