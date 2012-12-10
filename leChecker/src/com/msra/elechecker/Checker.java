package com.msra.elechecker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class Checker implements Runnable {
	public Checker() {
		
	}
	
	private ICamera camera = null;
	private ICheckerCallback checkerCallback = null;
	
	private int waitTime = 0;
	
	private boolean hasCalibrated = false;
	
	private Bitmap calibrateBitmap = null;
	
	private static String CALIBRATE_IMAGE_PATH = "/sdcard/ele_images/calibrate.jpeg";
	private static String SAMPLE_IMAGE_PATH = "/sdcard/ele_images/sample.jpeg";
	
	
	private ImageCompare compare;
	
	public void setCamera(ICamera cam) {
		camera = cam;
		camera.setCallback(this);
	}
	
	public void setCheckerCallback(ICheckerCallback cb) {
		checkerCallback = cb;
	}
	
	public void photoCallback(String path) {
		Bitmap bitmap = ImageCompare.convertGreyImg(BitmapFactory.decodeFile(path));
		if(path.equals(CALIBRATE_IMAGE_PATH)) {
			calibrateBitmap = bitmap;
			if(calibrateBitmap != null) {
				hasCalibrated = true;
			} else {
				hasCalibrated = false;
			}
		} else {
			if(compare == null) {
				compare = new ImageCompare(ImageCompare.COMPARE_ABS);
			}
			boolean ret = compare.compareBitmap(bitmap, calibrateBitmap);
			if(checkerCallback != null) {
				checkerCallback.checkCallback(ret);
			}
		}
	}
	
	public void calibrate(int milsec) {
		if(camera == null)
			return;
		waitTime = milsec;
		// TODO : add calibrate code
		new Thread(this).start();
		
	}
	
	
	// To check if the elevator is empty
	// true  --- empty
	// false --- not empty
	public void checkEmpty(ICheckerCallback cb) {
		checkerCallback = cb;
		camera.takePhoto(SAMPLE_IMAGE_PATH);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.takePhoto(CALIBRATE_IMAGE_PATH);
	}
}