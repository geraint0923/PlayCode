

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class IwInfoParser {
	
	public static void main(String args[]) {
		IwInfoParser parser = new IwInfoParser();
		List<BSSInfo> bssList = parser.parseIwInfoFile("special/iw_inside.log");
		for(int i = 0; i < bssList.size(); ++i) {
			System.out.println(bssList.get(i));
		}
		System.out.println("Size:" + bssList.size());
		int[] num = new int[4];
		num[0] = 17;
		num[1] = 15;
		num[2] = 14;
		num[3] = 12;
		String[] mark = new String[2];
		mark[0] = new String("b");
		mark[1] = new String("a");
		int count = 0;
		for(int i = 1; i <= 3; ++i) {
			for(int j = 0; j < num.length; ++j) {
				for(int k = 0; k < mark.length; ++k) {
					String path = "iwlog/iw_" + i + "" + num[j] + mark[k] + ".log";
					//System.out.println(path);
					System.out.print(i + "" + num[j] + mark[k] + " ");
					List<BSSInfo> tmpList = parser.parseIwInfoFile(path);
					for(int idx = 0; idx < bssList.size(); ++idx) {
						BSSInfo info = bssList.get(idx);
						boolean found = false;
						System.out.print("&");
						for(int ii = 0; ii < tmpList.size(); ++ ii) {
							if(info.equals(tmpList.get(ii))) {
								System.out.print(" " + tmpList.get(ii).getSignal() + " ");
								found = true;
								break;
							}
						}
						if(!found)
							System.out.print(" - ");
					}
					System.out.println("\\\\\n\\hline");
					
					++count;
				}
			}
		}
		System.out.println("count:"+count);
		
		
	}
	
	public IwInfoParser() {
		
	}
	
	public ArrayList<BSSInfo> parseIwInfoFile(String path) {
		File file = new File(path);
		if(!file.exists())
			return null;
		
		try {
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			if(reader == null)
				return null;
			String line;
			BSSInfo info = null;
			ArrayList<BSSInfo> infoList = new ArrayList<BSSInfo>();
			while((line = reader.readLine()) != null) {
				//System.out.println(line);
				if(line.startsWith("BSS ")) {
					//System.out.println(line);
					if(info != null)
						infoList.add(info);
					info = new BSSInfo();
					if(info == null)
						break;
					// Parse the BSSID
					Pattern pattern = Pattern.compile("BSS ([^ ]+) \\(");
					Matcher matcher = pattern.matcher(line);
					if(matcher.find()) {
						info.setBSSID(matcher.group(1));
					}
					//System.out.println(info);
					
				} else if(info != null) {
					// Parse the frequency
					if(line.startsWith("\tfreq: ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tfreq: ([0-9]+)");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							info.setFrequency(Integer.parseInt(matcher.group(1)));
						}
					}
					
					// Parse the channel
					if(line.startsWith("\tDS Parameter set: channel ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tDS Parameter set: channel ([0-9]+)");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							info.setChannel(Integer.parseInt(matcher.group(1)));
						}
					}
					
					// Parse the beacon interval
					if(line.startsWith("\tbeacon interval: ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tbeacon interval: ([0-9]+)");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							info.setBeaconInterval(Integer.parseInt(matcher.group(1)));
						}
					}
					
					// Parse the beacon signal
					if(line.startsWith("\tsignal: ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tsignal: (-[0-9\\.]+)");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							info.setSignal(Double.parseDouble(matcher.group(1)));
						}
					}
					
					// Parse the SSID
					if(line.startsWith("\tSSID: ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tSSID: (.+)");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							info.setSSID(matcher.group(1));
						}
					}
					
					// TODO : Parse the supported rates
					if(line.startsWith("\tSupported rates: ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tSupported rates: (.+) ");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							String target = matcher.group(1);
							target = target.replaceAll("\\*", "");
							//System.out.println(target);
							String[] strList = target.split(" ");
							double[] rateList = new double[strList.length];
							for(int i = 0; i < strList.length; ++i) {
								rateList[i] = Double.parseDouble(strList[i]);
							}
							info.setSupportedRates(rateList);
						}
					}
					
					// TODO : Parse the extended supported rates
					if(line.startsWith("\tExtended supported rates: ")) {
						//System.out.println(line);
						Pattern pattern = Pattern.compile("\tExtended supported rates: (.+) ");
						Matcher matcher = pattern.matcher(line);
						if(matcher.find()) {
							String target = matcher.group(1);
							target = target.replaceAll("\\*", "");
							
							String[] strList = target.split(" ");
							double[] rateList = new double[strList.length];
							for(int i = 0; i < strList.length; ++i) {
								rateList[i] = Double.parseDouble(strList[i]);
							}
							info.setExtendedSupportedRates(rateList);
						}
					}
					
				} else {
					assert(false);
				}
			}
			if(info != null)
				infoList.add(info);
			return infoList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}