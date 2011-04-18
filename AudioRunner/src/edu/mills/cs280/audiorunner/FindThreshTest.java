package edu.mills.cs280.audiorunner;

public class FindThreshTest {
	private static String TEST_FILE = "data/music/Freezepop - Starlight (Karacter Remix).mp3";
	
	public static void main( String[] argv ){
		PeakFinder george = new PeakFinder(TEST_FILE);
		System.out.println(george.returnPeaks().toString());

	}
}
