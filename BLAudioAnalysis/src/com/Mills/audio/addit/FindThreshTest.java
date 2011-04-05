package com.Mills.audio.addit;

public class FindThreshTest {
	
	public static void main( String[] argv ){
		PeakFinder george = new PeakFinder("samples/explosivo.mp3");
		System.out.println(george.returnPeaks().toString());

	}
}
