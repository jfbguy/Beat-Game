package edu.mills.cs280.audiorunner;

public class FindThreshTest {
	

	public static void main( String[] argv ){
		MusicData.setFile("data/music/test.mp3");
		PeakFinder george = new PeakFinder();
		System.out.println("Live Read:");
		System.out.println(george.returnPeaks().toString());
		//the code complicit with the following breaks the game, so it is not used
//		george.setTestable(true);
//		System.out.println("Using saved test data");
//		System.out.println(george.returnPeaks().toString());	
		}
}
