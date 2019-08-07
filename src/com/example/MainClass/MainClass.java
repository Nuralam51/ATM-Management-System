package com.example.MainClass;

import java.io.File;
import javax.swing.UIManager;
import com.example.Admin.LogIn;
import jaco.mp3.player.MP3Player;

public class MainClass {
	
	public static void main(String[] args) 
	{
		try {
			//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			//UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		LogIn login = new LogIn();
		String song = "audio/Welcome.mp3";
		MP3Player audio = new MP3Player(new File(song));
		audio.play();
	}
}