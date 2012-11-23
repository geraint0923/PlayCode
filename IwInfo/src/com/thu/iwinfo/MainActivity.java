package com.thu.iwinfo;

import java.io.IOException;
import java.io.OutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ExpandableListView listView = null;
	
	private Button scanButton = null;
	
	private EditText location = null;
	
	private Context context = null;
	
	private TextView resultView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		
		listView = (ExpandableListView) this.findViewById(R.id.BSSListView);
		
		scanButton = (Button) this.findViewById(R.id.scanButton);
		
		location = (EditText) this.findViewById(R.id.location);
		
		resultView = (TextView) this.findViewById(R.id.resultView);
		
		scanButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String path = "/sdcard/iwlog/iw_" + location.getText() + "_" + System.currentTimeMillis() + ".log"; 
				try {
					Process process = Runtime.getRuntime().exec("sh");
					OutputStream outputStream = process.getOutputStream();
					outputStream.write(("iw dev wlan0 scan > " + path + "\nexit\n").getBytes());
					outputStream.flush();
					process.waitFor();
					
					BSSListViewAdapter adapter = new BSSListViewAdapter(context, path);
					resultView.setText("We got " + adapter.getGroupCount() + " results");
					listView.setAdapter(adapter);
					location.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
        	
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
