package com.msra.elevatorchecker;

import java.io.FileOutputStream;

import com.msra.elevatorchecker.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
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
	
	private SurfaceView photoSurface = null;
	private SurfaceHolder photoHolder = null;
	
	private Button beginButton;
	private Button endButton;
	
	private Button recBeginButton;
	private Button recEndButton;
	
	private Button previewButton;
	private Button takeButton;
	
	
	private Camera camera = null;
	
	private Bitmap showBitmap = null;
	
	private ImageCapturer imageCapturer = null;
	
	
	private FileOutputStream fileOutputStream = null;
	
	public void writeLog(String str) {
		if(fileOutputStream != null) {
			
		}
	}
	
	
	private Runnable runInstance = null;
	
	class SurfaceCallback implements SurfaceHolder.Callback {

		private SurfaceView surface;
		private SurfaceHolder holder;
		
		private boolean isPush = false;
		
		public SurfaceCallback(SurfaceView sur, SurfaceHolder ho, boolean isP) {
			surface = sur;
			holder = ho;
			isPush = isP;
			if(isPush) {
				holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			} 
		}
		
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
					
					System.out.println("Create!!!!!!!!!!!!!!!!***");
					
					
					runInstance = this;
					
					int count = 0;
					System.out.println("OK!");

					while(count < 10) {
						
						int width = surface.getWidth();
						int wp = width / 10;
						int height = surface.getHeight();
						int hp = height / 10;
						
						
						Canvas canvas = holder.lockCanvas(new Rect(count*wp,0,(count+1)*wp,height));
						
						Paint paint = new Paint();
						paint.setColor(Color.BLUE);
						int color = count % 3;
						switch(color) {
							case 0:
								color = Color.RED;
								break;
							case 1:
								color = Color.BLUE;
								break;
							case 2:
								color = Color.GREEN;
								break;
							default:
								color = Color.BLACK;
								break;
						}
						canvas.drawColor(color);
						//Rect rect = new Rect(wp * count, hp * count, width - wp * count, height - hp * count );
						//canvas.drawRect(rect, paint);
						//System.out.println("count up!!! " + count +" "+ (wp * count) +" "+ (hp * count)+" "+ (width - wp * count)+" "+ (height - hp * count) + "width:"+canvas.getWidth() +" height:"+canvas.getHeight());
						//System.out.println("rect:left:" + rect.left + " top:"+rect.top+" right:"+rect.right+" bottom:"+rect.bottom);
						holder.unlockCanvasAndPost(canvas);
						++count;
						//lightView.setText("Now Count:" + count);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					if(!isPush) {
					
						while(true) {
							try {
								//synchronized(this) {
								//	wait();
								//}
								// TODO: add some code for showing the captured pictures.
								if(imageCapturer == null) {
									Thread.sleep(1000);
									continue;
								}
								showBitmap = imageCapturer.getBitmap();
								if(showBitmap != null) {
									Canvas canvas = holder.lockCanvas(new Rect(0, 0, surface.getWidth(), surface.getHeight()));
									Paint paint = new Paint();
									canvas.drawBitmap(showBitmap, 0, 0, paint);
									holder.unlockCanvasAndPost(canvas);
								}
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
        	//holder.addCallback(new SurfaceCallback(surface, holder, true));
        } else
        	lightView.setText("surface null!");
        
        
        photoSurface = (SurfaceView) this.findViewById(R.id.photo_surface);
        
        if(photoSurface != null) {
        	photoHolder = photoSurface.getHolder();
        	photoHolder.addCallback(new SurfaceCallback(photoSurface, photoHolder, false));
        	lightView.setText("Photo surface good");
        	System.out.println("Photo surface work fine.");
        }
        
        
        
        
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
        
        takeButton = (Button) this.findViewById(R.id.take_photo);
        
        previewButton = (Button) this.findViewById(R.id.preview_button);
        
        imageCapturer = new ImageCapturer(surface);
        
        imageCapturer.openCamera();
        
        takeButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(imageCapturer == null) {
					imageCapturer = new ImageCapturer(surface);
				}
				
				imageCapturer.takePicture();
				
				//showBitmap = imageCapturer.getBitmap();
				//runInstance.notify();				
			}
        	
        });
        
        previewButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imageCapturer.startPreview();
			}
        	
        });
        
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
