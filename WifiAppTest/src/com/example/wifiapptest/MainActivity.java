package com.example.wifiapptest;

import android.R.integer;
import java.net.*;
import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void setCountText(String str) {
		
	}
	
	private void setTriggerText(String str) {
		
	}
	
	private void setBatchText(String str) {
		
	}

	private TriggerTester triggerTester = null;
	private BatchTester batchTester = null;
	
	private final int PORT = 2323;
	
	private DatagramSocket socket = null;
	private DatagramPacket packet = null;
	
	private void initSocket(int port) {
		
	}
	
	abstract class Tester {
		protected boolean isRunning = false;
		
		public abstract void start(int count, int para);
		
		public abstract void stop();
		
		public abstract void onRecv();
	}
	
	class TriggerTester extends Tester {

		
		@Override
		public void start(int count, int para) {
			// TODO Auto-generated method stub
			// we also need the time of delay
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			if(!isRunning) {
				
			}
		}

		@Override
		public void onRecv() {
			// TODO Auto-generated method stub
			// 
		}

	}
	
	class BatchTester extends Tester {

		@Override
		public void start(int count, int para) {
			// TODO Auto-generated method stub
			// we also need the batch size
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			if(!isRunning) {
				
			}
		}

		@Override
		public void onRecv() {
			// TODO Auto-generated method stub
			// Dummy method, don't do any thing
		}

	}
}
