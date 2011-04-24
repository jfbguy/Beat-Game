package edu.mills.cs280.audiorunner;

import java.util.List;

public class FindThreshTest {
	
	public static void main( String[] argv ){
		MusicData.setFile("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		PeakFinder george = new PeakFinder();
		System.out.println("Live Read:");
		List peaks = george.returnPeaks();
		System.out.println(peaks.toString());
		System.out.println("size = " + peaks.size());
		george.setTestable(true);
		System.out.println("Using saved test data");
		System.out.println(george.returnPeaks().toString());	
		System.out.println("size = " + george.returnPeaks().size());
		
	}
}
