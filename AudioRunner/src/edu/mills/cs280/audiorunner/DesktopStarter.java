package edu.mills.cs280.audiorunner;


import java.util.List;

import com.badlogic.gdx.backends.jogl.JoglApplication;

//Tested branch with this comment!

public class DesktopStarter {
	public static void main(String[] args){

		MusicData.setFile("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		PeakFinder songData = new PeakFinder(MusicData.getFileLocation());
		List<Float> peaks = songData.returnPeaks();
		MusicData.setpeaks(peaks);

		new JoglApplication(new GameHandler(),
				"Audio Runner",
				854, 480, false);
	}
}
