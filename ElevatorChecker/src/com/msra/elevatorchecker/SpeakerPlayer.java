package com.msra.elevatorchecker;

import java.io.IOException;
import android.media.MediaPlayer;


public class SpeakerPlayer {
	
	private MediaPlayer player;
	
	private boolean isPlaying = false;
	
	public SpeakerPlayer(String path) {		
		player  =   new MediaPlayer();
        try {
			player.setDataSource(path);
			player.prepare();
			player.setLooping(true);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}              
	}
	
	public void start() {
		if(!isPlaying) {
			player.start();
			isPlaying = true;
		}
	}
	
	public void pause() {
		if(isPlaying) {
			player.pause();
			isPlaying = false;
		}
	}
}