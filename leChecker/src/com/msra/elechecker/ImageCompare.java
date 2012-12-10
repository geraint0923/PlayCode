package com.msra.elechecker;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;



public class ImageCompare {
	
	static final int COMPARE_RATE = 0;
	static final int COMPARE_ABS = 1;
	
	private int type;
	public ImageCompare(int t) {
		type = t;
	}
	
	private static final float thresholdRate = 3.6f;
	private static final int thresholdAbs = 5;
	
	
	private static final int thresholdDark = 6;
	public static boolean compareDark(Bitmap img) {
		int w = img.getWidth();
		int h = img.getHeight();
		int len = w * h;
		int[] pix = new int[len];
		
		for(int i = 0; i < len; ++i) {
			if((pix[i] & 0xff) > thresholdDark)
				return false;
		}
		return true;
	}
	
	private boolean compareRate(Bitmap a, Bitmap b) {
		int wa = a.getWidth();
		int ha = a.getHeight();
		int wb = b.getWidth();
		int hb = b.getHeight();
		if(wa != wb || ha != hb)
			return false;
		
		int len = wa * ha;
		int[] pixa = new int[len];
		int[] pixb = new int[len];
		
		a.getPixels(pixa, 0, wa, 0, 0, wa, ha);
		b.getPixels(pixb, 0, wb, 0, 0, wb, hb);
		
		int sum = 0;
		for(int i = 0; i < len; ++i) {
			sum += Math.abs((pixa[i] & 0xff) - (pixb[i] & 0xff));
		}
		if(sum == 0)
			return true;
		
		float per = ((float)sum)/len;
		for(int i = 0; i < len; ++i) {
			float gap = (float)Math.abs((pixa[i] & 0xff) - (pixb[i] - 0xff));
			if(gap / per > thresholdRate) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean compareAbs(Bitmap a, Bitmap b) {
		int wa = a.getWidth();
		int ha = a.getHeight();
		int wb = b.getWidth();
		int hb = b.getHeight();
		if(wa != wb || ha != hb)
			return false;
		
		int len = wa * ha;
		int[] pixa = new int[len];
		int[] pixb = new int[len];
		
		a.getPixels(pixa, 0, wa, 0, 0, wa, ha);
		b.getPixels(pixb, 0, wb, 0, 0, wb, hb);
		
		for(int i = 0; i < len; ++i) {
			int gap = Math.abs((pixa[i] & 0xff) - (pixb[i] & 0xff));
			if(gap > thresholdAbs)
				return false;
		}
		
		return true;
	}
	
	
	// return:
	// 	true  -- the same
	// 	false -- different
	public boolean compareBitmap(Bitmap a, Bitmap b) {
		switch(type) {
		case COMPARE_RATE:
			return compareRate(a, b);
		case COMPARE_ABS:
			return compareAbs(a, b);
		}
		return false;
	}
	
	
    static public Bitmap convertGreyImg(Bitmap img) {
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