package com.msra.elevatorchecker;

import java.lang.reflect.Array;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;



public class ImageCapturer {
	private ImageCapturer instance = null;
	
	private byte[] imageBuffer;
	
	private int width = 1024;
	private int height = 768;
	private Camera camera;
	
	public boolean pictureFinished = true;
	
	private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		
		
		
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub
			System.out.println("Picture Taken!!!");
			byte[] buffer = new byte[arg0.length];
			System.arraycopy(arg0, 0, buffer, 0, buffer.length);
			
			if(camera != null) {
				camera.release();
				camera = null;
			}
			
			if(instance != null) {
				instance.notify();
			}
			
			System.out.println("Picrure Finish!");
			
		}
	};
	
	public ImageCapturer() {
		instance = this;
	}
	
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
	
	public void takePicture() {
		
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