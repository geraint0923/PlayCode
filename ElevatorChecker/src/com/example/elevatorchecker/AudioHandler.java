package com.example.elevatorchecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;



public class AudioHandler implements Runnable {
	
	class IOBase {
		public short[] BufferBase;
		public int Size;
	}
	
	private AudioRecord audioRecord;
	
	private int minBufferSize;
	
	private boolean isRecording = false;
	
	private FileOutputStream fileOutputStream = null;
	
	private ArrayList<IOBase> bufferList = null;
	
	public AudioHandler() {
		 minBufferSize =AudioRecord.getMinBufferSize(44100,
                 AudioFormat.CHANNEL_IN_MONO,
                 AudioFormat.ENCODING_PCM_16BIT);
		 audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
		         44100,
		         AudioFormat.CHANNEL_IN_MONO,
		         AudioFormat.ENCODING_PCM_16BIT,
		         minBufferSize);
	}
	
	public void start() {
		if(isRecording)
			return;
		isRecording = true;
		try {
			fileOutputStream = new FileOutputStream(new File("/sdcard/Music/audio_raw"));
			bufferList = new ArrayList<IOBase>();
			new Thread(this).start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		if(!isRecording)
			return;
		isRecording = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(fileOutputStream == null || audioRecord == null || bufferList == null)
			return;
		int shortSize = minBufferSize / 2;
		audioRecord.startRecording();
		while(isRecording) {
			short[] buffer = new short[shortSize];
			IOBase base = new IOBase();
			base.BufferBase = buffer;
			base.Size = audioRecord.read(base.BufferBase, 0, shortSize);
			bufferList.add(base);
		}
		audioRecord.stop();
		int len = bufferList.size();
		for(int i = 0; i < len; ++i) {
			IOBase iob = bufferList.get(i);
			for(int j = 0; j < iob.Size; ++j) {
				try {
					fileOutputStream.write((iob.BufferBase[j]+",").getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileOutputStream = null;
		
	}
	
}