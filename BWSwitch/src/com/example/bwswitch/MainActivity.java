package com.example.bwswitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.jar.Attributes.Name;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MainActivity instance = null;
	
	private WifiAdmin wifiAdmin = null;
	
	private BluetoothAdapter mAdapter;
	
	private UUID getUuid() {
		TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);    
		String tmDevice, tmSerial, tmPhone, androidId;    
		  tmDevice = "" + tm.getDeviceId();   
		  tmSerial = "" + tm.getSimSerialNumber();    
		  androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);    
		  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode()); 
		  return deviceUuid;
	}
	
	private Handler mHandler = new Handler(){  
        
        public void handleMessage(Message msg) {  
           
            switch (msg.what) {
			case 1:
				Toast.makeText(instance, "write failed!", Toast.LENGTH_LONG).show();
				return;
			case 2:
				Toast.makeText(instance, "read failed!", Toast.LENGTH_LONG).show();
				return;

			default:
				break;
			}
            if(isServer) {
            	Toast.makeText(instance, "Server read!", Toast.LENGTH_LONG).show();
            } else {
            	//Toast.makeText(instance, "Client write!", Toast.LENGTH_LONG).show();
            }
        };  
    };
	
	boolean isServer = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				wifiAdmin = new WifiAdmin(instance);
				
				wifiAdmin.openWifi();
				System.out.println("start:"+System.currentTimeMillis()); 
				List<ScanResult> listResults;
				while(true) {
					wifiAdmin.startScan();
					listResults = wifiAdmin.getWifiList();
					if(listResults == null) {
						continue;
					}
					if(listResults.size() > 0) {
						break;
					}
					
				}
				System.out.println("scan");
				System.out.println("found wifi:"+System.currentTimeMillis());
				
				for(int i = 0; i < listResults.size(); ++i) {
					if(listResults.get(i).SSID.equals("sdrtest")) {
						wifiAdmin.connectConfiguration(i);
						System.out.println("I connect "+listResults.get(i).SSID);
						break;
					}
					System.out.println("Here is "+listResults.get(i).SSID);
				}
			}
		});
		thread.start();
		
		
		
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		/*
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, 0x1);
		*/
		mAdapter.enable();
		
		Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		//adapter
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		//registerReceiver(mReceiver, filter);
		
		if(isServer) {
			acceptThread = new AcceptThread();
			acceptThread.start();
		} else {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			BluetoothDevice device = adapter.getRemoteDevice("10:68:3F:B9:47:81");//服务器的蓝牙地址
			connectThread = new ConnectThread(device);
			connectThread.start();
		}
	}
	
	private Thread connectThread;
	private Thread acceptThread;
	
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
	
	BluetoothSocket clientSocket = null;
	BluetoothSocket serverSocket = null;

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
				/*
				UUID uuid = getUuid();
				System.out.println("uuid:"+uuid);
				tmp = mAdapter.listenUsingRfcommWithServiceRecord("YY", uuid);
				*/
				UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
				BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
				tmp = adapter.listenUsingRfcommWithServiceRecord("YY", uuid);
				System.out.println("aceept");
				//BluetoothSocket socket = serverSocket.accept();
			} catch (IOException e){
				e.printStackTrace();
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
						serverSocket = socket;
						new ConnectedThread(serverSocket).start();
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
				UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
				tmp = device.createRfcommSocketToServiceRecord(uuid);
			} catch (IOException e){
				
			}
			mmSocket = tmp;
		}
		
		public void run() {
			mAdapter.cancelDiscovery();
			
			try {
				mmSocket.connect();
				new ConnectedThread(mmSocket).start();
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
			if(!isServer) {
				mHandler.sendEmptyMessage(0);
				while(true) {
					byte[] allData = (System.currentTimeMillis()+"\n").getBytes();
					try {
						write(allData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(1);
					}
					//mHandler.obtainXX
				}
			}
			if(!isServer) 
				return;
			try {
				FileOutputStream fos = new FileOutputStream(new File("/sdcard/log.txt"));
				//Toast.makeText(instance, "Server read!", Toast.LENGTH_LONG);
				mHandler.sendEmptyMessage(0);
				while(true) {
					try {
						bytes = mmInputStream.read(buffer);
						byte[] prefix = (""+System.currentTimeMillis()+"<-").getBytes();
						fos.write(prefix, 0, prefix.length);
						fos.flush();
						fos.write(buffer, 0, bytes);
						fos.flush();
						//mHandler.obtainXX
					} catch(IOException e){
						break;
					}
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
		public void write(byte[] bytes) throws IOException {

				mmOutputStream.write(bytes);

		}
		
		public void cancel() {
			try {
				mmSocket.close();
			} catch(IOException e) {
				
			}
		}
	}

}
