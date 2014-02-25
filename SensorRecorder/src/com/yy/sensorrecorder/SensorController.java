package com.yy.sensorrecorder;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yy.sensorrecorder.SensorRecorderActivity.TextPrinter;

import android.content.Context;
import android.hardware.SensorManager;


public class SensorController {
	
	private OutputStream outputStream;
	private ArrayList<SensorPrinter> printerList = new ArrayList<SensorPrinter>();
	private SensorManager sensorManager;
	private TextPrinter textPrinter;
	private Map<SensorPrinter, SensorData> sensorDataMap = new HashMap<SensorPrinter, SensorData>();
	private ArrayList<SensorData> sensorDataList = new ArrayList<SensorData>();
	
	public class SensorData {
		public long timeStamp;
		public String sensor;
		public String data;
	}
	
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
	
	private int counter = 0;
	public void reportData(SensorPrinter sp, String str) {
		//TODO process the input data
		SensorData sd = new SensorData();
		sd.timeStamp = System.currentTimeMillis();
		sd.sensor = sp.sensorName;
		sd.data = str;
		sensorDataList.add(sd);
		sensorDataMap.put(sp, sd);
		
		counter++;
		if(counter % 15 != 0)
			return;
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < printerList.size(); i++) {
			SensorData d = sensorDataMap.get(printerList.get(i));
			if(d != null) {
				sb.append(d.sensor);
				sb.append(": ");
				sb.append(d.data);
				sb.append("\n");
			}
		}
		if(textPrinter != null)
			textPrinter.showText(sb.toString());
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