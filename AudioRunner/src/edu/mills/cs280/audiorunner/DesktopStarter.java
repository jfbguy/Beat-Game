package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class DesktopStarter {
	public static void main(String[] args){
		new JoglApplication(new AudioRunner(),
							"title",
							480, 320, false);
	}
}
