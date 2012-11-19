package com.example.elevatorchecker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	static private MainActivity instance;
	
	public EditText lightView;
	
	
	
	public static MainActivity Instance() {
		return instance;
	}
	
	private LightReader lightReader;
	
	private SpeakerPlayer speakerPlayer;
	
	private AudioRecorder audioRecorder;
	
	private Button beginButton;
	private Button endButton;
	
	private Button recBeginButton;
	private Button recEndButton;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        instance = this;
        
        
        lightView = (EditText) this.findViewById(R.id.light_text_view);
        lightReader = new LightReader(this);
        
        
        speakerPlayer = new SpeakerPlayer("/sdcard/Music/recorder.wav");
        
        audioRecorder = new AudioRecorder("/sdcard/Music/recorder.wav");
        
        beginButton = (Button) this.findViewById(R.id.begin_button);
        endButton = (Button) this.findViewById(R.id.end_button);
        
        recBeginButton = (Button) this.findViewById(R.id.recorder_begin);
        recEndButton = (Button) this.findViewById(R.id.recorder_end);
        
        beginButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				speakerPlayer.start();
				
			}
        	
        });
        
        endButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				speakerPlayer.pause();
			}
        	
        });
        
        recBeginButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audioRecorder.start();
				
			}
        	
        });
        
        recEndButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audioRecorder.stop();
				audioRecorder = new AudioRecorder("/sdcard/Music/recorder.wav");
				
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
