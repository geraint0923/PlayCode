package com.example.udpbroadcasttest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static final int APPENDTEXT = 1;
	private static final int SETTEXT = 2;
	
	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case APPENDTEXT:
				if(editText != null) {
					String str = msg.getData().getString("data");
					StringBuilder oldString = new StringBuilder(editText.getText().toString());
					oldString.append(str);
					editText.setText(oldString.toString());
				}
				break;
			case SETTEXT:
				if(editText != null) {
					editText.setText(msg.getData().getString("data"));
				}
				break;
			default:
				break;
			}
		}
	};
	
	private EditText editText = null;
	
	private UdpBroadcaster udpBroadcaster;
	
	private void appendText(String str){
		Message msg = new Message();
		msg.what = APPENDTEXT;
		Bundle bdl = new Bundle();
		bdl.putString("data", str);
		msg.setData(bdl);
		uiHandler.sendMessage(msg);
	}
	
	private void setText(String str){
		Message msg = new Message();
		msg.what = SETTEXT;
		Bundle bdl = new Bundle();
		bdl.putString("data", str);
		msg.setData(bdl);
		uiHandler.sendMessage(msg);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editText = (EditText) this.findViewById(R.id.text_edit);
		udpBroadcaster = new UdpBroadcaster(33333);
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int i = 0;
				while(true) {
					String str;
					byte[] buffer = new byte[4096];
					int len = udpBroadcaster.recv(buffer);
					if(len <= 0) 
						continue;
					try {
						str = new String(buffer, 0, len, "UTF-8");//"UTF-8");
						System.out.println("I recv: len="+len+" content="+str);
						//appendText((i++)+"-here is "+str+"\n");
						setText((i++)+"-here is "+str+"\n");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class UdpBroadcaster {
		private DatagramSocket socket = null;
		
		public UdpBroadcaster(int port) {
			// TODO Auto-generated constructor stub
			try {
				socket = new DatagramSocket(port);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public int recv(byte[] buffer) {
			if(socket != null && buffer != null) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(packet);
					return packet.getLength();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return 0;
		}
		
	}

}
