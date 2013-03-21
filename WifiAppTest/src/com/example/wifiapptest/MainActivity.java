package com.example.wifiapptest;

import android.R.integer;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Delayed;
import java.util.jar.Pack200;



import android.R.integer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	
	
	private EditText countText = null;
	private EditText triggerText = null;
	private EditText batchText = null;
	
	private Button triggerButton = null;
	private Button batchButton = null;
	public static MainActivity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		instance = this;
		
		
		countText = (EditText) this.findViewById(R.id.count_text);
		triggerText = (EditText) this.findViewById(R.id.trigger_text);
		triggerButton = (Button) this.findViewById(R.id.trigger_btn);
		batchButton = (Button) this.findViewById(R.id.batch_btn);
		batchText = (EditText) this.findViewById(R.id.batch_text);
		
		countText.setText(destIP+" "+destPort+" "+packetCount);
		
		triggerText.setText("0");
		
		batchText.setText("1");
		

		
		
		triggerButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDest();
				int para = Integer.parseInt(triggerText.getText().toString());
				try {
					testService.startTriggerTester(packetCount, para);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		batchButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDest();
				int para = Integer.parseInt(batchText.getText().toString());
				try {
					testService.startBatchTester(packetCount, para);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		Intent intent = new Intent("com.example.TestService"); 
		startService(intent); 
        
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);  
		
		
		
		
		
	//	initSocket(PORT);
	//	initThread();
	//	initAlarmManager(1000);
		
		
	}
	
	protected void onDestroy() {
	//	stopThread();
	//	stopAlarmManager();
		this.unbindService(serviceConnection); 
		super.onDestroy();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void setCountText(String str) {
		countText.setText(str);
	}
	
	private void setTriggerText(String str) {
		triggerText.setText(str);
	}
	
	private void setBatchText(String str) {
		batchText.setText(str);
	}
	
	private String getTriggerString() {
		return triggerText.getText().toString();
	}
	
	private String getBatchString() {
		return batchText.getText().toString();
	}

	
	

	
	private String destIP = "192.168.1.142";
	private int destPort = 2323;
	private int packetCount = 1000;
	
	private void initDest() {
		String str = countText.getText().toString();
		String[] sList = str.split(" ");
		if(sList.length == 3) {
			destIP = sList[0];
			destPort = Integer.parseInt(sList[1]);
			packetCount = Integer.parseInt(sList[2]);
		}
	}
	
	private ITestService testService = null;
	
	private ServiceConnection serviceConnection = new ServiceConnection() { 


		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			testService = (ITestService) arg1; 
			
			System.out.println("I connect!!!");
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			testService = null;
		} 

	};
	

	
	
}
