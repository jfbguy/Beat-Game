package com.badlogic.audio.samples.part7;

public class FindThreshTest {
	
	public static void main( String[] argv ){
		PeakFinder george = new PeakFinder("samples/explosivo.mp3");
		System.out.println(george.returnPeaks().toString());

	}
}
