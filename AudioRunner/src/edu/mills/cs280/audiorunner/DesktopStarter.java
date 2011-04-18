package edu.mills.cs280.audiorunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;

import com.badlogic.gdx.backends.jogl.JoglApplication;

//Tested branch with this comment!

public class DesktopStarter {
	public static void main(String[] args){

		//MusicHandler musicTest = new MusicHandler("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		//get duration of song
		float[] samples = new float[1024];
		
		

		/*
		new Thread(new Runnable() {
			public void run() {
				try{

					javazoom.jl.player.Player music = new javazoom.jl.player.Player(new FileInputStream(new File("data/music/Freezepop - Starlight (Karacter Remix).mp3")));
					music.play();
				}catch(FileNotFoundException e){

				}catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}).start();
		*/

		/*
		PeakFinder peakfinder = new PeakFinder("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		System.out.println(peakfinder.returnPeaks().toString());
		System.exit(0);*/

		//PeakFinder george = new PeakFinder("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		//System.out.println(george.returnPeaks().toString());

		new JoglApplication(new GameHandler(),
				"Audio Runner",
				854, 480, false);
	}
}
