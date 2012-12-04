package com.msra.elevatorchecker;

import java.io.IOException;
import java.lang.reflect.Array;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class ImageCapturer {
	private ImageCapturer instance = null;
	
	private byte[] imageBuffer;
	
	private int width = 1024;
	private int height = 768;
	private Camera camera;
	
	public boolean pictureFinished = true;
	
	private SurfaceView surface = null;
	
	public ImageCapturer(SurfaceView sf) {
		instance = this;
		surface = sf;
		surface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		
		
		
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub
			System.out.println("Picture Taken!!!");
			byte[] buffer = new byte[arg0.length];
			System.arraycopy(arg0, 0, buffer, 0, buffer.length);
			
			if(camera != null) {
				//camera.release();
				//camera = null;
			}
			
			if(instance != null) {
				instance.notify();
			}
			
			System.out.println("Picrure Finish!");
			
		}
	};

	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public void openCamera() {
		camera = Camera.open();
		try {
			SurfaceHolder ho = surface.getHolder();
			System.out.println("holder:"+ho);
			camera.setPreviewDisplay(ho);
			camera.startPreview();
			System.out.println("finish set preview display");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			camera.release();
			camera = null;
		}
	}
	
	public void startPreview() {
		if(camera == null)
			return;
		camera.startPreview();
		System.out.println("finishi start preview!!!!!!");
	}
	
	public void takePicture() {
		
		if(camera == null)
			return;
		
		System.out.println("camera::::"+camera);
		camera = Camera.open();
		System.out.println("camera::::"+camera);
		if(camera != null) {
			
			pictureFinished = false;
			
			camera.takePicture(null, null, pictureCallback);
			
			/*
			try {
				
				synchronized(this) {
				//if(!pictureFinished) {
					wait();
				//}			
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
		}
		
		return;
	}
	
	public byte[] getRawBuffer() {
		if(imageBuffer == null) {
			return null;
		}
		return null;
	}
	
	public Bitmap getBitmap() {
		if(imageBuffer == null) {
			return null;
		}
		return BitmapFactory.decodeByteArray(imageBuffer, 0, imageBuffer.length);
	}
	
}