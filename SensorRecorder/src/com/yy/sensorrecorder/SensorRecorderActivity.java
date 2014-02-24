package com.yy.sensorrecorder;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SensorRecorderActivity extends Activity implements SurfaceHolder.Callback {

	private SurfaceView surfaceView;
	private Button startButton, stopButton;
	private MediaRecorder mediaRecorder;
	private SurfaceHolder surfaceHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_recorder);
		init();
	}
	
	private void init() {
		startButton = (Button) findViewById(R.id.startButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Start pressed");
				if(mediaRecorder != null) {
					
				}
				mediaRecorder = new MediaRecorder();
				mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				mediaRecorder.setVideoSize(176, 144);
				mediaRecorder.setVideoFrameRate(20);
				mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
				mediaRecorder.setOutputFile("/sdcard/test/test.mp4");
				
				try {
					mediaRecorder.prepare();
					mediaRecorder.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		});
		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Stop pressed");
				if(mediaRecorder != null) {
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
				}
			}
			
		});
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor_recorder, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		surfaceHolder = arg0;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		surfaceHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		surfaceView = null;
		surfaceHolder = null;
		//mediaRecorder = null;
	}

}
