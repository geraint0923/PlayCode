package com.msra.elechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	private SurfaceView previewSurface = null;
	private SurfaceHolder previewHolder = null;
	
	private SurfaceView photoSurface = null;
	private SurfaceHolder photoHolder = null;
	
	private SurfaceView processSurface = null;
	private SurfaceHolder processHolder = null;
	
	private Camera camera = null;
	
	private Button photoButton = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		camera = Camera.open();
		
		camera.setDisplayOrientation(90);
		
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
			
			
			File file = new File("/sdcard/mypic.jpeg");
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
			
			System.out.println("=================I have take a photo!!!!!===================size:"+ arg0);
			
			
			// Lock the photo canvas
			Canvas canvas = photoHolder.lockCanvas();
			
			Paint paint = new Paint();
			Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/mypic.jpeg");
			
			Matrix matrix = new Matrix();
			//matrix.postScale(((float)canvas.getWidth())/bitmap.getWidth(), ((float)canvas.getHeight())/bitmap.getHeight());
			matrix.postRotate(90);
			matrix.postScale(((float)canvas.getWidth())/bitmap.getHeight(), ((float)canvas.getHeight())/bitmap.getWidth());
			
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			
			//bitmap = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), true);
			
			int[] pixs = new int[bitmap.getWidth() * bitmap.getHeight()];
			bitmap.getPixels(pixs, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
			
			
			canvas.drawBitmap(bitmap, 0, 0, paint);
			
			
			photoHolder.unlockCanvasAndPost(canvas);
			//Log.e("println", "scale height and width:"+ sh + ", "+sw);
			
			
			// Lock the process canvas
			canvas = processHolder.lockCanvas();
			paint = new Paint();
			bitmap = convertGreyImg(bitmap);
			canvas.drawBitmap(bitmap, 0, 0, paint);
			processHolder.unlockCanvasAndPost(canvas);
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
        if (camera == null)  
           	camera = Camera.open();  
        super.onResume();  
    } 
    
    @Override  
    protected void onPause() {  
    	/*
        if (hasStartPreview) {  
            mCam.stopPreview();  
        } 
        */ 
        camera.release();  
        camera = null;  
        //hasStartPreview = false;  
        super.onPause();  
    } 

    
    public Bitmap convertGreyImg(Bitmap img) {
    	int width = img.getWidth();
    	int height = img.getHeight();

    	int []pixels = new int[width * height];

    	img.getPixels(pixels, 0, width, 0, 0, width, height);
    	int alpha = 0xFF << 24;
    	for(int i = 0; i < height; i++) {
	    	for(int j = 0; j < width; j++) {
		    	int grey = pixels[width * i + j];
		
		    	int red = ((grey & 0x00FF0000 ) >> 16);
		    	int green = ((grey & 0x0000FF00) >> 8);
		    	int blue = (grey & 0x000000FF);
		
		    	grey = (int)((float) red * 0.229 + (float)green * 0.587 + (float)blue * 0.114);
		    	grey = alpha | (grey << 16) | (grey << 8) | grey;
		    	pixels[width * i + j] = grey;
	    	}
    	}
    	Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
    	result.setPixels(pixels, 0, width, 0, 0, width, height);
    	return result;
    } 
}
