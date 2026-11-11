package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.levels.LevelManager;
import pt.iscte.poo.objects.*;
import pt.iscte.poo.utils.Point2D;

import java.awt.event.KeyEvent;
import java.util.*;

public class GameEngine {
	private boolean is_running = false;
	private LevelManager levelManager;

	//Current level state
	private List<ImageTile> current_level_layout;

	private JumperGuy jumperGuy = new JumperGuy(new Point2D(0, 0));

	private final int TIME_LIMIT = 2000; // Time limit
	
	public GameEngine() {
		levelManager = new LevelManager();
		//Loads the first level
		levelManager.loadLevel(jumperGuy);
		current_level_layout = levelManager.getLevelLayout();
	}

	public void setRunning() {
		is_running = true;
	}

	public void keyPressed(int key) {
		if (is_running) {
			if (key == KeyEvent.VK_UP) {
				if (jumperGuy.checkOpenDoor() && levelManager.getLevel_number() > 0) {
					levelManager.getLevels().get(levelManager.getLevel_number()).setJumperGuyPosition(jumperGuy.getPosition());
					levelManager.setCurrentLevelLayout(current_level_layout);
					levelManager.loadPreviousLevel(jumperGuy);
					current_level_layout = levelManager.getLevelLayout();
				} else if (jumperGuy.checkClosedDoor()) {
					levelManager.getLevels().get(levelManager.getLevel_number()).setJumperGuyPosition(jumperGuy.getPosition());
					levelManager.setCurrentLevelLayout(current_level_layout);
					levelManager.loadNextLevel(jumperGuy);
					current_level_layout = levelManager.getLevelLayout();
				} else {
					jumperGuy.jump();
				}
			} else if (key == KeyEvent.VK_LEFT) {
				jumperGuy.moveLeft();
			} else if (key == KeyEvent.VK_RIGHT) {
				jumperGuy.moveRight();
			} else if (key == KeyEvent.VK_DOWN) {
				jumperGuy.moveDown();
			}
		}
	}

	public void tick(int ticks) {
		if (is_running) {
			//End the program if the player loses
			if (jumperGuy.getHp() <= 0) {
				is_running = false;
				GameEnd gameEnd = new GameEnd();
				gameEnd.gameOver();
			}
			//Start winning cutscene when the player reaches the princess
			if (jumperGuy.getWin()){
				is_running = false;
				GameEnd gameEnd = new GameEnd();
				gameEnd.gameWon(jumperGuy, TIME_LIMIT);
			}

			//Update JumperGuys Enemies and Projectiles position
			jumperGuy.setEnemies(levelManager.getEnemies());
			jumperGuy.setProjectiles(levelManager.getProjectiles());
			jumperGuy.setItems(levelManager.getItems());

			levelManager.moveEnemies(jumperGuy, current_level_layout);
			levelManager.moveProjectiles();

			jumperGuy.move();

			levelManager.removeItems();

			ImageGUI gui = ImageGUI.getInstance();

			int time_bonus = 0;
			if(!(ticks > TIME_LIMIT)){
				time_bonus = (TIME_LIMIT-ticks)*5;
			}
			gui.setStatusMessage("Hp: " + jumperGuy.getHp() + " | Strength: " + jumperGuy.getStrength() + " | Defense: " + jumperGuy.getDefense() + " | Score: " + jumperGuy.getScore() + " | Time bonus: " + time_bonus);

			System.out.println("Tic tac.. " + ticks);

		}
	}
}