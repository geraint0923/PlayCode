package com.msra.elechecker;


public class PeakDetector {
	
	private static final int windowSize = 7;
	private static final float GAP_THRESHOLD = 0.3f;
	
	private int counter = -1;
	private float [] windowValue;
	private int numberCount = 0;
	public PeakDetector() {
		windowValue = new float[windowSize + 1];
		
	}
	
	private void insertValue(float val) {
		if(numberCount != windowSize) {
			++numberCount;
		}
		++counter;
		counter %= windowSize + 1;
		windowValue[counter] = windowValue[(counter + windowSize) % (windowSize + 1)] + val;
	}
	
	private float prevValue = 0;
	private float lastGap = 0;
	
	public boolean newValue(float val) {
		insertValue(val);
		float avgValue = (windowValue[counter] - windowValue[(counter + 1) % (windowSize + 1)]) / windowSize;
		float gap = avgValue - prevValue;
		if(Math.abs(gap) > GAP_THRESHOLD && gap * lastGap < 0) {
			lastGap = gap;
			return true;
		}
		lastGap = gap;
		return false;
	}
}