package com.msra.elechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SurfaceHolder.Callback,ICamera,ICheckerCallback {

	private SurfaceView previewSurface = null;
	private SurfaceHolder previewHolder = null;
	
	private SurfaceView photoSurface = null;
	private SurfaceHolder photoHolder = null;
	
	private SurfaceView processSurface = null;
	private SurfaceHolder processHolder = null;
	
	private Camera camera = null;
	
	private Button photoButton = null;
	
	private String imagePath = null;
	
	private String imagePathPrefix = "/sdcard/ele_images/sample_";
	
	
	private Button calibrateButton = null;
	private Button beginButton = null;
	
	private Checker useChecker = null;
	
	private MainActivity instance = null;
	
	private void initCamera() {
		camera = Camera.open();
		Parameters paras = camera.getParameters();
		List<Size> sList = paras.getSupportedPictureSizes();
		int minW = 99999, minH = 99999;
		for(int i = 0; i < sList.size(); ++i) {
			int w = sList.get(i).width;
			int h = sList.get(i).height;
			if(w * h < minW * minH) {
				minW = w;
				minH = h;
			}
		}
		paras.setPictureSize(minW, minH);
		paras.setJpegQuality(66);
		camera.setParameters(paras);
		
		//camera.setDisplayOrientation(90);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		instance = this;
		
		useChecker = new Checker();
		useChecker.setCamera(instance);
		
		initCamera();
		
		previewSurface = (SurfaceView) this.findViewById(R.id.preview_surface);
		photoSurface = (SurfaceView) this.findViewById(R.id.photo_surface);
		processSurface = (SurfaceView) this.findViewById(R.id.process_surface);
		
		processHolder = processSurface.getHolder();
		
		photoButton = (Button) this.findViewById(R.id.photo_button);
		
		photoButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(camera != null) {
					camera.takePicture(null, null, pictureCallback);
				}
			}
			
		});
		
		calibrateButton = (Button) this.findViewById(R.id.calibrate_button);
		calibrateButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(useChecker != null) {
					useChecker.calibrate(5000);
				}
			}
			
		});
		
		beginButton = (Button) this.findViewById(R.id.begin_button);
		beginButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				if(useChecker != null) {
					useChecker.checkEmpty(instance);
				}
				*/
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true) {
							try {
								Thread.sleep(1000);
								useChecker.checkEmpty(instance);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					
				}).start();
			}
			
		});
		
		previewHolder = previewSurface.getHolder();
		previewHolder.addCallback(this);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		
		photoHolder = photoSurface.getHolder();
		
		
	}
	
	private PictureCallback pictureCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub
			System.out.println("***********************I have take a photo!!!!!**************size:"+ arg0);
			camera.startPreview();
			
			imagePath = imagePathPrefix + System.currentTimeMillis() + ".jpeg";
			System.out.println("path:"+imagePath+" prefix:"+imagePathPrefix);
			File file = new File(imagePath);
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				file.delete();
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(arg0);
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("=================I have take a photo!!!!!===================size:"+ arg0 + imagePath);
			
			
			// Lock the photo canvas
			Canvas canvas = photoHolder.lockCanvas();
			
			Paint paint = new Paint();
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			System.out.println("imagePath:"+imagePath);
			
			
			//Matrix matrix = new Matrix();
			//matrix.postScale(((float)canvas.getWidth())/bitmap.getWidth(), ((float)canvas.getHeight())/bitmap.getHeight());
			//matrix.postRotate(90);
			//matrix.postScale(((float)canvas.getWidth())/bitmap.getHeight(), ((float)canvas.getHeight())/bitmap.getWidth());
			
			//bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			
			bitmap = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), true);
			//bitmap = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), true);
			
			/*
			int[] pixs = new int[bitmap.getWidth() * bitmap.getHeight()];
			bitmap.getPixels(pixs, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
			*/
			
			canvas.drawBitmap(bitmap, 0, 0, paint);
			
			
			photoHolder.unlockCanvasAndPost(canvas);
			//Log.e("println", "scale height and width:"+ sh + ", "+sw);
			
			
			// Lock the process canvas
			canvas = processHolder.lockCanvas();
			paint = new Paint();
			bitmap = ImageCompare.convertGreyImg(bitmap);
			canvas.drawBitmap(bitmap, 0, 0, paint);
			processHolder.unlockCanvasAndPost(canvas);
			
			
			// we don't implement the callback now
			/*
			if(checker != null) {
				checker.photoCallback(imagePath);
			}
			*/
		}
	};
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		camera.startPreview();
		hasStartPreview = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		try {
			camera.setPreviewDisplay(previewHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
    @Override  
    protected void onResume() {  
        if (camera == null) {  
           	initCamera();
           	try {
				camera.setPreviewDisplay(previewHolder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if(!hasStartPreview) {
        	camera.startPreview();
        	hasStartPreview = true;
        }
        super.onResume();  
    } 
    
    boolean hasStartPreview = false;
    @Override  
    protected void onPause() {  
    	/*
        if (hasStartPreview) {  
            mCam.stopPreview();  
        } 
        */ 
        camera.release();  
        camera = null;  
        hasStartPreview = false;  
        super.onPause();  
    }

	@Override
	public void takePhoto(String path) {
		// TODO Auto-generated method stub
		//imagePath = path;
		if(camera != null) {
			camera.takePicture(null, null, pictureCallback);
		}
	}

	
	private Checker checker = null;
	@Override
	public void setCallback(Checker cr) {
		// TODO Auto-generated method stub
		checker = cr;
	}

	
	
	@Override
	public void checkCallback(boolean empty) {
		// TODO Auto-generated method stub
		if(empty) {
			beginButton.setText("true");
		} else {
			beginButton.setText("false");
		}
	} 

    
 
}
