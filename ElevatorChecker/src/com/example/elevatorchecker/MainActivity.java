package com.example.elevatorchecker;

import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
	
	private AudioHandler audioHandler = null;
	
	private SurfaceView surface = null;
	private SurfaceHolder holder = null;
	
	private Button beginButton;
	private Button endButton;
	
	private Button recBeginButton;
	private Button recEndButton;
	
	
	private FileOutputStream fileOutputStream = null;
	
	public void writeLog(String str) {
		if(fileOutputStream != null) {
			
		}
	}
	
	class SurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			int x = 0;
			int y = 0;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int count = 0;
					System.out.println("OK!");

					while(count < 10) {
						
						int width = surface.getWidth();
						int wp = width / 20;
						int height = surface.getHeight();
						int hp = height / 20;
						
						
						Canvas canvas = holder.lockCanvas();
						Paint paint = new Paint();
						paint.setColor(Color.BLUE);
						canvas.drawRect(new Rect(wp * count, hp * count, width - wp * count, height - hp * count ), paint);
						System.out.println("count up!!! " + count +" "+ (wp * count) +" "+ (hp * count)+" "+ (width - wp * count)+" "+ (height - hp * count) + "width:"+width +" height:"+height);
						holder.unlockCanvasAndPost(canvas);
						++count;
						//lightView.setText("Now Count:" + count);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				
			}).start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
		}

	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        instance = this;
        
        
        lightView = (EditText) this.findViewById(R.id.light_text_view);
        
        surface = (SurfaceView) this.findViewById(R.id.test_surface);
        
        if(surface != null) {
        	lightView.setText("surface not null");
        	holder = surface.getHolder();
        	holder.addCallback(new SurfaceCallback());
        } else
        	lightView.setText("surface null!");
        
        //lightReader = new LightReader(this);
        
        
        speakerPlayer = new SpeakerPlayer("/sdcard/Music/white_noise.wav");
        
        audioRecorder = new AudioRecorder("/sdcard/Music/recorder_new.3gp");
        
        
        
        beginButton = (Button) this.findViewById(R.id.begin_button);
        endButton = (Button) this.findViewById(R.id.end_button);
        
        beginButton.setEnabled(true);
        endButton.setEnabled(false);
        
        recBeginButton = (Button) this.findViewById(R.id.recorder_begin);
        recEndButton = (Button) this.findViewById(R.id.recorder_end);
        
        recBeginButton.setEnabled(true);
        recEndButton.setEnabled(false);
        
        beginButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				speakerPlayer.start();
				beginButton.setEnabled(false);
				endButton.setEnabled(true);
				
			}
        	
        });
        
        endButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				speakerPlayer.pause();
				beginButton.setEnabled(true);
				endButton.setEnabled(false);
				
				/*
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileOutputStream = null;
				
				*/
			}
        	
        });
        
        recBeginButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//audioRecorder.start();
				audioHandler = new AudioHandler();
				audioHandler.start();
				recBeginButton.setEnabled(false);
				recEndButton.setEnabled(true);
			}
        	
        });
        
        recEndButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audioHandler.stop();
				recBeginButton.setEnabled(true);
				recEndButton.setEnabled(false);
				
				//audioRecorder.stop();
				//audioRecorder = new AudioRecorder("/sdcard/Music/recorder.wav");
				
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
