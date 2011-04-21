package edu.mills.cs280.audiorunner;

public class FindThreshTest {
	private static String TEST_FILE = "data/music/Freezepop - Starlight (Karacter Remix).mp3";
	
	public static void main( String[] argv ){
//		SerializableMusicData test = SerializableMusicData.load();
//		System.out.println(test.getPeaks());
//		System.exit(7);
		
		PeakFinder george = new PeakFinder(TEST_FILE);
		System.out.println(george.returnPeaks().toString());

	}
}
