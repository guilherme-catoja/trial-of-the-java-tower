package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.AudioPlayer.AudioPlayer;

public class Main {

	public static void main(String[] args) {
		ImageGUI gui = ImageGUI.getInstance();
		GameEngine engine = new GameEngine();
		gui.setName("Trial of the Java Tower");
		gui.setStatusMessage("Good luck!");
		gui.setEngine(engine);
		gui.go();

		gui.showMessage("Controls", "Left and right arrows: Move left and right \n \nUp arrow: jump and enter doors \n \nDown arrow: go down stairs \n \nHeadphones recommended");
		engine.setRunning();

		AudioPlayer backgroundMusic = new AudioPlayer();
		try{
			backgroundMusic.load("music/Music.wav");
		}catch (Exception e){
			e.printStackTrace();
		}
		backgroundMusic.loop(true);
		backgroundMusic.play();

		while(true) {
			gui.update();
		}
	}

}
