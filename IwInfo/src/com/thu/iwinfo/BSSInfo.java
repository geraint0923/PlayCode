package com.thu.iwinfo;



public class BSSInfo {
	private String ssid;
	private String bssid;
	
	private int channel;
	private int frequency;
	private int beaconInterval;
	private double signal;
	
	private double[] supportedRates;
	private double[] extendedSupportedRates;
	
	public String getSSID () {
		return ssid;
	}
	public String getBSSID() {
		return bssid;
	}
	public int getChannel() {
		return channel;
	}
	public int getFrequency() {
		return frequency;
	}
	public int getBeconInterval() {
		return beaconInterval;
	}
	public double getSignal() {
		return signal;
	}
	
	public void setSSID(String in) {
		ssid = new String(in);
	}
	public void getBSSID(String in) {
		bssid = new String(in);
	}
	public void setChannel(int ch) {
		channel = ch;
	}
	public void setFrequency(int freq) {
		frequency = freq;
	}
	public void setBeaconInterval(int bi) {
		beaconInterval = bi;
	}
	public void setSignal(double si) {
		signal = si;
	}
	public void setSupportedRates(double[] srList) {
		supportedRates = new double[srList.length];
		for(int i = 0; i < srList.length; ++i) {
			supportedRates[i] = srList[i];
		}
	}
	public void setExtendedSupportedRates(double[] srList) {
		extendedSupportedRates = new double[srList.length];
		for(int i = 0; i < srList.length; ++i) {
			extendedSupportedRates[i] = srList[i];
		}
	}
	
}