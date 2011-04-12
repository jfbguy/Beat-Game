package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.backends.jogl.JoglApplication;

//Tested branch with this comment!

public class DesktopStarter {
	public static void main(String[] args){
		
		//AudioAnalyzer analyzer = new AudioAnalyzer("data/music/Freezepop - Starlight (Karacter Remix).mp3");
		//System.out.println(analyzer.getData().toString());
		//System.exit(0);
		
		new JoglApplication(new GameHandler(),
							"Audio Runner",
							854, 480, false);
	}
}
