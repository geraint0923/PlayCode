package com.example.bwswitch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.Attributes.Name;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;

public class MainActivity extends Activity {

	
	private BluetoothAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		/*
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, 0x1);
		*/
		mAdapter.enable();
		
		Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		//adapter
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			
			if(BluetoothDevice.ACTION_FOUND.equals(action)){ 
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				System.out.println("device found");
			}
		}
		
	};
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		
		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = mAdapter.listenUsingRfcommWithServiceRecord("YY", null);
			} catch (IOException e){
				
			}
			mmServerSocket = tmp;
		}
		
		public void run() {
			BluetoothSocket socket = null;
			while(true) {
				try {
					socket = mmServerSocket.accept();
				} catch(IOException e) {
					break;
				}
				if(socket != null) {
					try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch(IOException e){
				
			}
		}
	}
	
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		
		public ConnectThread(BluetoothDevice device){
			BluetoothSocket tmp = null;
			mmDevice = device;
			
			try {
				tmp = device.createRfcommSocketToServiceRecord(null);
			} catch (IOException e){
				
			}
			mmSocket = tmp;
		}
		
		public void run() {
			mAdapter.cancelDiscovery();
			
			try {
				mmSocket.connect();
			} catch(IOException e) {
				try {
					mmSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		public void cancel() {
			try {
				mmSocket.close();
			} catch(IOException e){
				
			}
		}
	}
	
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private InputStream mmInputStream;
		private OutputStream mmOutputStream;
		
		public ConnectedThread(BluetoothSocket socket){
			mmSocket = socket;
			InputStream tmpin = null;
			OutputStream tmpout = null;
			
			try {
				tmpin = socket.getInputStream();
				tmpout = socket.getOutputStream();
			} catch(IOException e){
				
			}
			
			mmInputStream = tmpin;
			mmOutputStream = tmpout;
		}
		
		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
			
			while(true) {
				try {
					bytes = mmInputStream.read(buffer);
					//mHandler.obtainXX
				} catch(IOException e){
					break;
				}
			}
		}
		
		public void write(byte[] bytes) {
			try {
				mmOutputStream.write(bytes);
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		
		public void cancel() {
			try {
				mmSocket.close();
			} catch(IOException e) {
				
			}
		}
	}

}
