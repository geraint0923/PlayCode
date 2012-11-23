package com.thu.iwinfo;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;



public class BSSListViewAdapter extends BaseExpandableListAdapter {
	
	private ArrayList<BSSInfo> infoList = null;
	private Context context = null;
	
	public BSSListViewAdapter(Context ctx, String path) {
		context = ctx;
		IwInfoParser parser = new IwInfoParser();
		File file = new File(path);
		if(!file.exists())
			return;
		infoList = parser.parseIwInfoFile(path);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		if(infoList == null)
			return null;
		BSSInfo info = infoList.get(arg0);
		StringBuilder string = new StringBuilder("");
		switch(arg1) {
		case 0:
			string.append("\tChannel:" + info.getChannel() + "\n");
			break;
		case 1:
			string.append("\tFrequency:" + info.getFrequency() + "\n");
			break;
		case 2:
			string.append("\tSignal:" + info.getSignal() + " dBm\n");
			break;
		case 3:
			string.append("\tBeacon Interval:" + info.getBeconInterval() + " ms\n");
			break;
		default:
				break;
		}
		return string.toString();
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		if(infoList == null)
			return null;
		
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(  
                ViewGroup.LayoutParams.FILL_PARENT, 32);  
        TextView text = new TextView(context);  
        text.setLayoutParams(layoutParams);  
        // Center the text vertically  
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);  
        // Set the text starting position  
        text.setPadding(36, 0, 0, 0);  
        text.setText((String)getChild(arg0, arg1)); 
        return text;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		if(infoList == null)
			return null;
		BSSInfo info = infoList.get(arg0);
		StringBuilder string = new StringBuilder("SSID:");
		string.append(info.getSSID() + "\n");
		string.append("BSSID:" + info.getBSSID());
		return string.toString();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(infoList == null)
			return 0;
		return infoList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		if(infoList == null)
			return null;
		
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(  
                ViewGroup.LayoutParams.FILL_PARENT, 64);  
        TextView text = new TextView(context);  
        text.setLayoutParams(layoutParams);  
        // Center the text vertically  
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);  
        // Set the text starting position  
        text.setPadding(36, 0, 0, 0);  
        text.setText((String)getGroup(arg0)); 
        return text;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}