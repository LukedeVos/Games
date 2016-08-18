package RPG;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	Main game;

	public KeyInput(Main game) {
		this.game = game;
	}

	public void keyPressed(KeyEvent k) {
		game.keyPressed(k);
	}

	public void keyReleased(KeyEvent k) {
		game.keyReleased(k);
	}
}