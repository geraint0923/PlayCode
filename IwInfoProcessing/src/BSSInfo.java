



public class BSSInfo {
	private String ssid;
	private String bssid;
	
	private int channel;
	private int frequency;
	private int beaconInterval;
	private double signal;
	
	private double[] supportedRates;
	private double[] extendedSupportedRates;
	
	public String toString() {
		StringBuilder string = new StringBuilder("SSID : ");
		string.append(ssid + "\n");
		string.append("BSSID : " + bssid + "\n");
		string.append("Channel : " + channel + "\n");
		string.append("Frequency : " + frequency + " MHz\n");
		string.append("Signal : " + signal + " dBm\n");
		string.append("Beacon Interval : " + beaconInterval + " ms\n");
		
		string.append("Supported Rates : ");
		if(supportedRates != null) {
			for(int i = 0; i < supportedRates.length; ++i) {
				string.append(supportedRates[i] + " ");
			}
		}

		string.append("\nExtended Supported Rates : ");
		if(extendedSupportedRates != null) {
			for(int i = 0; i < extendedSupportedRates.length; ++i) {
				string.append(extendedSupportedRates[i] + " ");
			}
		}
		string.append("\n");
		return string.toString();
	}
	
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
	public void setBSSID(String in) {
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
	
	
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof BSSInfo) {
			BSSInfo info = (BSSInfo) obj;
			if(info.ssid != null && info.bssid != null && info.ssid.equals(ssid) && info.bssid.equals(bssid)) {
				return true;
			}
		}
		return false;
	}
}