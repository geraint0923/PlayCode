package com.example.wifiapptest;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;



public class TestService extends Service implements Runnable {

	private AlarmManager alarmManager = null;
	private PendingIntent intent = null;
	private void initAlarmManager(int tick) {
		if(alarmManager == null) {
			alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		}
		Intent i = new Intent(this, Alarm.class);
		PendingIntent intent = PendingIntent.getBroadcast(this, 0, i, 0);
		System.out.println("set alarm");
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), tick, intent);
	}
	
	private void stopAlarmManager() {
		if(intent != null) {
			alarmManager.cancel(intent);
		}
	}
	
	public void onCreate() {
		
		initSocket(PORT);
		initThread();
		initAlarmManager(1000);
		
		triggerTester = new TriggerTester();
		batchTester = new BatchTester();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binderStub;
	}
	
	private final ITestService.Stub binderStub = new ITestService.Stub() {
		
		/*
		@Override
		public IBinder asBinder() {
			// TODO Auto-generated method stub
			return this;
		}
		*/
		
		@Override
		public void tick() throws RemoteException {
			// TODO Auto-generated method stub
			mainTick();
		}

		@Override
		public void startTriggerTester(int count, int para)
				throws RemoteException {
			// TODO Auto-generated method stub
			triggerTester.start(count, para);
		}

		@Override
		public void startBatchTester(int count, int para)
				throws RemoteException {
			// TODO Auto-generated method stub
			batchTester.start(count, para);
		}
	};
	
	
	interface Tester {
		
		public void start(int count, int para);
		
		public void stop();
		
		public void onRecv();
		
		public void tick();
	}
	
	class TriggerTester implements Tester {
		private boolean isRunning = false;
		private int ticks;
		private int delay = 0;
		
		@Override
		public void start(int count, int para) {
			// TODO Auto-generated method stub
			// we also need the time of delay
			ticks = -1;
			delay = para; 
			if(currentTester != null && currentTester != this) {
				currentTester.stop();
			}
			currentTester = this;
			isRunning = true;
			
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			if(!isRunning) {
				return;
			}
			isRunning = false;
		}

		@Override
		public void onRecv() {
			// TODO Auto-generated method stub
			// 
			if(delay == 0) {
				sendPacket("I am here");
			} else {
				ticks = delay;
			}
		}

		@Override
		public void tick() {
			// TODO Auto-generated method stub
			System.out.println("trigger tick");
			if(!isRunning) {
				return;
			}
			if(ticks <= 0) {
				return;
			} else {
				--ticks;
				if(ticks == 0) {
					sendPacket("I just got here");
				}
			}
		}

	}
	
	class BatchTester implements Tester {

		private boolean isRunning = false;
		private int ticks;
		private int batchSize = 0;
 		
		@Override
		public void start(int count, int para) {
			// TODO Auto-generated method stub
			// we also need the batch size
			ticks = 0;
			batchSize = para;
			
			if(currentTester != null && currentTester != this) {
				currentTester.stop();
			}
			currentTester = this;
			isRunning = true;
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			if(!isRunning) {
				return;
			}
			isRunning = false;
		}

		@Override
		public void onRecv() {
			// TODO Auto-generated method stub
			// Dummy method, don't do any thing
		}

		@Override
		public void tick() {
			// TODO Auto-generated method stub
			if(!isRunning) {
				return;
			}
			++ticks;
			if(ticks >= batchSize) {
				for(int i = 0; i < ticks; ++i) {
					sendPacket("Batch No."+i);
				}
				ticks = 0;
			}
		}

	}
	
	
	private void initThread() {
		if(recvThread == null) {
			//init a new thread
			recvThread = new Thread(this);
			recvThread.start();
		} else {
			//currentTester = tester;
		}
	}
	class SendThread implements Runnable {
		private String sendString;
		public SendThread(String str){
			sendString = str;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(socket == null) {
				initSocket(PORT);
			}
			byte[] data = (sendString+"\n").getBytes();
			try {
				DatagramPacket sPacket = new DatagramPacket(data, data.length, 
						InetAddress.getByName(destIP), destPort);
				socket.send(sPacket);
				System.out.println("I send a packet to"+destIP+":"+destPort);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void sendPacket(String str) {
		new Thread(new SendThread(str)).start();
	}
	
	private void recvPacket() throws IOException {
		if(socket == null) {
			initSocket(PORT);
		}
		socket.receive(packet);
		System.out.println("recv a packet from "+packet.getSocketAddress().toString());
	}
	
	private void stopThread() {
		if(recvThread == null) {
			return;
		}
		recvThread.interrupt();
	}
	
	public void run() {
		if(socket == null) {
			recvThread = null;
			return;
		}
		System.out.println("in thread!!!");
		while(true) {
			try {
				//try to receive a packet from a specified port
				recvPacket();
				
				if(currentTester != null) {
					currentTester.onRecv();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				recvThread = null;
				return;
			}
		}
	}
	
	private TriggerTester triggerTester = null;
	private BatchTester batchTester = null;
	
	private final int PORT = 2323;
	
	private DatagramSocket socket = null;
	private DatagramPacket packet = null;
	
	
	private String destIP = "192.168.1.113";
	private int destPort = 2323;
	private int packetCount = 1000;
	
	private byte[] recvBuffer = new byte[1500];
	
	private Thread recvThread = null;
	private Tester currentTester = null;
	
	private void initSocket(int port) {
		try {
			socket = new DatagramSocket(PORT);
			packet = new DatagramPacket(recvBuffer, recvBuffer.length);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void mainTick() {
		System.out.println("here tick!");
		if(currentTester != null) {
			currentTester.tick();
		}
	}
	
}