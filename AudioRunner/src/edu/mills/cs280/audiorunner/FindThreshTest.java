package edu.mills.cs280.audiorunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FindThreshTest {
	private static String TEST_FILE = "data/music/Freezepop - Starlight (Karacter Remix).mp3";

	public static void main( String[] argv ){
		PeakFinder george = new PeakFinder(TEST_FILE);
		george.returnPeaks();
		System.exit(1);
		
		SerializableMusicData test = SerializableMusicData.load();
		//		System.out.println(test.getPeaks());
		//		System.exit(7);
		
		ArrayList<float[]> samples = test.getSamples();
		/*for(float[] ff : samples){
			for(float f : ff){
				System.out.print(f);
			}
			System.out.println();
		}*/
		
		System.exit(1);

		FileOutputStream outStream;
		ObjectOutputStream objectOutStream;
		try {
			outStream = new FileOutputStream("data/testSamples.ar");
			objectOutStream = new ObjectOutputStream(outStream);
			objectOutStream.writeObject(test.getSamples());
			objectOutStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			outStream = new FileOutputStream("data/testPeaks.ar");
			objectOutStream = new ObjectOutputStream(outStream);
			objectOutStream.writeObject(test.getPeaks());
			objectOutStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(1);

		//PeakFinder george = new PeakFinder(TEST_FILE);
		//System.out.println(george.returnPeaks().toString());

	}
}
