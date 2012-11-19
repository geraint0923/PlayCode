package com.example.elevatorchecker;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.widget.EditText;




public class AudioRecorder {

	private boolean isRecording = false;
	
	private boolean canUse = true;
	
	private MediaRecorder recorder;
	
	public AudioRecorder(String path) {
		recorder = new MediaRecorder();
		
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); 
		
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		
		recorder.setAudioSamplingRate(44100);
		
		File file = new File("/sdcard/Music/recorder.wav");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		recorder.setOutputFile(path);  
		
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	
	public void start() {
		if(!isRecording && canUse) {
			recorder.start();
			isRecording = true;
		}
	}
	
	public void stop() {
		if(isRecording) {
			recorder.stop();
			recorder.release();
			canUse = false;
		}
	}
}