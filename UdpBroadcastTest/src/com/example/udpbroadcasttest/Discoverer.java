package com.example.udpbroadcasttest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.provider.SyncStateContract.Constants;
import android.util.Log;


/*
 * This class tries to send a broadcast UDP packet over your wifi network
 */

public class Discoverer implements Runnable {
	private static final String TAG = "Discovery";
	private static final int DISCOVERY_PORT = 2323;
	private static final int TIMEOUT_MS = 1000;
	private static final String DISCOVERY_REQ = "REQ";
	private static final String DISCOVERY_RPL = "RPL";
	private static final String DISCOVERY_DAT = "DAT";
	private static final int DISCOVERY_LOOP = 3;

	// TODO: Vary the challenge, or it's not much of a challenge :)
	private static WifiManager wifiManager;
	private InetAddress mBroadcastAddress;
	private int mGroupId = 0;
	private static String userId = "UNKNOWN";
	private DatagramSocket socket;
	
	private List<Peer> peerList = new ArrayList<Peer>();
	
	private DataCallback dataCallback = null;
	
	private static Discoverer discovererInstance = null;
	
	public static Discoverer getInstance() {
		if(wifiManager == null) {
			return null;
		}
		if(discovererInstance == null) {
			discovererInstance = new Discoverer(wifiManager);
		}
		return discovererInstance;
	}
	
	public static void setWifiManager(WifiManager wifi){
		wifiManager = wifi;
	}
	public static void setUserId(String uid) {
		userId = uid;
	}
	public static void destroyDiscoverer() {
		if(discovererInstance != null) {
			discovererInstance.destroy();
			discovererInstance = null;
		}
	}
	
	public interface DataCallback {
		void onData(int groupIp, String userId, String data);
	}
	
	public final List<Peer> getPeerList() {
		return peerList;
	}
	public void setDataCallback(DataCallback callback) {
		dataCallback = callback;
	}
	
	public void destroy() {
		socket.close();
	}
	
	public static class Peer {
		public Peer(int groupId, String userId, String ip) {
			this.groupId = groupId;
			this.userId = userId;
			this.ip = ip;
		}
		public boolean equals(Object object) {
			if(object instanceof Peer) {
				Peer peer = (Peer)object;
				return peer.userId.equals(this.userId) 
						&& peer.ip.equals(this.ip)
						&& peer.groupId == this.groupId;
			}
			return false;
		}
		public String toString() {
			return "gid:"+groupId+",uid:"+userId+",ip:"+ip+",last:"+lastUpdate;
		}
		int groupId;
		String ip;
		String userId;
		long lastUpdate;
	}

	class DiscoverRequestLooper implements Runnable {
		public DiscoverRequestLooper() {
			// TODO Auto-generated constructor stub
		}
		
		public void run() {
			String data = formatPacketData(DISCOVERY_REQ, null);
			try {
				for(int i = 0; i < DISCOVERY_LOOP; i++) {
					sendPacket(data.getBytes());
					Log.d(TAG, "Sending data " + data);
					Thread.sleep(1000);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Discoverer(WifiManager wifi) {
		wifiManager = wifi;
	}
	
	public void setGroupId(int gid) {
		mGroupId = gid;
	}
	public int getGroupId() {
		return mGroupId;
	}
	
	private void initSocket() {
		try {
			socket = new DatagramSocket(DISCOVERY_PORT);
			socket.setBroadcast(true);
			socket.setSoTimeout(TIMEOUT_MS);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkSocket() {
		if(socket == null) {
			initSocket();
		}
		return socket != null;
	}
	
	public void listen() {
		Thread thread = new Thread(this);
		thread.start();
	}
	public void run() {
		if(!checkSocket())
			return;
		listenForResponses();
	}
	
	public void discover() {
		sendDiscoveryRequest();
	}
	
	
	private String getUserId() {
		/*
		if(mUserId == null) {
			mUserId = new String("");
		}
		*/
		return userId;
	}
	
	private String formatPacketData(String type, String dat) {
		String data = null;
		if(type.equals(DISCOVERY_REQ)) {
			data = String.format("%d::%s::%s::%s",
					mGroupId, userId, DISCOVERY_REQ, getUserId());
		} else if(type.equals(DISCOVERY_RPL)) {
			data = String.format("%d::%s::%s::%s",
					mGroupId, userId, DISCOVERY_RPL, getUserId());
		} else {
			data = String.format("%d::%s::%s::%s",
					mGroupId, userId, DISCOVERY_DAT, dat);
		}
		return data;
	}

	/**
	 * Send a broadcast UDP packet containing a request for boxee services to
	 * announce themselves.
	 * 
	 * @throws IOException
	 */
	private void sendDiscoveryRequest() {
		Thread thread = new Thread(new DiscoverRequestLooper());
		thread.start();
	}
	
	private void sendDiscoveryReply() {
		String data = formatPacketData(DISCOVERY_RPL, null);
		try {
			sendPacket(data.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void broadcastData(String data) {
		String dat = formatPacketData(DISCOVERY_DAT, data);
		try {
			sendPacket(dat.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendPacket(byte[] data) throws IOException {
		if(!checkSocket()) 
			return;
		DatagramPacket packet = new DatagramPacket(data,
				data.length, getBroadcastAddress(), DISCOVERY_PORT);
		socket.send(packet);
	}

	/**
	 * Calculate the broadcast IP we need to send the packet along. If we send
	 * it to 255.255.255.255, it never gets sent. I guess this has something to
	 * do with the mobile network not wanting to do broadcast.
	 */
	private InetAddress getBroadcastAddress() throws IOException {
		DhcpInfo dhcp = wifiManager.getDhcpInfo();
		if (dhcp == null) {
			Log.d(TAG, "Could not get dhcp info");
			return null;
		}

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		
		//just for cache
		mBroadcastAddress = InetAddress.getByAddress(quads);
		return mBroadcastAddress;
	}

	/**
	 * Listen on socket for responses, timing out after TIMEOUT_MS
	 * 
	 */
	private void listenForResponses() {
		System.out.println("OK?");
		//return;
		
		byte[] buf = new byte[4096];
		while (true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				Log.d(TAG, "Receive timed out");
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} 
			String s = new String(packet.getData(), 0, packet.getLength());
			Log.d(TAG, "Received response " + s);
			handleResponses(s, packet.getAddress().toString());
		}
		
	}
	private boolean existPeer(Peer inPeer) {
		for(Peer peer : peerList) {
			if(peer.equals(inPeer)) {
				return true;
			}
		}
		return false;
	}

	
	private void handleResponses(String response, String ip) {
		if(response != null) {
			String[] sList = response.split("::");
			if(sList.length == 4) {
				int groupId = Integer.parseInt(sList[0]);
				String uid = sList[1];
				String type = sList[2];
				String data = sList[3];
				Peer fromPeer = new Peer(groupId, uid, ip);
				if(fromPeer.userId.equals(userId)) {
					return;
				}
				if(type.equals(DISCOVERY_REQ)) {
					if(!existPeer(fromPeer)) {
						peerList.add(fromPeer);
					}
					sendDiscoveryReply();
				} else if(type.equals(DISCOVERY_RPL)) {
					if(!existPeer(fromPeer)) {
						peerList.add(fromPeer);
					}
				} else if(type.equals(DISCOVERY_DAT)) {
					if(groupId == this.mGroupId) {
						//handle the data
						if(data != null && dataCallback != null) {
							System.out.println(data);
							dataCallback.onData(groupId, uid, data);
						}
					}
				}
			}
		}
	}
}