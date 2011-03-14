package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.backends.jogl.JoglApplication;

//Tested branch with this comment!!!
//New comment, woot!

public class DesktopStarter {
	public static void main(String[] args){
		new JoglApplication(new GameHandler(),
							"title",
							480, 320, false);
	}
}
