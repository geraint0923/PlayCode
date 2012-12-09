package com.msra.elechecker;

import android.graphics.Bitmap;



public class ImageCompare {
	
	static final int COMPARE_RATE = 0;
	static final int COMPARE_ABS = 1;
	
	private int type;
	public ImageCompare(int t) {
		type = t;
	}
	
	private float thresholdRate = 3.6f;
	private int thresholdAbs = 5;
	
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
			sum += Math.abs(pixa[i] - pixb[i]);
		}
		if(sum == 0)
			return true;
		
		float per = ((float)sum)/len;
		for(int i = 0; i < len; ++i) {
			float gap = (float)Math.abs(pixa[i] - pixb[i]);
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
			int gap = Math.abs(pixa[i] - pixb[i]);
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
}