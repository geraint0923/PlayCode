package com.yy.sensorrecorder;

import java.io.File;
import java.io.IOException;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import 	android.media.CamcorderProfile;

public class SensorRecorderActivity extends Activity implements SurfaceHolder.Callback {

	private SurfaceView surfaceView;
	private Button startButton, stopButton;
	private MediaRecorder mediaRecorder;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private TextView textView;
	
	private SensorController sensorController;
	
	private String workDir;
	
	private final String VIDEO_TMP_PATH = "/sdcard/test/test.mp4";
	private final String WORK_ROOT = "/sdcard/test/";
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(textView != null && msg.getData() != null) {
				String text = msg.getData().getString("text", "none");
				textView.setText(text);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_recorder);
		init();
	}
	
	private void init() {
		startButton = (Button) findViewById(R.id.startButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		textView = (TextView) findViewById(R.id.textView);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Start pressed");
				if(mediaRecorder != null) {
					
				}
				camera = initCamera();
				mediaRecorder = new MediaRecorder();
				mediaRecorder.setCamera(camera);
				mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
				mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
				/*
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				mediaRecorder.setVideoSize(640, 480);
				mediaRecorder.setVideoFrameRate(10);
				*/
				mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
				mediaRecorder.setOutputFile(VIDEO_TMP_PATH);
				//System.out.println(mediaRecorder.getVideoEncodingBitRate());
				
				//mediaRecorder.setOrientationHint(90);
				
				try {
					mediaRecorder.prepare();
					mediaRecorder.start();
					
					long ts = System.currentTimeMillis();
					
					sensorController.startSensors();
					
					File dir = new File(WORK_ROOT + ts);
					if(!dir.exists())
						dir.mkdirs();
					workDir = dir.getAbsolutePath();
					
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
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
					sensorController.stopSensors();
					if(camera != null) {
						camera.lock();
						camera.release();
						camera = null;
					}
					
					File vf = new File(VIDEO_TMP_PATH);
					if(vf.exists() && workDir != null) {
						vf.renameTo(new File(workDir + "/video.mp4"));
						sensorController.dumpToFile(workDir + "/data.log");
						sensorController.clearData();
						workDir = null;
					}
					
					mediaRecorder = null;
					
					stopButton.setEnabled(false);
					startButton.setEnabled(true);
				}
			}
			
		});
		initSurfaceView();
		
		sensorController = new SensorController(this, new TextPrinter());
	}
	
	private void initSurfaceView() {
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	}
	
	private Camera initCamera() {
		Camera c = Camera.open();
		c.setDisplayOrientation(90);
		c.unlock();
		return c;
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
	
	private void showText(String str) {
		Bundle bundle = new Bundle();
		bundle.putString("text", str);
		Message msg = new Message();
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public class TextPrinter {
		public void showText(String str) {
			SensorRecorderActivity.this.showText(str);
		}
	}

}
