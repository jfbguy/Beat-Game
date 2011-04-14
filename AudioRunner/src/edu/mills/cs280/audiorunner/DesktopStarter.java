package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.backends.jogl.JoglApplication;

//Tested branch with this comment!

public class DesktopStarter {
	public static void main(String[] args){
		
		//MusicHandler musicTest = new MusicHandler("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		//get druation of song
		float[] samples = new float[1024];
		AudioAnalyzer analyzer = new AudioAnalyzer("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		System.out.println(analyzer.readSamples(samples));
		System.exit(0);
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
